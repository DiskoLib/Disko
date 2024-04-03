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

package dev.deftu.disko.entities.channel.impl

import dev.deftu.disko.Disko
import dev.deftu.disko.entities.channel.ChannelType
import dev.deftu.disko.entities.channel.GuildChannel
import dev.deftu.disko.entities.channel.PermissionOverwrite
import dev.deftu.disko.entities.guild.Guild
import dev.deftu.disko.entities.message.Message
import dev.deftu.disko.utils.Snowflake
import java.time.Instant

public class GuildAnnouncementChannel(
    disko: Disko,
    shardId: Int,
    id: Snowflake,
    type: ChannelType,
    guild: Guild,
    position: Int?,
    permissionOverwrites: List<PermissionOverwrite>,
    topic: String,
    nsfw: Boolean,
    rateLimitPerUser: Int,
    parent: GuildChannel?,
    name: String,
    lastMessageId: Snowflake?,
    lastPinTimestamp: Instant?
) : GuildMessageChannel(
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
) {
    public fun crosspost(message: Message) {
        // TODO
    }
}
