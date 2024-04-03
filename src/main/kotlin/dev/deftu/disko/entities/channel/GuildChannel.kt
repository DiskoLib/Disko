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

package dev.deftu.disko.entities.channel

import dev.deftu.disko.entities.guild.Guild
import java.time.Instant

public interface GuildChannel : Channel {
    public val guild: Guild
    public val position: Int?
    public val permissionOverwrites: List<PermissionOverwrite>
    public val topic: String
    public val nsfw: Boolean
    public val rateLimitPerUser: Int
    public val parent: GuildChannel?

    public fun getChannelUrl(): String =
        "https://discord.com/channels/${guild.id}/${id}"
}
