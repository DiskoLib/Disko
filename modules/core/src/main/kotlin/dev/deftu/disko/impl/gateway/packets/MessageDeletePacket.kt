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
import dev.deftu.disko.api.DiskoConstants
import dev.deftu.disko.api.events.message.MessageDeleteEvent
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.api.utils.isNull
import dev.deftu.disko.api.utils.maybeGetSnowflake
import dev.deftu.disko.gateway.packets.ReceivablePacket
import org.slf4j.LoggerFactory

public class MessageDeletePacket(private val instance: Disko) : ReceivablePacket {

    private companion object {

        private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Message Delete Packet")

    }

    override fun handlePayloadReceived(
        gateway: DiskoGateway,
        data: JsonElement?
    ) {
        if (data == null || !data.isJsonObject) return

        val dataObj = data.asJsonObject
        val guild = (if (!dataObj.isNull("guild_id")) {
            val guildId = dataObj.maybeGetSnowflake("guild_id")
            if (guildId != null) {
                val guild = instance.entityCache.getGuild(guildId)
                if (guild == null) {
                    logger.debug("Received a message delete packet for a guild that is not cached.")
                    null
                } else guild
            } else null
        } else null) ?: return logger.debug("Received a message delete packet without a guild ID.")

        val messageId = dataObj.maybeGetSnowflake("id") ?: return logger.debug("Received a message delete packet without a message ID.")
        val channelId = dataObj.maybeGetSnowflake("channel_id") ?: return logger.debug("Received a message delete packet without a channel ID.")

        val channel = instance.entityCache.getChannel(channelId)?.asMessageChannel()
        if (channel == null) {
            // TODO - Cache channel event
            return logger.debug("Received a message delete packet for a channel that is not cached.")
        }

        instance.eventBus.post(MessageDeleteEvent(instance, gateway.shard.id, channel, messageId))
    }

}
