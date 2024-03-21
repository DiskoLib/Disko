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
import dev.deftu.disko.Disko
import dev.deftu.disko.DiskoConstants
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonObject
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.intents.GatewayIntent
import dev.deftu.disko.utils.buildJsonArray

public class IdentifyPacket(
    private val instance: Disko
) : BasePacket {
    public companion object : PacketRegistrationData(2, null, IdentifyPacket::class)

    override fun createSendJson(
        listener: DiskoGateway
    ): JsonElement = buildJsonObject {
        add("token", instance.token)
        add("intents", GatewayIntent.calculate(*instance.intentManager.get().toTypedArray()))

        add("properties", buildJsonObject {
            addProperty("os", System.getProperty("os.name"))
            addProperty("browser", DiskoConstants.NAME)
            addProperty("device", DiskoConstants.NAME)
        })

        add("presence", instance.presenceManager.toJson())
        add("shard", buildJsonArray {
            add(listener.shardId)
            add(listener.instance.gatewayMetadata.getShards())
        })
    }

    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        // no-op
    }
}
