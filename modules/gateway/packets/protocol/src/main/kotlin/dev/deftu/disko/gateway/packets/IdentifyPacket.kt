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
import dev.deftu.disko.gateway.GatewayIntent
import dev.deftu.disko.api.utils.add
import dev.deftu.disko.api.utils.buildJsonArray
import dev.deftu.disko.api.utils.buildJsonObject
import dev.deftu.disko.gateway.GatewayContext

public object IdentifyPacket : SendablePacket {

    override val op: Int = 2

    override fun createPayload(context: GatewayContext): JsonObject {
        return buildJsonObject {
            add("properties", buildJsonObject {
                add("os", System.getProperty("os.name"))
                add("browser", context.name)
                add("device", context.name)
            })

            context.shard?.let { shard ->
                add("shard", buildJsonArray {
                    add(shard.id)
                    add(shard.total)
                })
            }

            add("token", context.token)
            add("intents", GatewayIntent.toBitset(context.intents.toList()))
            add("large_threshold", context.threshold)
            add("compress", false)
        }
    }

}
