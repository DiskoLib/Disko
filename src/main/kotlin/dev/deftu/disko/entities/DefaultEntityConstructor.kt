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
import dev.deftu.disko.entities.guild.*
import dev.deftu.disko.utils.Snowflake

public object DefaultEntityConstructor : EntityConstructor {
    override fun constructSelfUser(json: JsonObject): SelfUser? {
        val rawId = json.get("id")?.asLong ?: return null
        val id = Snowflake(rawId)
        val username = json.get("username")?.asString ?: return null
        val discriminator = json.get("discriminator")?.asString ?: "0"
        val rawAvatar = json.get("avatar")
        val avatar = if (rawAvatar != null && !rawAvatar.isJsonNull) rawAvatar.asString else null
        val mfaEnabled = json.get("mfa_enabled")?.asBoolean ?: false
        val locale = json.get("locale")?.asString ?: "en-US"
        val rawFlags = json.get("flags")?.asInt ?: 0
        val flags = UserFlag.from(rawFlags)

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

    override fun constructGuild(json: JsonObject): Guild? {
        val rawId = json.get("id")?.asLong ?: return null
        val id = Snowflake(rawId)
        val name = json.get("name")?.asString ?: return null
        val rawIcon = json.get("icon")
        val icon = if (rawIcon != null && !rawIcon.isJsonNull) rawIcon.asString else null
        val rawSplash = json.get("splash")
        val splash = if (rawSplash != null && !rawSplash.isJsonNull) rawSplash.asString else null
        val rawDiscoverySplash = json.get("discovery_splash")
        val discoverySplash = if (rawDiscoverySplash != null && !rawDiscoverySplash.isJsonNull) rawDiscoverySplash.asString else null
        val owner = json.get("owner")?.asBoolean ?: false
        val rawOwnerId = json.get("owner_id")?.asLong ?: return null
        val ownerId = Snowflake(rawOwnerId)
        val rawPermissions = json.get("permissions")?.asInt ?: 0
        val permissions = Permission.from(rawPermissions)
        // TODO - afkChannel
        val afkTimeout = json.get("afk_timeout")?.asInt ?: 0
        val widgetEnabled = json.get("widget_enabled")?.asBoolean ?: false
        // TODO - widgetChannel
        val rawVerificationLevel = json.get("verification_level")?.asInt ?: 0
        val verificationLevel = VerificationLevel.from(rawVerificationLevel) ?: VerificationLevel.NONE
        val rawDefaultMessageNotifications = json.get("default_message_notifications")?.asInt ?: 0
        val defaultMessageNotifications = DefaultNotificationLevel.from(rawDefaultMessageNotifications) ?: DefaultNotificationLevel.ALL_MESSAGES
        val rawExplicitContentFilter = json.get("explicit_content_filter")?.asInt ?: 0
        val explicitContentFilter = ExplicitContentFilterLevel.from(rawExplicitContentFilter) ?: ExplicitContentFilterLevel.DISABLED
        val features = json.get("features")?.asJsonArray?.map { it.asString } ?: emptyList()
        val rawMfaLevel = json.get("mfa_level")?.asInt ?: 0
        val mfaLevel = GuildMfaLevel.from(rawMfaLevel) ?: GuildMfaLevel.NONE
        val jsonApplicationId = json.get("application_id")
        val rawApplicationId = if (jsonApplicationId != null && !jsonApplicationId.isJsonNull) jsonApplicationId.asLong else null
        val applicationId = if (rawApplicationId != null) Snowflake(rawApplicationId) else null
        // TODO - systemChannel
        val rawSystemChannelFlags = json.get("system_channel_flags")?.asInt ?: 0
        val systemChannelFlags = SystemChannelFlag.from(rawSystemChannelFlags)
        // TODO - rulesChannel
        val maxPresences = json.get("max_presences")?.asInt
        val maxMembers = json.get("max_members")?.asInt
        val rawVanityUrlCode = json.get("vanity_url_code")
        val vanityUrlCode = if (rawVanityUrlCode != null && !rawVanityUrlCode.isJsonNull) rawVanityUrlCode.asString else null
        val rawDescription = json.get("description")
        val description = if (rawDescription != null && !rawDescription.isJsonNull) rawDescription.asString else null
        val rawBanner = json.get("banner")
        val banner = if (rawBanner != null && !rawBanner.isJsonNull) rawBanner.asString else null
        val rawPremiumTier = json.get("premium_tier")?.asInt ?: 0
        val premiumTier = BoostLevel.from(rawPremiumTier) ?: BoostLevel.NONE
        val premiumSubscriptionCount = json.get("premium_subscription_count")?.asInt ?: 0
        val rawPreferredLocale = json.get("preferred_locale")?.asString ?: "en-US"
        val preferredLocale = Locale.from(rawPreferredLocale) ?: Locale.ENGLISH_US
        // TODO - publicUpdatesChannel
        val maxVideoChannelUsers = json.get("max_video_channel_users")?.asInt
        val maxStageVideoChannelUsers = json.get("max_stage_video_channel_users")?.asInt
        val approximateMemberCount = json.get("approximate_member_count")?.asInt ?: 0
        val approximatePresenceCount = json.get("approximate_presence_count")?.asInt ?: 0
        // TODO - welcomeScreen
        val rawNsfwLevel = json.get("nsfw_level")?.asInt ?: 0
        val nsfwLevel = NsfwLevel.from(rawNsfwLevel) ?: NsfwLevel.DEFAULT
        // TODO - stageInstances
        // TODO - stickers
        val premiumProgressBarEnabled = json.get("premium_progress_bar_enabled")?.asBoolean ?: false

        return Guild(
            id,
            name,
            icon,
            splash,
            discoverySplash,
            owner,
            ownerId,
            permissions,
            afkTimeout,
            widgetEnabled,
            verificationLevel,
            defaultMessageNotifications,
            explicitContentFilter,
            features,
            mfaLevel,
            applicationId,
            systemChannelFlags,
            maxPresences,
            maxMembers,
            vanityUrlCode,
            description,
            banner,
            premiumTier,
            premiumSubscriptionCount,
            preferredLocale,
            maxVideoChannelUsers,
            maxStageVideoChannelUsers,
            approximateMemberCount,
            approximatePresenceCount,
            nsfwLevel,
            premiumProgressBarEnabled
        )
    }
}
