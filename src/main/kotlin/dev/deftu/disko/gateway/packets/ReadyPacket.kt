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
import dev.deftu.disko.events.ReadyEvent
import dev.deftu.disko.gateway.DiskoGateway

public class ReadyPacket : BaseReceivePacket {
    public companion object : PacketRegistrationData(0, "READY", ReadyPacket::class)


    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        if (data == null || !data.isJsonObject) return

        val userJson = data.asJsonObject["user"].asJsonObject
        val selfUser = listener.instance.entityConstructor.constructSelfUser(userJson) ?: return
        listener.instance.selfUser = selfUser

        val rawSessionId = data.asJsonObject["session_id"] ?: return
        listener.sessionId = rawSessionId.asString

        // TODO - guilds, private channels, presences, relationships

        listener.instance.eventBus.post(ReadyEvent(listener.instance, listener.shardId, selfUser))
    }
}
