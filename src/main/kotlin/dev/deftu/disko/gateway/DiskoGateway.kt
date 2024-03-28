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

import dev.deftu.disko.Disko
import dev.deftu.disko.gateway.packets.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

public abstract class DiskoGateway(
    public val instance: Disko,
    public val shardId: Int
) : WebSocketListener() {
    public companion object {
        public fun DiskoGateway.registerDefaultPackets() {
            // Send Events
            packetRegistry.register(IdentifyPacket)
            packetRegistry.register(HeartbeatPacket)

            // Receive Events
            packetRegistry.register(HeartbeatAcknowledgePacket)
            packetRegistry.register(HelloPacket)
            packetRegistry.register(ReadyPacket)
            packetRegistry.register(PresenceUpdatePacket)
            packetRegistry.register(GuildCreatePacket)
        }
    }

    public var webSocket: WebSocket? = null
        protected set
    public val heart: Heart = Heart(instance, this)
    public val packetRegistry: PacketRegistry = PacketRegistry(this)

    internal var lastSeq = -1
    internal var sessionId: String? = null

    public abstract fun send(packet: BasePacket)

    public open fun handleUnknownMessage(type: UnknownMessageType, text: String) {
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        this.webSocket = webSocket
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val data = packetRegistry.parse(text) ?: return handleUnknownMessage(UnknownMessageType.PARSE, text)
        val (json, packet) = data
        packet?.handleDataReceived(this, json, lastSeq) ?: handleUnknownMessage(UnknownMessageType.HANDLE, text)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        heart.close()
    }

    public enum class UnknownMessageType {
        PARSE,
        HANDLE
    }
}
