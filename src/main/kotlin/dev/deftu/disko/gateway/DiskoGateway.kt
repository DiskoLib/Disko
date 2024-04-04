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
            packetRegistry.register(ResumePacket)
            packetRegistry.register(HeartbeatPacket)

            // Receive Events
            packetRegistry.register(HeartbeatAcknowledgePacket)
            packetRegistry.register(HelloPacket)
            packetRegistry.register(ReadyPacket)
            packetRegistry.register(ReconnectPacket)
            packetRegistry.register(InvalidSessionPacket)
            packetRegistry.register(PresenceUpdatePacket)
            packetRegistry.register(GuildCreatePacket)
            packetRegistry.register(MessageCreatePacket)
        }
    }

    public var webSocket: WebSocket? = null
        protected set
    public val heart: Heart = Heart(instance, this)
    public val packetRegistry: PacketRegistry = PacketRegistry(this)

    internal var isResuming = false
    internal var lastSeq = -1
    internal var resumeGatewayUrl: String? = null
    internal var sessionId: String? = null

    public abstract fun send(packet: BasePacket)

    public open fun onConnected(response: Response) {
    }

    public open fun onClosed(opcode: GatewayCloseOpcode, reason: String) {
    }

    public open fun handleUnknownMessage(type: UnknownMessageType, text: String) {
    }

    final override fun onOpen(webSocket: WebSocket, response: Response) {
        this.webSocket = webSocket
        onConnected(response)
    }

    final override fun onMessage(webSocket: WebSocket, text: String) {
        val data = packetRegistry.parse(text) ?: return handleUnknownMessage(UnknownMessageType.PARSE, text)
        val (json, packet) = data
        if (packet !is BaseReceivePacket) return handleUnknownMessage(UnknownMessageType.TYPE, text)
        packet.handleDataReceived(this, json, lastSeq) ?: handleUnknownMessage(UnknownMessageType.HANDLE, text)
    }

    final override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        heart.close()
        val opcode = GatewayCloseOpcode.from(code) ?: GatewayCloseOpcode.UNKNOWN_ERROR
        onClosed(opcode, reason)
    }

    public enum class UnknownMessageType {
        PARSE,
        TYPE,
        HANDLE
    }
}
