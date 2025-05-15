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
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.api.utils.maybeGetJsonArray
import dev.deftu.disko.api.utils.maybeGetJsonObject
import dev.deftu.disko.api.utils.maybeGetSnowflake
import dev.deftu.disko.gateway.packets.ReceivablePacket

public class ReadyPacket(private val instance: Disko) : ReceivablePacket {

    override fun handlePayloadReceived(gateway: DiskoGateway, data: JsonElement?) {
        if (data == null || !data.isJsonObject) return

        val obj = data.asJsonObject

//        val sessionId = dataObj.maybeGetString("session_id") ?: throw IllegalStateException("expected session ID in READY packet")

//        val resumeGatewayUrl = dataObj.maybeGetString("resume_gateway_url") ?: throw IllegalStateException("expected resume gateway URL in READY packet")

        val user = obj.maybeGetJsonObject("user") ?: throw IllegalStateException("Expected user object in READY packet")
        val selfUser = instance.entityBuilder.selfUser(user.asJsonObject) ?: return
        instance.selfUser = selfUser
        instance.entityCache.addUser(selfUser)

        val guilds = obj.maybeGetJsonArray("guilds") ?: throw IllegalStateException("Expected guilds array in READY packet")
        val guildCount = guilds.size()
        if (guildCount > 0) {
            guilds.forEach { guild ->
                if (!guild.isJsonObject) return@forEach

                val guildObj = guild.asJsonObject
                val id = guildObj.maybeGetSnowflake("id") ?: return@forEach
                instance.guildManager.handleGuildReady(gateway, id)
            }
        } else {
            instance.guildManager.markBotReady()
            instance.guildManager.postReadyForcibly(gateway)
        }
    }

}
