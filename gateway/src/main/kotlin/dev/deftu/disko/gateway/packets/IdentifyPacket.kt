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

import com.google.gson.JsonObject
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.GatewayIntent
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonArray
import dev.deftu.disko.utils.buildJsonObject

public class IdentifyPacket : SendablePacket {
    override fun createSendPayload(gateway: DiskoGateway): JsonObject = buildJsonObject {
        add("properties", buildJsonObject {
            add("os", System.getProperty("os.name"))
            add("browser", gateway.name)
            add("device", gateway.name)
        })

        add("shard", buildJsonArray {
            add(gateway.shard.id)
            add(gateway.shard.total)
        })

        add("token", gateway.token)
        add("intents", GatewayIntent.toBitset(gateway.intents))
        add("large_threshold", gateway.threshold)
        add("compress", false)
    }
}
