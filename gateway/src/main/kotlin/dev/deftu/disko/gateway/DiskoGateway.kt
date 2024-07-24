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
import dev.deftu.disko.gateway.packets.*
import dev.deftu.disko.gateway.presence.PresenceUpdate
import dev.deftu.disko.gateway.presence.PresenceUpdateBuilder
import dev.deftu.disko.utils.*
import kotlinx.coroutines.CoroutineScope
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

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
    public val name: String,
    public val token: String,
    public val httpClient: OkHttpClient,
    public val intents: List<GatewayIntent>,
    public val threshold: Int = 250,
    public val shard: Shard = Shard(0, 1)
) : WebSocketListener(), CoroutineScope {

    public companion object {

        @JvmStatic
        public fun createGatewayUrl(version: ApiVersion): String =
            "wss://gateway.discord.gg/?v=${version.number}&encoding=json"

        @JvmStatic
        public fun createOptimalHttpClientBuilder(): OkHttpClient.Builder =
            OkHttpClient.Builder()
                .pingInterval(60, TimeUnit.SECONDS)

        @JvmStatic
        public fun createOptimalHttpClient(): OkHttpClient =
            createOptimalHttpClientBuilder().build()

    }

    private val packets = mutableMapOf<Pair<Int, String?>, Class<out Packet>>()

    /**
     * The object responsible for managing Discord's heartbeat/keep-alive system.
     */
    public val heart: GatewayHeart = GatewayHeart(this)

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

    private var hasIdentified = false
    internal var identifyPresence: PresenceUpdate? = null

    init {
        registerPacket(1, null, HeartbeatPacket::class.java)
        registerPacket(2, null, IdentifyPacket::class.java)
        registerPacket(3, null, PresenceUpdatePacket::class.java)
        registerPacket(10, null, HelloPacket::class.java)
        registerPacket(11, null, HeartbeatAckPacket::class.java)
    }

    // Extensible Functions

    protected open fun onConnected(response: Response) {
    }

    protected open fun onRawMessage(text: String) {
    }

    protected open fun onRawMessage(bytes: ByteString) {
    }

    protected open fun onInvalidMessage(text: String) {
    }

    protected open fun onInvalidPacket(text: String, packet: Class<out Packet>?, type: InvalidPacketType) {
    }

    protected open fun onDisconnected(code: CloseCode, reason: String) {
    }

    // DiskoGateway Implementation

    public open fun login() {
        val request = Request.Builder()
            .url(createGatewayUrl(ApiVersion.V10))
            .build()
        webSocket = httpClient.newWebSocket(request, this)
    }

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
            it.value == packet::class.java
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
        packet: Class<out Packet>
    ) {
        packets[Pair(op, name)] = packet
    }

    public fun setPresence(presence: PresenceUpdate) {
        if (!hasIdentified) {
            identifyPresence = presence
            return
        }

        send(PresenceUpdatePacket(presence))
    }

    public fun setPresence(builder: PresenceUpdateBuilder.() -> Unit) {
        setPresence(PresenceUpdateBuilder()
            .apply(builder)
            .build())
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
        code: CloseCode,
        reason: String
    ) {
        close(code.code, reason)
    }

    public fun registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(Thread({
            close(CloseCode.NORMAL, "Shutting down.")
        }, "Disko Default Shutdown Hook"))
    }

    private fun handleMessage(text: String) {
        if (!isValidJson(text)) {
            onInvalidMessage(text)
            return
        }

        val rawJson = text.parseJson()
        if (!rawJson.isJsonObject || !rawJson.asJsonObject.has("op") || !rawJson.asJsonObject.has("t")) {
            val type = when {
                !rawJson.isJsonObject -> InvalidPacketType.NOT_JSON
                !rawJson.asJsonObject.has("op") -> InvalidPacketType.MISSING_OPCODE
                !rawJson.asJsonObject.has("t") -> InvalidPacketType.MISSING_NAME
                else -> throw IllegalStateException("This should never happen.")
            }

            onInvalidPacket(text, null, type)
            return
        }

        val json = rawJson.asJsonObject
        val op = json.maybeGetInteger("op")
        val name = json.maybeGetString("t")
        if (name == "READY")
            hasIdentified = true


        val data = json.maybeGetJsonObject("d")
        if (op == null && name == null) {
            onInvalidPacket(text, null, InvalidPacketType.BAD_OPCODE_AND_NAME)
            return
        }

        val packet = packets[Pair(op, name)]
        if (packet == null) {
            onInvalidPacket(text, null, InvalidPacketType.UNREGISTERED)
            return
        }

        lastSequence = json.maybeGetInteger("s")
        val instance = packet.constructors.first().newInstance() as? Packet ?: onInvalidPacket(text, packet, InvalidPacketType.INSTANCE_CREATION_FAILED)
        (instance as? ReceivablePacket)?.handlePayloadReceived(this, data) ?: onInvalidPacket(text, packet, InvalidPacketType.NOT_RECEIVABLE)
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

    protected enum class InvalidPacketType {
        NOT_JSON,
        MISSING_OPCODE,
        MISSING_NAME,
        BAD_OPCODE_AND_NAME,
        UNREGISTERED,
        INSTANCE_CREATION_FAILED,
        NOT_RECEIVABLE
    }

}
