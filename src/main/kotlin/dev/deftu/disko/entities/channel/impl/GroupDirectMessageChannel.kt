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
import dev.deftu.disko.entities.ImageFormat
import dev.deftu.disko.entities.User
import dev.deftu.disko.entities.channel.ChannelType
import dev.deftu.disko.entities.channel.impl.DirectMessageChannel
import dev.deftu.disko.utils.Snowflake
import java.time.Instant

public class GroupDirectMessageChannel(
    disko: Disko,
    shardId: Int,
    id: Snowflake,
    type: ChannelType,
    name: String,
    lastMessageId: Snowflake?,
    lastPinTimestamp: Instant?,
    recipients: List<User>,
    public val icon: String?,
    private val ownerId: Snowflake
) : DirectMessageChannel(
    disko,
    shardId,
    id,
    type,
    name,
    lastMessageId,
    lastPinTimestamp,
    recipients
) {
    public val owner: User
        get() = disko.userCache.getUser(ownerId)!!

    public fun getIconUrl(): String? =
        getIconUrl(ImageFormat.PNG)
    public fun getIconUrl(format: ImageFormat): String? =
        "https://cdn.discordapp.com/channel-icons/${id}/${icon}.${format.extension}"
    public fun getIconUrl(format: ImageFormat, size: Int): String? =
        "https://cdn.discordapp.com/channel-icons/${id}/${icon}.${format.extension}?size=${size}"
}
