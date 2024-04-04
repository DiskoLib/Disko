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

package dev.deftu.disko.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.events.GuildCreateEvent
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.utils.maybeGetJsonArray

public class GuildCreatePacket : BaseReceivePacket {
    public companion object : PacketRegistrationData(0, "GUILD_CREATE", GuildCreatePacket::class)

    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        if (data == null || !data.isJsonObject) return

        val instance = listener.instance
        val json = data.asJsonObject
        val guild = instance.entityConstructor.constructGuild(json) ?: return

        val members = json.maybeGetJsonArray("members")
        members?.forEach { rawMember ->
            instance.entityConstructor.constructMember(rawMember.asJsonObject)?.let { member ->
                instance.memberCache.addMember(member)
                instance.userCache.addUser(member.user)
            }
        }

        json.maybeGetJsonArray("channels")?.forEach { rawChannel ->
            instance.entityConstructor.constructChannel(listener.shardId, guild, rawChannel.asJsonObject)?.let { channel ->
                instance.channelCache.addChannel(channel)
            }
        }

        instance.guildCache.addGuild(guild)
        instance.eventBus.post(GuildCreateEvent(instance, listener.shardId, guild))
    }
}
