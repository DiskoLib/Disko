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

import dev.deftu.disko.api.utils.*
import dev.deftu.disko.gateway.packets.SendablePacket
import kotlinx.coroutines.CoroutineScope
import okhttp3.*
import okio.ByteString
import kotlin.coroutines.CoroutineContext

public class GatewayClient(
    override val coroutineContext: CoroutineContext,
    override val name: String,
    override val token: String,
    public val httpClient: OkHttpClient,
    public val gatewayUrl: String,
    override val threshold: Int,
    public val dispatcher: GatewayDispatcher,
    override val intents: Set<GatewayIntent>,
    override val shard: Shard,
) : WebSocketListener(), CoroutineScope, GatewayContext {

    private var isIdentified = false

    override val heart: GatewayHeartImpl by lazy {
        GatewayHeartImpl(this)
    }

    /**
     * The session ID received from Discord's Gateway API.
     */
    override var sessionId: String? = null
        private set

    /**
     * The last sequence number received from Discord's Gateway API.
     */
    override var lastSequence: Int? = null
        private set

    override val isConnected: Boolean
        get() = TODO("Not yet implemented")

    override val isResuming: Boolean
        get() = TODO("Not yet implemented")

    /**
     * The actual WebSocket connection to Discord's Gateway API.
     */
    public var webSocket: WebSocket? = null
        private set

    public fun close(code: Int, reason: String?) {
        webSocket?.close(code, reason)
    }

    public fun close(
        code: CloseCode = CloseCode.NORMAL,
        reason: String? = null
    ) {
        close(code.code, reason)
    }

    public fun login() {
        val request = Request.Builder()
            .url(gatewayUrl)
            .build()
        this.webSocket = httpClient.newWebSocket(request, this)
    }

    public fun send(packet: SendablePacket) {
        val data  = packet.createPayload(this)
        val payload = buildJsonObject {
            add("op", packet.op)
            add("d", data)
        }.toString()

        webSocket?.send(payload)
    }

    private fun handleMessage(text: String) {
        println("Received: $text")
        if (!isValidJson(text)) {
            return
        }

        val rawJson = text.parseJson()
        if (!rawJson.isJsonObject) {
            return
        }

        val jsonObject = rawJson.asJsonObject

        jsonObject.maybeGetInteger("s")?.let {
            this.lastSequence = it
        }

        if (!jsonObject.has("op") || !jsonObject.has("t")) {
            return
        }

        val op = jsonObject.maybeGetInteger("op")
        val type = jsonObject.maybeGetString("t")
        if (op == null) {
            return
        }

        if (type == "READY") {
            this.isIdentified = true

            val sessionId = jsonObject.maybeGetString("s")
            if (sessionId != null) {
                this.sessionId = sessionId
            }
        }

        val data = jsonObject.maybeGet("d")
        println("Data: $data")
        val packet = dispatcher.registry.decodePacket(op, data) ?: return
        println("Packet: $packet")
        dispatcher.dispatch(this, packet)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        this.webSocket = webSocket
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        handleMessage(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        handleMessage(bytes.utf8())
    }

}
