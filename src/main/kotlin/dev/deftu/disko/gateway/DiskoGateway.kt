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
            packetRegistry.register(ReadyPacket)
            packetRegistry.register(HeartbeatPacket)
            packetRegistry.register(IdentifyPacket)
            packetRegistry.register(PresenceUpdatePacket)
            packetRegistry.register(HelloPacket)
            packetRegistry.register(HeartbeatAcknowledgePacket)
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
