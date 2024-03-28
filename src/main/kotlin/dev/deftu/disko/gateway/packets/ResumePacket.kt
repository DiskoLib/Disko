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
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonObject

public class ResumePacket : BaseSendPacket {
    public companion object : PacketRegistrationData(6, null, ResumePacket::class)

    override fun createSendJson(
        listener: DiskoGateway
    ): JsonElement = buildJsonObject {
        if (listener.sessionId == null || listener.resumeGatewayUrl == null)
            throw IllegalStateException("Cannot resume without a session ID or resume URL")

        listener.isResuming = true
        add("token", listener.instance.token)
        add("session_id", listener.sessionId!!)
        add("seq", listener.lastSeq)
    }
}
