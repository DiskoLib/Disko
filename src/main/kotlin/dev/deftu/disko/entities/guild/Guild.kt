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

package dev.deftu.disko.entities.guild

import dev.deftu.disko.entities.ImageFormat
import dev.deftu.disko.entities.Locale
import dev.deftu.disko.entities.Permission
import dev.deftu.disko.utils.Snowflake
import java.util.Optional

public class Guild(
    public val id: Snowflake,
    public val name: String,
    public val icon: String?,
    public val splash: String?,
    public val discoverySplash: String?,
    public val owner: Boolean,
    public val ownerId: Snowflake,
    public val permissions: List<Permission>,
    // TODO - public val afkChannel: Channel
    public val afkTimeout: Int,
    public val widgetEnabled: Boolean,
    // TODO - public val widgetChannel: Channel
    public val verificationLevel: VerificationLevel,
    public val defaultMessageNotifications: DefaultNotificationLevel,
    public val explicitContentFilter: ExplicitContentFilterLevel,
    // TODO - public val roles: List<Role>
    // TODO - public val emojis: List<Emoji>
    public val features: List<String>,
    public val mfaLevel: GuildMfaLevel,
    /**
     * The application ID of the guild creator if it is bot-created.
     */
    public val applicationId: Snowflake?,
    // TODO - public val systemChannel: Channel
    public val systemChannelFlags: List<SystemChannelFlag>,
    // TODO - public val rulesChannel: Channel
    public val maxPresences: Int?,
    public val maxMembers: Int?,
    public val vanityUrlCode: String?,
    public val description: String?,
    public val banner: String?,
    public val premiumTier: BoostLevel,
    public val premiumSubscriptionCount: Int,
    public val preferredLocale: Locale,
    // TODO - public val publicUpdatesChannel: Channel
    public val maxVideoChannelUsers: Int?,
    public val maxStageVideoChannelUsers: Int?,
    public val approximateMemberCount: Int,
    public val approximatePresenceCount: Int,
    // TODO - public val welcomeScreen: WelcomeScreen
    public val nsfwLevel: NsfwLevel,
    // TODO - public val stageInstances: List<StageInstance>
    // TODO - public val stickers: List<GuildSticker>
    public val premiumProgressBarEnabled: Boolean,
    // TODO - public val safetyAlertsChannel: Channel
) {
    public fun getIconUrl(): String? =
        getIconUrl(ImageFormat.PNG)
    public fun getIconUrl(format: ImageFormat): String? =
        icon?.let { "https://cdn.discordapp.com/icons/$id/$it.${format.extension}" }
    public fun getIconUrl(format: ImageFormat, size: Int): String? =
        icon?.let { "https://cdn.discordapp.com/icons/$id/$it.${format.extension}?size=${size.coerceIn(16, 4096)}" }

    public fun getSplashUrl(): String? =
        getSplashUrl(ImageFormat.PNG)
    public fun getSplashUrl(format: ImageFormat): String? =
        splash?.let { "https://cdn.discordapp.com/splashes/$id/$it.${format.extension}" }
    public fun getSplashUrl(format: ImageFormat, size: Int): String? =
        splash?.let { "https://cdn.discordapp.com/splashes/$id/$it.${format.extension}?size=${size.coerceIn(16, 4096)}" }

    public fun getDiscoverySplashUrl(): String? =
        getDiscoverySplashUrl(ImageFormat.PNG)
    public fun getDiscoverySplashUrl(format: ImageFormat): String? =
        discoverySplash?.let { "https://cdn.discordapp.com/discovery-splashes/$id/$it.${format.extension}" }
    public fun getDiscoverySplashUrl(format: ImageFormat, size: Int): String? =
        discoverySplash?.let { "https://cdn.discordapp.com/discovery-splashes/$id/$it.${format.extension}?size=${size.coerceIn(16, 4096)}" }

    public fun getBannerUrl(): String? =
        getBannerUrl(ImageFormat.PNG)
    public fun getBannerUrl(format: ImageFormat): String? =
        banner?.let { "https://cdn.discordapp.com/banners/$id/$it.${format.extension}" }
    public fun getBannerUrl(format: ImageFormat, size: Int): String? =
        banner?.let { "https://cdn.discordapp.com/banners/$id/$it.${format.extension}?size=${size.coerceIn(16, 4096)}" }
}
