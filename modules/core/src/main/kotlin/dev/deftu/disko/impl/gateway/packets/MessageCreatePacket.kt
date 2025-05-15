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

package dev.deftu.disko.impl.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.api.Disko
import dev.deftu.disko.api.entity.guild.Guild
import dev.deftu.disko.api.events.message.MessageCreateEvent
import dev.deftu.disko.api.utils.maybeGetSnowflake
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.packets.ReceivablePacket

public class MessageCreatePacket(private val instance: Disko) : ReceivablePacket {

    override fun handlePayloadReceived(
        gateway: DiskoGateway,
        data: JsonElement?
    ) {
        if (data == null || !data.isJsonObject) return

        val obj = data.asJsonObject
        val channelId = obj.maybeGetSnowflake("channel_id") ?: return
        val channel = instance.entityCache.getChannel(channelId)
            ?: return

        val messageChannel = channel
            .asMessageChannel()
            ?: return

        val guildId = obj.maybeGetSnowflake("guild_id")
        val guild = guildId?.let { instance.entityCache.getGuild(it) }

        val message = instance.entityBuilder.message(messageChannel, guild, gateway.shard.id, obj) ?: return

        instance.eventBus.post(MessageCreateEvent(
            instance,
            gateway.shard.id,
            message
        ))
    }

}
