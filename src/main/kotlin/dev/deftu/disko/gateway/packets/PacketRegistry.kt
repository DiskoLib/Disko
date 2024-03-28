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
import dev.deftu.disko.DiskoConstants
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonObject
import dev.deftu.disko.utils.isValidJson
import dev.deftu.disko.utils.parseJson
import dev.deftu.disko.gateway.DiskoGateway
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

public class PacketRegistry(
    private val listener: DiskoGateway
) {
    private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Packet Registry")
    private val packets = mutableMapOf<Pair<Int, String?>, KClass<out BasePacket>>()

    public fun parse(message: String): Pair<JsonElement?, BasePacket?>? {
        if (!isValidJson(message)) {
            logger.warn("Received invalid JSON for packet: $message")
            return null
        }

        val json = message.parseJson()
        if (
            !json.isJsonObject ||
            !(json.asJsonObject.has("op") && json.asJsonObject.has("t"))
        ) {
            logger.warn("Received invalid schema for packet: $message")
            return null
        }

        val op = json.asJsonObject["op"]?.let { if (it.isJsonNull) null else it.asInt }
        val name = json.asJsonObject["t"]?.let { if (it.isJsonNull) null else it.asString }
        val packet = packets[Pair(op, name)] ?: return null

        val data = json.asJsonObject["d"] ?: null
        val seq = json.asJsonObject["s"]?.let { if (it.isJsonNull) null else it.asInt } ?: -1
        listener.lastSeq = seq

        return data to packet.constructors.first().call()
    }

    public fun jsonify(packet: BasePacket): String {
        if (packet !is BaseSendPacket) throw IllegalArgumentException("Packet (${packet::class.qualifiedName}) is not sendable")

        val op = packets.entries.find {
            it.value == packet::class
        }?.key?.first ?: throw IllegalArgumentException("Packet (${packet::class.qualifiedName}) is not registered")
        val data = packet.createSendJson(listener)

        return buildJsonObject {
            add("op", op)
            if (data != null) add("d", data)
        }.toString()
    }

    public fun register(op: Int, name: String?, packet: KClass<out BasePacket>) {
        packets[Pair(op, name)] = packet
    }

    public fun register(handler: PacketRegistrationHandler) {
        handler.register(this)
    }
}
