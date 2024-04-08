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
import dev.deftu.disko.utils.maybeGetJsonArray
import dev.deftu.disko.utils.maybeGetJsonObject
import dev.deftu.disko.utils.maybeGetSnowflake
import dev.deftu.disko.utils.maybeGetString

public class ReadyPacket : BaseReceivePacket {
    public companion object : PacketRegistrationData(0, "READY", ReadyPacket::class)

    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        if (data == null || !data.isJsonObject) return

        val dataObj = data.asJsonObject

        val sessionId = dataObj.maybeGetString("session_id") ?: throw IllegalStateException("expected session ID in READY packet")
        listener.sessionId = sessionId

        val resumeGatewayUrl = dataObj.maybeGetString("resume_gateway_url") ?: throw IllegalStateException("expected resume gateway URL in READY packet")
        listener.resumeGatewayUrl = resumeGatewayUrl

        val user = dataObj.maybeGetJsonObject("user") ?: throw IllegalStateException("expected user object in READY packet")
        val selfUser = listener.instance.entityConstructor.constructSelfUser(user.asJsonObject) ?: return
        listener.instance.selfUser = selfUser

        val guilds = data.asJsonObject.maybeGetJsonArray("guilds") ?: throw IllegalStateException("Expected guilds array in READY packet")
        val guildCount = guilds.size()
        if (guildCount > 0) {
            guilds.forEach { guild ->
                if (!guild.isJsonObject) return@forEach

                val obj = guild.asJsonObject
                val id = obj.maybeGetSnowflake("id") ?: return@forEach
                listener.guildStateManager.handleReady(id)
            }
        } else {
            listener.guildStateManager.markReady()
            listener.instance.eventBus.post(ReadyEvent(listener.instance, listener.shardId))
        }

        // TODO - private channels, presences, relationships
    }
}
