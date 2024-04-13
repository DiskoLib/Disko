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

import dev.deftu.disko.gateway.exceptions.GatewayClosedException
import dev.deftu.disko.gateway.exceptions.UnregisteredPacketException
import dev.deftu.disko.gateway.packets.Packet
import dev.deftu.disko.gateway.packets.ReceivablePacket
import dev.deftu.disko.gateway.packets.SendablePacket
import dev.deftu.disko.utils.*
import kotlinx.coroutines.CoroutineScope
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

/**
 * Represents the low-level logical gateway for the WebSocket connection to Discord's Gateway API.
 *
 * @param coroutineContext The coroutine context to run the gateway on.
 * @param shardId The shard ID of the gateway. - Will always be `0` for non-sharded bots.
 *
 * @since 0.1.0
 * @author Deftu
 */
public abstract class DiskoGateway(
    final override val coroutineContext: CoroutineContext,
    private val token: String,
    public val shardId: Int
) : WebSocketListener(), CoroutineScope {
    private val packets = mutableMapOf<Pair<Int, String?>, KClass<out Packet>>()

    /**
     * The last sequence number received from Discord's Gateway API.
     */
    public var lastSequence: Int? = null
        private set

    /**
     * The actual WebSocket connection to Discord's Gateway API.
     */
    public var webSocket: WebSocket? = null
        private set

    // Extensible Functions

    protected open fun onConnected(response: Response) {
    }

    protected open fun onRawMessage(text: String) {
    }

    protected open fun onRawMessage(bytes: ByteString) {
    }

    protected open fun onInvalidMessage(text: String) {
    }

    protected open fun onInvalidPacket(text: String) {
    }

    protected open fun onDisconnected(code: CloseCode, reason: String) {
    }

    // DiskoGateway Implementation

    /**
     * Sends a packet to Discord's Gateway API - Automatically inferring its opcode from the packet registry.
     *
     * @param packet The packet to send.
     * @throws GatewayClosedException If the WebSocket connection is closed.
     * @throws UnregisteredPacketException If the packet is not registered in the packet registry.
     * @see Packet
     * @see SendablePacket
     *
     * @since 0.1.0
     * @author Deftu
     */
    public fun send(packet: SendablePacket) {
        val op = packets.entries.find {
            it.value == packet::class
        }?.key?.first ?: throw UnregisteredPacketException("Packet ${packet::class.simpleName} is not registered in the packet registry.")
        val json = packet.createSendPayload(this)

        val payload = buildJsonObject {
            add("op", op)
            add("d", json)
        }.toString()
        webSocket?.send(payload) ?: throw GatewayClosedException("Cannot send packet ${packet::class.simpleName} as the WebSocket connection is closed.")
    }

    /**
     * Registers a packet in the packet registry. If a given packet is not registered into the packet registry, it will not be able to be sent or received.
     *
     * This function is intended to be used internally, unless you may need to register packets for new behaviour which the library may not support yet.
     *
     * @param op The opcode of the packet. - Will always be `0` for DISPATCH packets.
     * @param name The name of the packet. - Will always be `null` for non-DISPATCH packets, and the event name for DISPATCH packets.
     * @param packet The packet class to register
     * @see Packet
     * @see SendablePacket
     * @see ReceivablePacket
     *
     * @since 0.1.0
     * @author Deftu
     */
    public fun registerPacket(
        op: Int,
        name: String?,
        packet: KClass<out Packet>
    ) {
        packets[Pair(op, name)] = packet
    }

    /**
     * Closes the WebSocket connection to Discord's Gateway API.
     *
     * @param code The close code to send to Discord's Gateway API.
     * @param reason The reason to send to Discord's Gateway API.
     * @throws GatewayClosedException If the WebSocket connection is already closed.
     *
     * @since 0.1.0
     * @author Deftu
     */
    public fun close(
        code: Int,
        reason: String
    ) {
        webSocket?.close(code, reason) ?: throw GatewayClosedException("Cannot close the WebSocket connection as it is already closed.")
    }

    private fun handleMessage(text: String) {
        if (!isValidJson(text)) {
            onInvalidMessage(text)
            return
        }

        val rawJson = text.parseJson()
        if (!rawJson.isJsonObject || !rawJson.asJsonObject.has("op") || !rawJson.asJsonObject.has("t")) {
            onInvalidPacket(text)
            return
        }

        val json = rawJson.asJsonObject
        val op = json.maybeGetInteger("op")
        val name = json.maybeGetString("t")
        val data = json.maybeGetJsonObject("d")
        if (op == null && name == null) {
            onInvalidPacket(text)
            return
        }

        val packet = packets[Pair(op, name)] ?: return
        if (packet.isInstance(ReceivablePacket::class)) {
            onInvalidPacket(text)
            return
        }

        lastSequence = json.maybeGetInteger("s")

        val instance = packet.constructors.first().call()
        (instance as? ReceivablePacket)?.handlePayloadReceived(this, data) ?: onInvalidPacket(text)
    }

    // WebSocketListener Implementation
    final override fun onOpen(webSocket: WebSocket, response: Response) {
        this.webSocket = webSocket
        onConnected(response)
    }

    final override fun onMessage(webSocket: WebSocket, text: String) {
        onRawMessage(text)
        handleMessage(text)
    }

    final override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        onRawMessage(bytes)
        handleMessage(bytes.utf8())
    }

    final override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        onDisconnected(CloseCode.fromCode(code), reason)
    }

    final override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        // TODO: Implement
    }
}
