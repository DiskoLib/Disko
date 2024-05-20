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

package dev.deftu.disko.gateway

import dev.deftu.disko.gateway.packets.Packet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory

public class DefaultDiskoGateway(
    name: String,
    token: String,
    httpClient: OkHttpClient,
    intents: List<GatewayIntent>,
    threshold: Int = 250,
    shard: Shard = Shard(0, 1) // Non-sharded bots will always have a shard ID of 0.
) : DiskoGateway(
    coroutineContext = Dispatchers.Default + SupervisorJob(),
    name = name,
    token = token,
    httpClient = httpClient,
    intents = intents,
    threshold = threshold,
    shard = shard
) {

    private val logger = LoggerFactory.getLogger("Default Disko Gateway")

    override fun onInvalidMessage(text: String) {
        logger.error("Received invalid message: $text")
    }

    override fun onInvalidPacket(text: String, packet: Class<out Packet>?, type: InvalidPacketType) {
        logger.error("Received invalid packet: $type, $packet, $text")
    }

}
