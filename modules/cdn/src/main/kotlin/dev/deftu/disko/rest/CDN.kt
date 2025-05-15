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

package dev.deftu.disko.rest

import dev.deftu.disko.api.utils.Snowflake

public object CDN {

    public const val BASE_URL: String = "https://cdn.discordapp.com"

    public fun emoji(id: Snowflake): CdnUrl {
        return CdnUrl("$BASE_URL/emojis/$id")
    }

    public fun guildIcon(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/icons/$id/$hash")
    }

    public fun guildSplash(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/splashes/$id/$hash")
    }

    public fun guildDiscoverySplash(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/discovery-splashes/$id/$hash")
    }

    public fun guildBanner(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/banners/$id/$hash")
    }

    public fun userBanner(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/banners/$id/$hash")
    }

    public fun defaultUserAvatar(id: Snowflake): CdnUrl {
        return CdnUrl("$BASE_URL/embed/avatars/${(id.value shr 22) % 6}")
    }

    public fun userAvatar(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/avatars/$id/$hash")
    }

    public fun guildMemberAvatar(guildId: Snowflake, userId: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/guilds/$guildId/users/$userId/avatars/$hash")
    }

    public fun avatarDecoration(hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/avatar-decoration-presets/$hash")
    }

    public fun applicationIcon(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/app-icons/$id/$hash")
    }

    public fun applicationCover(id: Snowflake, hash: String): CdnUrl {
        return applicationIcon(id, hash)
    }

    public fun applicationAsset(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/app-assets/$id/$hash")
    }

    public fun achievementIcon(applicationId: Snowflake, achievementId: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/app-assets/$applicationId/achievements/$achievementId/icons/$hash")
    }

    public fun storePageAsset(applicationId: Snowflake, assetId: Snowflake): CdnUrl {
        return CdnUrl("$BASE_URL/app-assets/$applicationId/store/$assetId")
    }

    public fun stickerPackBanner(id: Snowflake): CdnUrl {
        return CdnUrl("$BASE_URL/app-assets/710982414301790216/store/$id")
    }

    public fun teamIcon(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/team-icons/$id/$hash")
    }

    // TODO
    // public fun sticker(id: Snowflake): CdnUrl {
    //     return CdnUrl("$BASE_URL/stickers/$id")
    // }

    public fun roleIcon(id: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/role-icons/$id/$hash")
    }

    public fun guildScheduledEventCover(scheduledEventId: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/guild-events/$scheduledEventId/$hash")
    }

    public fun guildMemberBanner(guildId: Snowflake, userId: Snowflake, hash: String): CdnUrl {
        return CdnUrl("$BASE_URL/guilds/$guildId/users/$userId/banners/$hash")
    }

}
