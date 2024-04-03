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

package dev.deftu.disko.entities

import com.google.gson.JsonObject
import dev.deftu.disko.Disko
import dev.deftu.disko.entities.channel.*
import dev.deftu.disko.entities.channel.VoiceRegion
import dev.deftu.disko.entities.channel.impl.*
import dev.deftu.disko.entities.guild.*
import dev.deftu.disko.utils.*
import java.time.Instant

public class DefaultEntityConstructor(
    private val disko: Disko
) : EntityConstructor {
    override fun constructUser(json: JsonObject): User? {
        val id = json.maybeGetSnowflake("id") ?: return null
        val username = json.maybeGetString("username") ?: return null
        val discriminator = json.maybeGetString("discriminator") ?: return null
        val avatar = json.maybeGetString("avatar")
        val bot = json.maybeGetBoolean("bot") ?: false
        val system = json.maybeGetBoolean("system") ?: false
        val mfaEnabled = json.maybeGetBoolean("mfa_enabled") ?: false
        val locale = Locale.from(json.maybeGetString("locale") ?: "en-US") ?: Locale.ENGLISH_US
        val flags = UserFlag.from(json.maybeGetInteger("flags") ?: 0)

        return User(
            id,
            username,
            discriminator,
            avatar,
            bot,
            system,
            mfaEnabled,
            locale,
            flags
        )
    }

    override fun constructSelfUser(json: JsonObject): SelfUser? {
        val id = json.maybeGetSnowflake("id") ?: return null
        val username = json.maybeGetString("username") ?: return null
        val discriminator = json.maybeGetString("discriminator") ?: return null
        val avatar = json.maybeGetString("avatar")
        val mfaEnabled = json.maybeGetBoolean("mfa_enabled") ?: false
        val locale = Locale.from(json.maybeGetString("locale") ?: "en-US") ?: Locale.ENGLISH_US
        val flags = UserFlag.from(json.maybeGetInteger("flags") ?: 0)

        return SelfUser(
            id,
            username,
            discriminator,
            avatar,
            mfaEnabled,
            locale,
            flags
        )
    }

    override fun constructMember(json: JsonObject): Member? {
        val user = constructUser(json.maybeGetJsonObject("user") ?: return null) ?: return null
        val nick = json.maybeGetString("nick")
        val avatar = json.maybeGetString("avatar")
        //val roles = json.get("roles")?.asJsonArray?.map { it.asString } ?: emptyList()
        val joinedAt = json.maybeGetString("joined_at")?.let { Instant.parse(it) } ?: return null
        val premiumSince = json.maybeGetString("premium_since")?.let { Instant.parse(it) }
        val deaf = json.maybeGetBoolean("deaf") ?: false
        val mute = json.maybeGetBoolean("mute") ?: false
        val pending = json.maybeGetBoolean("pending") ?: false
        val permissions = Permission.from(json.maybeGetInteger("permissions") ?: 0)
        val flags = MemberFlag.from(json.maybeGetInteger("flags") ?: 0)
        val communicationDisabledUntil = json.maybeGetString("communication_disabled_until")?.let { Instant.parse(it) }

        return Member(
            user,
            nick,
            avatar,
            //roles,
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

    override fun constructGuild(json: JsonObject): Guild? {
        val id = json.maybeGetSnowflake("id") ?: return null
        val name = json.maybeGetString("name") ?: return null
        val icon = json.maybeGetString("icon")
        val splash = json.maybeGetString("splash")
        val discoverySplash = json.maybeGetString("discovery_splash")
        val isOwner = json.maybeGetBoolean("owner") ?: false
        val ownerId = json.maybeGetSnowflake("owner_id") ?: return null
        val permissions = Permission.from(json.maybeGetInteger("permissions") ?: 0)
        val afkChannelId = json.maybeGetSnowflake("afk_channel_id")
        val afkTimeout = json.maybeGetInteger("afk_timeout") ?: 0
        val widgetEnabled = json.maybeGetBoolean("widget_enabled") ?: false
        val widgetChannelId = json.maybeGetSnowflake("widget_channel_id")
        val verificationLevel = VerificationLevel.from(json.maybeGetInteger("verification_level") ?: 0) ?: VerificationLevel.NONE
        val defaultMessageNotifications = DefaultNotificationLevel.from(json.maybeGetInteger("default_message_notifications") ?: 0) ?: DefaultNotificationLevel.ALL_MESSAGES
        val explicitContentFilter = ExplicitContentFilterLevel.from(json.maybeGetInteger("explicit_content_filter") ?: 0) ?: ExplicitContentFilterLevel.DISABLED
        val features = json.get("features")?.asJsonArray?.map { it.asString } ?: emptyList()
        val mfaLevel = GuildMfaLevel.from(json.maybeGetInteger("mfa_level") ?: 0) ?: GuildMfaLevel.NONE
        val applicationId = json.maybeGetSnowflake("application_id")
        val systemChannelId = json.maybeGetSnowflake("system_channel_id")
        val systemChannelFlags = SystemChannelFlag.from(json.maybeGetInteger("system_channel_flags") ?: 0)
        val rulesChannelId = json.maybeGetSnowflake("rules_channel_id")
        val maxPresences = json.maybeGetInteger("max_presences")
        val maxMembers = json.maybeGetInteger("max_members")
        val vanityUrlCode = json.maybeGetString("vanity_url_code")
        val description = json.maybeGetString("description")
        val banner = json.maybeGetString("banner")
        val premiumTier = BoostLevel.from(json.maybeGetInteger("premium_tier") ?: 0) ?: BoostLevel.NONE
        val premiumSubscriptionCount = json.maybeGetInteger("premium_subscription_count") ?: 0
        val preferredLocale = Locale.from(json.maybeGetString("preferred_locale") ?: "en-US") ?: Locale.ENGLISH_US
        val publicUpdatesChannelId = json.maybeGetSnowflake("public_updates_channel_id")
        val maxVideoChannelUsers = json.maybeGetInteger("max_video_channel_users")
        val maxStageVideoChannelUsers = json.maybeGetInteger("max_stage_video_channel_users")
        val approximateMemberCount = json.maybeGetInteger("approximate_member_count") ?: 0
        val approximatePresenceCount = json.maybeGetInteger("approximate_presence_count") ?: 0
        val welcomeScreen = json.maybeGetJsonObject("welcome_screen")?.let { constructGuildWelcomeScreen(it) }
        val nsfwLevel = NsfwLevel.from(json.maybeGetInteger("nsfw_level") ?: 0) ?: NsfwLevel.DEFAULT
        // TODO - stageInstances
        // TODO - stickers
        val premiumProgressBarEnabled = json.maybeGetBoolean("premium_progress_bar_enabled") ?: false
        val safetyAlertsChannelId = json.maybeGetSnowflake("safety_alerts_channel_id")

        return Guild(
            disko,
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
            premiumProgressBarEnabled,
            safetyAlertsChannelId
        )
    }

    override fun constructGuildWelcomeScreen(json: JsonObject): WelcomeScreen {
        val description = json.maybeGetString("description")
        val welcomeChannels = json.get("welcome_channels")?.asJsonArray?.mapNotNull { constructGuildWelcomeScreenChannel(it.asJsonObject) } ?: emptyList()

        return WelcomeScreen(
            description,
            welcomeChannels
        )
    }

    override fun constructGuildWelcomeScreenChannel(json: JsonObject): WelcomeScreenChannel? {
        val channelId = json.maybeGetSnowflake("channel_id") ?: return null
        val description = json.maybeGetString("description") ?: return null
        val emojiId = json.maybeGetString("emoji_id")
        val emojiName = json.maybeGetString("emoji_name")

        return WelcomeScreenChannel(
            channelId,
            description,
            emojiId,
            emojiName
        )
    }

    override fun constructPermissionOverwrite(json: JsonObject): PermissionOverwrite? {
        val id = json.maybeGetSnowflake("id") ?: return null
        val type = PermissionOverwriteType.from(json.maybeGetString("type")) ?: return null
        val allow = Permission.from(json.maybeGetInteger("allow") ?: 0)
        val deny = Permission.from(json.maybeGetInteger("deny") ?: 0)

        return when (type) {
            PermissionOverwriteType.ROLE -> RolePermissionOverwrite(disko, id, allow, deny)
            PermissionOverwriteType.MEMBER -> MemberPermissionOverwrite(disko, id, allow, deny)
            else -> null
        }
    }

    override fun constructVoiceRegion(json: JsonObject): VoiceRegion? {
        val id = json.maybeGetString("id") ?: return null
        val name = json.maybeGetString("name") ?: return null
        val optimal = json.maybeGetBoolean("optimal") ?: false
        val deprecated = json.maybeGetBoolean("deprecated") ?: false
        val custom = json.maybeGetBoolean("custom") ?: false

        return VoiceRegion(
            id,
            name,
            optimal,
            deprecated,
            custom
        )
    }

    override fun constructChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): Channel? {
        val type = ChannelType.from(json.maybeGetInteger("type") ?: 0) ?: return null

        return when (type) {
            ChannelType.GUILD_TEXT -> constructGuildMessageChannel(shardId, guild, json)
            ChannelType.DM -> constructDirectMessageChannel(shardId, json)
            ChannelType.GUILD_VOICE -> constructGuildVoiceChannel(shardId, guild, json)
            ChannelType.GROUP_DM -> constructGroupDirectMessageChannel(shardId, json)
            ChannelType.GUILD_CATEGORY -> constructGuildCategoryChannel(shardId, guild, json)
            ChannelType.GUILD_ANNOUNCEMENT -> constructGuildAnnouncementChannel(shardId, guild, json)
            else -> null
        }
    }

    override fun constructGuildMessageChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): GuildMessageChannel? {
        guild ?: return null

        val id = json.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(json.maybeGetInteger("type") ?: 0) ?: ChannelType.GUILD_TEXT
        val position = json.maybeGetInteger("position")
        val permissionOverwrites = json.get("permission_overwrites")?.asJsonArray?.mapNotNull { constructPermissionOverwrite(it.asJsonObject) } ?: emptyList()
        val topic = json.maybeGetString("topic") ?: ""
        val nsfw = json.maybeGetBoolean("nsfw") ?: false
        val rateLimitPerUser = json.maybeGetInteger("rate_limit_per_user") ?: 0
        val parentId = json.maybeGetSnowflake("parent_id")
        val parent = if (parentId != null) disko.channelCache.getGuildChannel(parentId) else null
        val lastPinTimestamp = json.maybeGetString("last_pin_timestamp")?.let { Instant.parse(it) }
        val name = json.maybeGetString("name") ?: return null
        val lastMessageId = json.maybeGetSnowflake("last_message_id")

        return GuildMessageChannel(
            disko,
            shardId,
            id,
            type,
            guild,
            position,
            permissionOverwrites,
            topic,
            nsfw,
            rateLimitPerUser,
            parent,
            name,
            lastMessageId,
            lastPinTimestamp
        )
    }

    override fun constructDirectMessageChannel(
        shardId: Int,
        json: JsonObject
    ): DirectMessageChannel? {
        val id = json.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(json.maybeGetInteger("type") ?: 0) ?: ChannelType.DM
        val name = json.maybeGetString("name") ?: return null
        val lastMessageId = json.maybeGetSnowflake("last_message_id")
        val recipients = json.get("recipients")?.asJsonArray?.mapNotNull { constructUser(it.asJsonObject) } ?: emptyList()
        val lastPinTimestamp = json.maybeGetString("last_pin_timestamp")?.let { Instant.parse(it) }

        return DirectMessageChannel(
            disko,
            shardId,
            id,
            type,
            name,
            lastMessageId,
            lastPinTimestamp,
            recipients
        )
    }

    override fun constructGuildVoiceChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): GuildVoiceChannel? {
        guild ?: return null

        val id = json.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(json.maybeGetInteger("type") ?: 0) ?: ChannelType.GUILD_VOICE
        val position = json.maybeGetInteger("position")
        val permissionOverwrites = json.get("permission_overwrites")?.asJsonArray?.mapNotNull { constructPermissionOverwrite(it.asJsonObject) } ?: emptyList()
        val topic = json.maybeGetString("topic") ?: ""
        val nsfw = json.maybeGetBoolean("nsfw") ?: false
        val rateLimitPerUser = json.maybeGetInteger("rate_limit_per_user") ?: 0
        val parentId = json.maybeGetSnowflake("parent_id")
        val parent = if (parentId != null) disko.channelCache.getGuildChannel(parentId) else null
        val name = json.maybeGetString("name") ?: return null
        val lastMessageId = json.maybeGetSnowflake("last_message_id")
        val lastPinTimestamp = json.maybeGetString("last_pin_timestamp")?.let { Instant.parse(it) }
        val userLimit = json.maybeGetInteger("user_limit") ?: 0
        val status = json.maybeGetString("status") ?: ""
        val region = json.maybeGetString("rtc_region")?.let { disko.voiceRegions.getRegionById(it) }
        val bitrate = json.maybeGetInteger("bitrate") ?: 0

        return GuildVoiceChannel(
            disko,
            shardId,
            id,
            type,
            guild,
            position,
            permissionOverwrites,
            topic,
            nsfw,
            rateLimitPerUser,
            parent,
            name,
            lastMessageId,
            lastPinTimestamp,
            userLimit,
            status,
            region,
            bitrate
        )
    }

    override fun constructGroupDirectMessageChannel(
        shardId: Int,
        json: JsonObject
    ): GroupDirectMessageChannel? {
        val id = json.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(json.maybeGetInteger("type") ?: 0) ?: ChannelType.GROUP_DM
        val name = json.maybeGetString("name") ?: return null
        val lastMessageId = json.maybeGetSnowflake("last_message_id")
        val lastPinTimestamp = json.maybeGetString("last_pin_timestamp")?.let { Instant.parse(it) }
        val recipients = json.get("recipients")?.asJsonArray?.mapNotNull { constructUser(it.asJsonObject) } ?: emptyList()
        val icon = json.maybeGetString("icon")
        val ownerId = json.maybeGetSnowflake("owner_id") ?: return null

        return GroupDirectMessageChannel(
            disko,
            shardId,
            id,
            type,
            name,
            lastMessageId,
            lastPinTimestamp,
            recipients,
            icon,
            ownerId
        )
    }

    override fun constructGuildCategoryChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): GuildCategoryChannel? {
        guild ?: return null

        val id = json.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(json.maybeGetInteger("type") ?: 0) ?: ChannelType.GUILD_CATEGORY
        val position = json.maybeGetInteger("position")
        val permissionOverwrites = json.get("permission_overwrites")?.asJsonArray?.mapNotNull { constructPermissionOverwrite(it.asJsonObject) } ?: emptyList()
        val name = json.maybeGetString("name") ?: return null
        val nsfw = json.maybeGetBoolean("nsfw") ?: false
        val parentId = json.maybeGetSnowflake("parent_id")
        val parent = if (parentId != null) disko.channelCache.getGuildChannel(parentId) else null

        return GuildCategoryChannel(
            disko,
            shardId,
            id,
            type,
            guild,
            position,
            permissionOverwrites,
            "",
            nsfw,
            0,
            parent,
            name
        )
    }

    override fun constructGuildAnnouncementChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): GuildAnnouncementChannel? {
        guild ?: return null

        val id = json.maybeGetSnowflake("id") ?: return null
        val type = ChannelType.from(json.maybeGetInteger("type") ?: 0) ?: ChannelType.GUILD_ANNOUNCEMENT
        val position = json.maybeGetInteger("position")
        val permissionOverwrites = json.get("permission_overwrites")?.asJsonArray?.mapNotNull { constructPermissionOverwrite(it.asJsonObject) } ?: emptyList()
        val topic = json.maybeGetString("topic") ?: ""
        val nsfw = json.maybeGetBoolean("nsfw") ?: false
        val rateLimitPerUser = json.maybeGetInteger("rate_limit_per_user") ?: 0
        val parentId = json.maybeGetSnowflake("parent_id")
        val parent = if (parentId != null) disko.channelCache.getGuildChannel(parentId) else null
        val lastPinTimestamp = json.maybeGetString("last_pin_timestamp")?.let { Instant.parse(it) }
        val name = json.maybeGetString("name") ?: return null
        val lastMessageId = json.maybeGetSnowflake("last_message_id")

        return GuildAnnouncementChannel(
            disko,
            shardId,
            id,
            type,
            guild,
            position,
            permissionOverwrites,
            topic,
            nsfw,
            rateLimitPerUser,
            parent,
            name,
            lastMessageId,
            lastPinTimestamp
        )
    }
}
