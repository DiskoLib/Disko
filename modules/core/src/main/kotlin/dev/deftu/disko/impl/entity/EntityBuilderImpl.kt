/*
 * Copyright (C) 2024 Deftu and the Disko contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.deftu.disko.impl.entity

import com.google.gson.JsonObject
import dev.deftu.disko.api.Disko
import dev.deftu.disko.api.entity.EntityBuilder
import dev.deftu.disko.api.entity.Locale
import dev.deftu.disko.api.entity.VoiceRegion
import dev.deftu.disko.api.entity.channel.*
import dev.deftu.disko.api.entity.emoji.Emoji
import dev.deftu.disko.api.entity.emoji.Sticker
import dev.deftu.disko.api.entity.emoji.StickerFormat
import dev.deftu.disko.api.entity.emoji.StickerType
import dev.deftu.disko.api.entity.guild.*
import dev.deftu.disko.api.entity.guild.member.Member
import dev.deftu.disko.api.entity.guild.member.MemberFlag
import dev.deftu.disko.api.entity.message.Message
import dev.deftu.disko.api.entity.message.MessageEmbed
import dev.deftu.disko.api.entity.message.MessageFlag
import dev.deftu.disko.api.entity.message.MessageType
import dev.deftu.disko.api.entity.user.SelfUser
import dev.deftu.disko.api.entity.user.User
import dev.deftu.disko.api.entity.user.UserFlag
import dev.deftu.disko.api.utils.*
import dev.deftu.disko.api.utils.Snowflake.Companion.toSnowflake
import dev.deftu.disko.impl.entity.channel.*
import java.awt.Color
import java.time.Instant

public class EntityBuilderImpl(private val instance: Disko) : EntityBuilder {

    override fun user(obj: JsonObject): User? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val username = obj.maybeGetString("username") ?: return null
        val discriminator = obj.maybeGetString("discriminator") ?: return null
        val avatar = obj.maybeGetString("avatar")
        val bot = obj.maybeGetBoolean("bot") ?: false
        val system = obj.maybeGetBoolean("system") ?: false
        val mfaEnabled = obj.maybeGetBoolean("mfa_enabled") ?: false
        val locale = Locale.from(obj.maybeGetString("locale") ?: Locale.DEFAULT) ?: return null
        val publicFlags = UserFlag.fromBitset(obj.maybeGetInteger("flags") ?: 0)

        return User(
            id,
            username,
            discriminator,
            avatar,
            bot,
            system,
            mfaEnabled,
            locale,
            publicFlags
        )
    }

    override fun selfUser(obj: JsonObject): SelfUser? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val username = obj.maybeGetString("username") ?: return null
        val discriminator = obj.maybeGetString("discriminator") ?: return null
        val avatar = obj.maybeGetString("avatar")
        val isMfaEnabled = obj.maybeGetBoolean("mfa_enabled") ?: return null
        val locale = Locale.from(obj.maybeGetString("locale") ?: Locale.DEFAULT) ?: return null
        val publicFlags = UserFlag.fromBitset(obj.maybeGetInteger("flags") ?: 0)

        return SelfUser(
            id,
            username,
            discriminator,
            avatar,
            isMfaEnabled,
            locale,
            publicFlags
        )
    }

    override fun emoji(obj: JsonObject): Emoji? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val name = obj.maybeGetString("name") ?: return null
        val roles = obj.maybeGetJsonArray("roles")?.mapNotNull { roleElem ->
            if (!roleElem.isJsonPrimitive) {
                return@mapNotNull null
            }

            val roleId = roleElem.asLong
            instance.entityCache.getRole(roleId.toSnowflake())
        }?.toSet() ?: emptySet()
        val user = obj.maybeGetJsonObject("user")?.let(::user)
        val requireColons = obj.maybeGetBoolean("require_colons") ?: false
        val managed = obj.maybeGetBoolean("managed") ?: false
        val animated = obj.maybeGetBoolean("animated") ?: false
        val available = obj.maybeGetBoolean("available") ?: false

        return Emoji(
            id,
            name,
            roles,
            user,
            requireColons,
            managed,
            animated,
            available
        )
    }

    override fun sticker(obj: JsonObject): Sticker? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val packId = obj.maybeGetSnowflake("pack_id")
        val name = obj.maybeGetString("name") ?: return null
        val description = obj.maybeGetString("description")
        val tags = obj.maybeGetJsonArray("tags")?.mapNotNull { it.asString }?.toSet() ?: emptySet()
        val type = StickerType.from(obj.maybeGetInteger("type") ?: return null) ?: return null
        val formatType = StickerFormat.from(obj.maybeGetInteger("format_type") ?: return null) ?: return null
        val available = obj.maybeGetBoolean("available") ?: false
        val guildId = obj.maybeGetSnowflake("guild_id")
        val user = obj.maybeGetJsonObject("user")?.let(::user)
        val sortValue = obj.maybeGetInteger("sort_value")

        return Sticker(
            id,
            packId,
            name,
            description,
            tags,
            type,
            formatType,
            available,
            guildId,
            user,
            sortValue
        )
    }

    override fun voiceRegion(obj: JsonObject): VoiceRegion? {
        val id = obj.maybeGetString("id") ?: return null
        val name = obj.maybeGetString("name") ?: return null
        val optimal = obj.maybeGetBoolean("optimal") ?: false
        val deprecated = obj.maybeGetBoolean("deprecated") ?: false
        val custom = obj.maybeGetBoolean("custom") ?: false

        return VoiceRegion(
            id,
            name,
            optimal,
            deprecated,
            custom
        )
    }

    override fun roleTags(obj: JsonObject): RoleTags {
        val botId = obj.maybeGetSnowflake("bot_id")
        val integrationId = obj.maybeGetSnowflake("integration_id")
        val premiumSubscriber = obj.maybeGetBoolean("premium_subscriber") ?: false
        val subscriptionListingId = obj.maybeGetSnowflake("subscription_listing_id")
        val availableForPurchase = obj.maybeGetBoolean("available_for_purchase") ?: false
        val linkedRole = obj.maybeGetBoolean("linked_role") ?: false

        return RoleTags(
            botId,
            integrationId,
            premiumSubscriber,
            subscriptionListingId,
            availableForPurchase,
            linkedRole
        )
    }

    override fun role(obj: JsonObject): Role? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val name = obj.maybeGetString("name") ?: return null
        val color = obj.maybeGetInteger("color") ?: 0
        val hoist = obj.maybeGetBoolean("hoist") ?: false
        val icon = obj.maybeGetString("icon")
        val position = obj.maybeGetInteger("position") ?: 0
        val permissions = Permission.fromBitset(obj.maybeGetLong("permissions") ?: 0)
        val managed = obj.maybeGetBoolean("managed") ?: false
        val mentionable = obj.maybeGetBoolean("mentionable") ?: false
        val tags = obj.maybeGetJsonObject("tags")?.let(::roleTags)
        val flags = RoleFlag.fromBitset(obj.maybeGetInteger("flags") ?: 0)

        return Role(
            id,
            name,
            Color(color),
            hoist,
            icon,
            position,
            permissions,
            managed,
            mentionable,
            tags,
            flags
        )
    }

    override fun permissionOverwrite(obj: JsonObject): PermissionOverwrite? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val type = PermissionOverwriteType.from(obj.maybeGetInteger("type") ?: return null) ?: return null
        val allow = Permission.fromBitset(obj.maybeGetLong("allow") ?: 0)
        val deny = Permission.fromBitset(obj.maybeGetLong("deny") ?: 0)

        return when (type) {
            PermissionOverwriteType.ROLE -> RolePermissionOverwrite(id, allow, deny)
            PermissionOverwriteType.MEMBER -> MemberPermissionOverwrite(id, allow, deny)
        }
    }

    override fun channel(guild: Guild?, shardId: Int, obj: JsonObject): Channel? {
        val type = ChannelType.from(obj.maybeGetInteger("type") ?: 0) ?: return null
        val name = obj.maybeGetString("name") ?: return null

        return when (type) {
            ChannelType.GUILD_TEXT -> guildTextChannel(guild ?: return null, shardId, name, obj)
            ChannelType.DM -> directMessageChannel(shardId, name, obj)
            ChannelType.GUILD_VOICE -> guildVoiceChannel(guild!!, shardId, name, obj)
            ChannelType.GROUP_DM -> groupDirectMessageChannel(shardId, name, obj)
            ChannelType.GUILD_CATEGORY -> guildCategoryChannel(guild ?: return null, shardId, name, obj)
            ChannelType.GUILD_ANNOUNCEMENT -> guildAnnouncementChannel(guild ?: return null, shardId, name, obj)
            ChannelType.ANNOUNCEMENT_THREAD -> threadChannel(guild ?: return null, shardId, name, obj)
            ChannelType.PUBLIC_THREAD, ChannelType.PRIVATE_THREAD -> threadChannel(guild ?: return null, shardId, name, obj)
            ChannelType.GUILD_STAGE_VOICE -> guildVoiceChannel(guild ?: return null, shardId, name, obj)
            else -> null
        }
    }

    override fun guildTextChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildMessageChannel? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(obj.maybeGetInteger("type") ?: 0) ?: ChannelType.GUILD_TEXT
        val position = obj.maybeGetInteger("position")
        val permissionOverwrites = obj.maybeGetJsonArray("permission_overwrites")?.mapNotNull { permissionOverwriteElem ->
            if (!permissionOverwriteElem.isJsonObject) {
                return@mapNotNull null
            }

            permissionOverwrite(permissionOverwriteElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val topic = obj.maybeGetString("topic") ?: ""
        val nsfw = obj.maybeGetBoolean("nsfw") ?: false
        val rateLimitPerUser = obj.maybeGetInteger("rate_limit_per_user") ?: 0
        val parentId = obj.maybeGetSnowflake("parent_id")
        val parent = if (parentId != null) instance.entityCache.getChannel(parentId)?.asGuildChannel() else null
        val lastPinTimestamp = obj.maybeGetString("last_pin_timestamp")?.let(Instant::parse)
        val lastMessageId = obj.maybeGetSnowflake("last_message_id")

        return GuildMessageChannelImpl(
            shardId,
            name,
            lastMessageId,
            lastPinTimestamp,
            id,
            type,
            guild,
            position,
            permissionOverwrites,
            topic,
            nsfw,
            rateLimitPerUser,
            parent,
            instance
        )
    }

    override fun directMessageChannel(shardId: Int, name: String, obj: JsonObject): DirectMessageChannel? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(obj.maybeGetInteger("type") ?: 0) ?: ChannelType.DM
        val recipients = obj.maybeGetJsonArray("recipients")?.mapNotNull { recipientElem ->
            if (!recipientElem.isJsonObject) {
                return@mapNotNull null
            }

            user(recipientElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val lastMessageId = obj.maybeGetSnowflake("last_message_id")
        val lastPinTimestamp = obj.maybeGetString("last_pin_timestamp")?.let(Instant::parse)

        return DirectMessageChannelImpl(
            shardId,
            recipients,
            name,
            lastMessageId,
            lastPinTimestamp,
            id,
            type,
            instance
        )
    }

    override fun guildVoiceChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildVoiceChannel? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(obj.maybeGetInteger("type") ?: 0) ?: ChannelType.GUILD_VOICE
        val userLimit = obj.maybeGetInteger("user_limit") ?: 0
        val status = obj.maybeGetString("status") ?: ""
        val bitrate = obj.maybeGetInteger("bitrate") ?: 0
        val region = obj.maybeGetJsonObject("rtc_region")?.let(::voiceRegion) ?: return null
        val position = obj.maybeGetInteger("position")
        val permissionOverwrites = obj.maybeGetJsonArray("permission_overwrites")?.mapNotNull { permissionOverwriteElem ->
            if (!permissionOverwriteElem.isJsonObject) {
                return@mapNotNull null
            }

            permissionOverwrite(permissionOverwriteElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val topic = obj.maybeGetString("topic") ?: ""
        val nsfw = obj.maybeGetBoolean("nsfw") ?: false
        val rateLimitPerUser = obj.maybeGetInteger("rate_limit_per_user") ?: 0
        val parentId = obj.maybeGetSnowflake("parent_id")
        val parent = if (parentId != null) instance.entityCache.getChannel(parentId)?.asGuildChannel() else null
        val lastMessageId = obj.maybeGetSnowflake("last_message_id")
        val lastPinTimestamp = obj.maybeGetString("last_pin_timestamp")?.let(Instant::parse)

        return GuildVoiceChannelImpl(
            shardId,
            userLimit,
            status,
            bitrate,
            region,
            guild,
            position,
            permissionOverwrites,
            topic,
            nsfw,
            rateLimitPerUser,
            parent,
            id,
            type,
            name,
            lastMessageId,
            lastPinTimestamp,
            instance
        )
    }

    override fun groupDirectMessageChannel(shardId: Int, name: String, obj: JsonObject): GroupDirectMessageChannel? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(obj.maybeGetInteger("type") ?: 0) ?: ChannelType.GROUP_DM
        val recipients = obj.maybeGetJsonArray("recipients")?.mapNotNull { recipientElem ->
            if (!recipientElem.isJsonObject) {
                return@mapNotNull null
            }

            user(recipientElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val lastMessageId = obj.maybeGetSnowflake("last_message_id")
        val lastPinTimestamp = obj.maybeGetString("last_pin_timestamp")?.let(Instant::parse)
        val icon = obj.maybeGetString("icon")
        val ownerId = obj.maybeGetSnowflake("owner_id") ?: return null
        val owner = instance.entityCache.getUser(ownerId) ?: return null

        return GroupDirectMessageChannelImpl(
            shardId,
            icon,
            recipients,
            name,
            lastMessageId,
            lastPinTimestamp,
            id,
            type,
            owner,
            instance
        )
    }

    override fun guildCategoryChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildCategoryChannel? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(obj.maybeGetInteger("type") ?: 0) ?: ChannelType.GUILD_CATEGORY
        val position = obj.maybeGetInteger("position")
        val permissionOverwrites = obj.maybeGetJsonArray("permission_overwrites")?.mapNotNull { permissionOverwriteElem ->
            if (!permissionOverwriteElem.isJsonObject) {
                return@mapNotNull null
            }

            permissionOverwrite(permissionOverwriteElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val topic = obj.maybeGetString("topic") ?: ""
        val nsfw = obj.maybeGetBoolean("nsfw") ?: false
        val rateLimitPerUser = obj.maybeGetInteger("rate_limit_per_user") ?: 0
        val parentId = obj.maybeGetSnowflake("parent_id")
        val parent = if (parentId != null) instance.entityCache.getChannel(parentId)?.asGuildChannel() else null

        return GuildCategoryChannelImpl(
            shardId,
            guild,
            position,
            permissionOverwrites,
            topic,
            nsfw,
            rateLimitPerUser,
            parent,
            id,
            type,
            name,
            instance
        )
    }

    override fun guildAnnouncementChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildAnnouncementChannel? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(obj.maybeGetInteger("type") ?: 0) ?: ChannelType.GUILD_ANNOUNCEMENT
        val position = obj.maybeGetInteger("position")
        val permissionOverwrites = obj.maybeGetJsonArray("permission_overwrites")?.mapNotNull { permissionOverwriteElem ->
            if (!permissionOverwriteElem.isJsonObject) {
                return@mapNotNull null
            }

            permissionOverwrite(permissionOverwriteElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val topic = obj.maybeGetString("topic") ?: ""
        val nsfw = obj.maybeGetBoolean("nsfw") ?: false
        val rateLimitPerUser = obj.maybeGetInteger("rate_limit_per_user") ?: 0
        val parentId = obj.maybeGetSnowflake("parent_id")
        val parent = if (parentId != null) instance.entityCache.getChannel(parentId)?.asGuildChannel() else null
        val lastPinTimestamp = obj.maybeGetString("last_pin_timestamp")?.let(Instant::parse)
        val lastMessageId = obj.maybeGetSnowflake("last_message_id")

        return GuildAnnouncementChannelImpl(
            shardId,
            name,
            lastMessageId,
            lastPinTimestamp,
            id,
            type,
            guild,
            position,
            permissionOverwrites,
            topic,
            nsfw,
            rateLimitPerUser,
            parent,
            instance
        )
    }

    override fun threadChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): ThreadChannel? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(obj.maybeGetInteger("type") ?: 0) ?: ChannelType.GUILD_TEXT
        val position = obj.maybeGetInteger("position")
        val permissionOverwrites = obj.maybeGetJsonArray("permission_overwrites")?.mapNotNull { permissionOverwriteElem ->
            if (!permissionOverwriteElem.isJsonObject) {
                return@mapNotNull null
            }

            permissionOverwrite(permissionOverwriteElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val topic = obj.maybeGetString("topic") ?: ""
        val nsfw = obj.maybeGetBoolean("nsfw") ?: false
        val rateLimitPerUser = obj.maybeGetInteger("rate_limit_per_user") ?: 0
        val parentId = obj.maybeGetSnowflake("parent_id")
        val parent = if (parentId != null) instance.entityCache.getChannel(parentId)?.asGuildChannel() else null
        val lastPinTimestamp = obj.maybeGetString("last_pin_timestamp")?.let(Instant::parse)
        val lastMessageId = obj.maybeGetSnowflake("last_message_id")
        val owner = obj.maybeGetSnowflake("owner_id")?.let { ownerId ->
            instance.entityCache.getUser(ownerId)
        } ?: return null

        return ThreadChannelImpl(
            guild,
            name,
            lastMessageId,
            lastPinTimestamp,
            shardId,
            id,
            type,
            position,
            permissionOverwrites,
            topic,
            nsfw,
            rateLimitPerUser,
            parent,
            owner,
            instance
        )
    }

    override fun welcomeScreenChannel(obj: JsonObject): WelcomeScreenChannel? {
        val channelId = obj.maybeGetSnowflake("channel_id") ?: return null
        val description = obj.maybeGetString("description") ?: return null
        val emojiId = obj.maybeGetString("emoji_id")
        val emojiName = obj.maybeGetString("emoji_name")

        return WelcomeScreenChannel(
            channelId,
            description,
            emojiId,
            emojiName
        )
    }

    override fun welcomeScreen(obj: JsonObject): WelcomeScreen {
        val description = obj.maybeGetString("description")
        val welcomeChannels = obj.maybeGetJsonArray("welcome_channels")?.mapNotNull { welcomeScreenChannelElem ->
            if (!welcomeScreenChannelElem.isJsonObject) {
                return@mapNotNull null
            }

            welcomeScreenChannel(welcomeScreenChannelElem.asJsonObject)
        }?.toSet() ?: emptySet()

        return WelcomeScreen(
            description,
            welcomeChannels
        )
    }

    override fun guild(obj: JsonObject): Guild? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val name = obj.maybeGetString("name") ?: return null
        val icon = obj.maybeGetString("icon")
        val splash = obj.maybeGetString("splash")
        val discoverySplash = obj.maybeGetString("discovery_splash")
        val isOwner = obj.maybeGetBoolean("owner") ?: false
        val ownerId = obj.maybeGetSnowflake("owner_id") ?: return null
        val permissions = Permission.fromBitset(obj.maybeGetInteger("permissions") ?: 0)
        val afkChannelId = obj.maybeGetSnowflake("afk_channel_id")
        val afkTimeout = obj.maybeGetInteger("afk_timeout") ?: 0
        val widgetEnabled = obj.maybeGetBoolean("widget_enabled") ?: false
        val widgetChannelId = obj.maybeGetSnowflake("widget_channel_id")
        val verificationLevel = VerificationLevel.from(obj.maybeGetInteger("verification_level") ?: 0) ?: VerificationLevel.NONE
        val defaultMessageNotifications = DefaultNotificationLevel.from(obj.maybeGetInteger("default_message_notifications") ?: 0) ?: DefaultNotificationLevel.ALL_MESSAGES
        val explicitContentFilter = ExplicitContentFilterLevel.from(obj.maybeGetInteger("explicit_content_filter") ?: 0) ?: ExplicitContentFilterLevel.DISABLED
        val roles = obj.maybeGetJsonArray("roles")?.mapNotNull { roleElem ->
            if (!roleElem.isJsonObject) {
                return@mapNotNull null
            }

            role(roleElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val emojis = obj.maybeGetJsonArray("emojis")?.mapNotNull { emojiElem ->
            if (!emojiElem.isJsonObject) {
                return@mapNotNull null
            }

            emoji(emojiElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val features = obj.get("features")?.asJsonArray?.map { featureElem ->
            featureElem.asString
        }?.toSet() ?: emptySet()
        val mfaLevel = GuildMfaLevel.from(obj.maybeGetInteger("mfa_level") ?: 0) ?: GuildMfaLevel.NONE
        val applicationId = obj.maybeGetSnowflake("application_id")
        val systemChannelId = obj.maybeGetSnowflake("system_channel_id")
        val systemChannelFlags = SystemChannelFlag.fromBitset(obj.maybeGetInteger("system_channel_flags") ?: 0)
        val rulesChannelId = obj.maybeGetSnowflake("rules_channel_id")
        val maxPresences = obj.maybeGetInteger("max_presences")
        val maxMembers = obj.maybeGetInteger("max_members")
        val vanityUrlCode = obj.maybeGetString("vanity_url_code")
        val description = obj.maybeGetString("description")
        val banner = obj.maybeGetString("banner")
        val premiumTier = BoostLevel.from(obj.maybeGetInteger("premium_tier") ?: 0) ?: BoostLevel.NONE
        val premiumSubscriptionCount = obj.maybeGetInteger("premium_subscription_count") ?: 0
        val preferredLocale = Locale.from(obj.maybeGetString("preferred_locale") ?: "en-US") ?: Locale.ENGLISH_US
        val publicUpdatesChannelId = obj.maybeGetSnowflake("public_updates_channel_id")
        val maxVideoChannelUsers = obj.maybeGetInteger("max_video_channel_users")
        val maxStageVideoChannelUsers = obj.maybeGetInteger("max_stage_video_channel_users")
        val approximateMemberCount = obj.maybeGetInteger("approximate_member_count") ?: 0
        val approximatePresenceCount = obj.maybeGetInteger("approximate_presence_count") ?: 0
        val welcomeScreen = obj.maybeGetJsonObject("welcome_screen")?.let(::welcomeScreen)
        val nsfwLevel = NsfwLevel.from(obj.maybeGetInteger("nsfw_level") ?: 0) ?: NsfwLevel.DEFAULT
        val stickers = obj.maybeGetJsonArray("stickers")?.mapNotNull { stickerElem ->
            if (!stickerElem.isJsonObject) {
                return@mapNotNull null
            }

            sticker(stickerElem.asJsonObject)
        } ?: emptyList()
        val premiumProgressBarEnabled = obj.maybeGetBoolean("premium_progress_bar_enabled") ?: false
        val safetyAlertsChannelId = obj.maybeGetSnowflake("safety_alerts_channel_id")

        return Guild(
            id,
            name,
            icon,
            splash,
            discoverySplash,
            isOwner,
            ownerId,
            permissions,
            afkChannelId,
            afkTimeout,
            widgetEnabled,
            widgetChannelId,
            verificationLevel,
            defaultMessageNotifications,
            explicitContentFilter,
            roles,
            emojis,
            features,
            mfaLevel,
            applicationId,
            systemChannelId,
            systemChannelFlags,
            rulesChannelId,
            maxPresences,
            maxMembers,
            vanityUrlCode,
            description,
            banner,
            premiumTier,
            premiumSubscriptionCount,
            preferredLocale,
            publicUpdatesChannelId,
            maxVideoChannelUsers,
            maxStageVideoChannelUsers,
            approximateMemberCount,
            approximatePresenceCount,
            welcomeScreen,
            nsfwLevel,
            stickers,
            premiumProgressBarEnabled,
            safetyAlertsChannelId
        )
    }

    override fun member(user: User?, guild: Guild, obj: JsonObject): Member? {
        val validatedUser = user ?: user(obj.maybeGetJsonObject("user") ?: return null) ?: return null
        val nick = obj.maybeGetString("nick")
        val avatar = obj.maybeGetString("avatar")
        val roles = obj.maybeGetJsonArray("roles")?.mapNotNull { roleElem ->
            if (!roleElem.isJsonPrimitive) {
                return@mapNotNull null
            }

            instance.entityCache.getRole(roleElem.asLong.toSnowflake())
        }?.toSet() ?: emptySet()
        val joinedAt = obj.maybeGetString("joined_at")?.let { timestamp -> Instant.parse(timestamp) } ?: return null
        val premiumSince = obj.maybeGetString("premium_since")?.let { timestamp -> Instant.parse(timestamp) }
        val deaf = obj.maybeGetBoolean("deaf") ?: false
        val mute = obj.maybeGetBoolean("mute") ?: false
        val pending = obj.maybeGetBoolean("pending") ?: false
        val permissions = Permission.fromBitset(obj.maybeGetInteger("permissions") ?: 0)
        val flags = MemberFlag.fromBitset(obj.maybeGetInteger("flags") ?: 0)
        val communicationDisabledUntil = obj.maybeGetString("communication_disabled_until")?.let { timestamp -> Instant.parse(timestamp) }

        return Member(
            guild,
            validatedUser,
            nick,
            avatar,
            roles,
            joinedAt,
            premiumSince,
            deaf,
            mute,
            flags,
            pending,
            permissions,
            communicationDisabledUntil
        )
    }

    override fun message(
        channel: MessageChannel,
        guild: Guild?,
        shardId: Int,
        obj: JsonObject
    ): Message? {
        val id = obj.maybeGetSnowflake("id") ?: return null
        val author = user(obj.maybeGetJsonObject("author") ?: return null) ?: return null
        val content = obj.maybeGetString("content") ?: ""
        val timestamp = obj.maybeGetString("timestamp")?.let { Instant.parse(it) } ?: return null
        val editedTimestamp = obj.maybeGetString("edited_timestamp")?.let { Instant.parse(it) }
        val tts = obj.maybeGetBoolean("tts") ?: false
        val mentionEveryone = obj.maybeGetBoolean("mention_everyone") ?: false
        val mentions = obj.maybeGetJsonArray("mentions")?.mapNotNull { userElem ->
            if (!userElem.isJsonObject) {
                return@mapNotNull null
            }

            user(userElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val mentionRoles = obj.maybeGetJsonArray("mention_roles")?.mapNotNull { roleIdElem ->
            if (!roleIdElem.isJsonPrimitive) {
                return@mapNotNull null
            }

            instance.entityCache.getRole(roleIdElem.asLong.toSnowflake())
        }?.toSet() ?: emptySet()
        val mentionChannels = obj.maybeGetJsonArray("mention_channels")?.mapNotNull { channelIdElem ->
            if (!channelIdElem.isJsonPrimitive) {
                return@mapNotNull null
            }

            instance.entityCache.getChannel(channelIdElem.asLong.toSnowflake())
        }?.toSet() ?: emptySet()
        val embeds = obj.maybeGetJsonArray("embeds")?.mapNotNull { embedElem ->
            if (!embedElem.isJsonObject) {
                return@mapNotNull null
            }

            messageEmbed(embedElem.asJsonObject)
        }?.toSet() ?: emptySet()
        val nonce = obj.maybeGetString("nonce")
        val pinned = obj.maybeGetBoolean("pinned") ?: false
        val webhookId = obj.maybeGetSnowflake("webhook_id")
        val type = MessageType.from(obj.maybeGetInteger("type") ?: 0) ?: MessageType.DEFAULT
        val flags = MessageFlag.fromBitset(obj.maybeGetInteger("flags") ?: 0)
        val referencedMessage = obj.maybeGetJsonObject("referenced_message")?.let { messageObj -> message(channel, guild, shardId, messageObj) }
        val thread = obj.maybeGetJsonObject("thread")?.let { channelObj ->
            channel(guild, shardId, channelObj)
        }?.asThreadChannel()
        val approximatePosition = obj.maybeGetInteger("approximate_position")
        val guildId = obj.maybeGetSnowflake("guild_id")
        val validatedGuild = guild ?: guildId?.let { instance.entityCache.getGuild(it) }
        val member = validatedGuild?.let { nonnullGuild -> obj.maybeGetJsonObject("member")?.let { memberObj -> member(author, nonnullGuild, memberObj) } } // This is awful!

        return Message(
            id,
            channel,
            author,
            content,
            timestamp,
            editedTimestamp,
            tts,
            mentionEveryone,
            mentions,
            mentionRoles,
            mentionChannels,
            embeds,
            nonce,
            pinned,
            webhookId,
            type,
            flags,
            referencedMessage,
            thread,
            approximatePosition,
            validatedGuild,
            member
        )
    }

    override fun message(guild: Guild?, shardId: Int, obj: JsonObject): Message? {
        val channel = obj.maybeGetSnowflake("channel_id")?.let { channelId ->
            instance.entityCache.getChannel(channelId)
        }?.asMessageChannel() ?: return null
        return message(channel, guild ?: channel.asGuildChannel()?.guild, shardId, obj)
    }

    override fun message(shardId: Int, obj: JsonObject): Message? {
        val channel = obj.maybeGetSnowflake("channel_id")?.let { channelId ->
            instance.entityCache.getChannel(channelId)
        }?.asMessageChannel() ?: return null
        val guild = channel.asGuildChannel()?.guild
        return message(channel, guild, shardId, obj)
    }

    override fun messageEmbed(obj: JsonObject): MessageEmbed {
        val title = obj.maybeGetString("title")
        val type = MessageEmbed.MessageEmbedType.from(obj.maybeGetString("type") ?: "rich") ?: MessageEmbed.MessageEmbedType.RICH
        val description = obj.maybeGetString("description")
        val url = obj.maybeGetString("url")
        val timestamp = obj.maybeGetString("timestamp")?.let { Instant.parse(it) }
        val color = obj.maybeGetInteger("color")
        val footer = obj.maybeGetJsonObject("footer")?.let(::messageEmbedFooter)
        val image = obj.maybeGetJsonObject("image")?.let(::messageEmbedImage)
        val thumbnail = obj.maybeGetJsonObject("thumbnail")?.let(::messageEmbedThumbnail)
        val video = obj.maybeGetJsonObject("video")?.let(::messageEmbedVideo)
        val provider = obj.maybeGetJsonObject("provider")?.let(::messageEmbedProvider)
        val author = obj.maybeGetJsonObject("author")?.let(::messageEmbedAuthor)
        val fields = obj.maybeGetJsonArray("fields")?.mapNotNull { fieldElem ->
            if (!fieldElem.isJsonObject) {
                return@mapNotNull null
            }

            messageEmbedField(fieldElem.asJsonObject)
        }?.toSet() ?: emptySet()

        return MessageEmbed(
            title,
            type,
            description,
            url,
            timestamp,
            color,
            footer,
            image,
            thumbnail,
            video,
            provider,
            author,
            fields
        )
    }

    override fun messageEmbedFooter(obj: JsonObject): MessageEmbed.MessageEmbedFooter? {
        val text = obj.maybeGetString("text") ?: return null
        val iconUrl = obj.maybeGetString("icon_url")
        val proxyIconUrl = obj.maybeGetString("proxy_icon_url")

        return MessageEmbed.MessageEmbedFooter(
            text,
            iconUrl,
            proxyIconUrl
        )
    }

    override fun messageEmbedImage(obj: JsonObject): MessageEmbed.MessageEmbedImage? {
        val url = obj.maybeGetString("url") ?: return null
        val proxyUrl = obj.maybeGetString("proxy_url")
        val height = obj.maybeGetInteger("height")
        val width = obj.maybeGetInteger("width")

        return MessageEmbed.MessageEmbedImage(
            url,
            proxyUrl,
            height,
            width
        )
    }

    override fun messageEmbedThumbnail(obj: JsonObject): MessageEmbed.MessageEmbedThumbnail? {
        val url = obj.maybeGetString("url") ?: return null
        val proxyUrl = obj.maybeGetString("proxy_url")
        val height = obj.maybeGetInteger("height")
        val width = obj.maybeGetInteger("width")

        return MessageEmbed.MessageEmbedThumbnail(
            url,
            proxyUrl,
            height,
            width
        )
    }

    override fun messageEmbedVideo(obj: JsonObject): MessageEmbed.MessageEmbedVideo? {
        val url = obj.maybeGetString("url") ?: return null
        val height = obj.maybeGetInteger("height")
        val width = obj.maybeGetInteger("width")

        return MessageEmbed.MessageEmbedVideo(
            url,
            height,
            width
        )
    }

    override fun messageEmbedProvider(obj: JsonObject): MessageEmbed.MessageEmbedProvider? {
        val name = obj.maybeGetString("name") ?: return null
        val url = obj.maybeGetString("url")

        return MessageEmbed.MessageEmbedProvider(
            name,
            url
        )
    }

    override fun messageEmbedAuthor(obj: JsonObject): MessageEmbed.MessageEmbedAuthor? {
        val name = obj.maybeGetString("name") ?: return null
        val url = obj.maybeGetString("url")
        val iconUrl = obj.maybeGetString("icon_url")
        val proxyIconUrl = obj.maybeGetString("proxy_icon_url")

        return MessageEmbed.MessageEmbedAuthor(
            name,
            url,
            iconUrl,
            proxyIconUrl
        )
    }

    override fun messageEmbedField(obj: JsonObject): MessageEmbed.MessageEmbedField? {
        val name = obj.maybeGetString("name") ?: return null
        val value = obj.maybeGetString("value") ?: return null
        val inline = obj.maybeGetBoolean("inline") ?: false

        return MessageEmbed.MessageEmbedField(
            name,
            value,
            inline
        )
    }

}
