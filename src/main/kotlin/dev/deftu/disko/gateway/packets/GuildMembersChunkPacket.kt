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
import dev.deftu.disko.utils.maybeGetInteger
import dev.deftu.disko.utils.maybeGetJsonArray
import dev.deftu.disko.utils.maybeGetSnowflake

public class GuildMembersChunkPacket : BaseReceivePacket {
    public companion object : PacketRegistrationData(0, "GUILD_MEMBERS_CHUNK", GuildMembersChunkPacket::class)

    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        if (data == null || !data.isJsonObject) return

        val dataObj = data.asJsonObject
        val id = dataObj.maybeGetSnowflake("guild_id") ?: return
        val chunkIndex = dataObj.maybeGetInteger("chunk_index") ?: return
        val chunkCount = dataObj.maybeGetInteger("chunk_count") ?: return
        val isLastChunk = chunkIndex == chunkCount - 1
        val members = dataObj.maybeGetJsonArray("members") ?: return
        listener.guildStateManager.handleChunk(id, isLastChunk, members)
    }
}
