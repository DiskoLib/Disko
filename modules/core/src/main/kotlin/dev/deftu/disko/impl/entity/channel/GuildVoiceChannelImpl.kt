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

package dev.deftu.disko.impl.entity.channel

import dev.deftu.disko.api.Disko
import dev.deftu.disko.api.entity.VoiceRegion
import dev.deftu.disko.api.entity.channel.*
import dev.deftu.disko.api.entity.guild.Guild
import dev.deftu.disko.api.entity.message.Message
import dev.deftu.disko.api.entity.user.User
import dev.deftu.disko.api.utils.GuildExtensions.getMember
import dev.deftu.disko.api.utils.Snowflake
import java.time.Instant

public class GuildVoiceChannelImpl(
    override val shardId: Int,
    override val userLimit: Int,
    override val status: String,
    override val bitrate: Int,
    override val region: VoiceRegion,
    override val guild: Guild,
    override val position: Int?,
    override val permissionOverwrites: Set<PermissionOverwrite>,
    override val topic: String,
    override val isNsfw: Boolean,
    override val rateLimitPerUser: Int,
    override val parent: GuildChannel?,
    override val id: Snowflake,
    override val type: ChannelType,
    override val name: String,
    override var lastMessageId: Snowflake?,
    override val lastPinTimestamp: Instant?,
    public val instance: Disko
) : GuildVoiceChannel, MessageChannelImpl(instance, guild) {

    override fun isVisibleTo(user: User): Boolean {
        return guild.getMember(instance, user)?.hasPermission(Permission.VIEW_CHANNEL) ?: false
    }

    override fun getMessageHistory(limit: Int): Set<Message> {
        TODO("Not yet implemented")
    }

    override fun getMessageHistoryBefore(message: Message, limit: Int): Set<Message> {
        TODO("Not yet implemented")
    }

    override fun getMessageHistoryBefore(id: Snowflake, limit: Int): Set<Message> {
        TODO("Not yet implemented")
    }

    override fun getMessageHistoryAfter(message: Message, limit: Int): Set<Message> {
        TODO("Not yet implemented")
    }

    override fun getMessageHistoryAfter(id: Snowflake, limit: Int): Set<Message> {
        TODO("Not yet implemented")
    }

}
