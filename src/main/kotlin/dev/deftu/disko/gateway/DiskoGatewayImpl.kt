package dev.deftu.disko.gateway

import dev.deftu.disko.Disko
import dev.deftu.disko.DiskoConstants
import dev.deftu.disko.utils.isInternetAvailable
import dev.deftu.disko.gateway.packets.BasePacket
import okhttp3.Response
import okhttp3.WebSocket
import org.slf4j.LoggerFactory
import java.net.ConnectException

internal class DiskoGatewayImpl(
    instance: Disko,
    shardId: Int
) : DiskoGateway(instance, shardId) {
    private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Gateway $shardId")

    init {
        registerDefaultPackets()
    }

    override fun send(packet: BasePacket) {
        webSocket?.send(packetRegistry.jsonify(packet))
    }

    override fun handleUnknownMessage(
        type: UnknownMessageType,
        text: String
    ) {
        logger.warn("Received unknown message at 'phase' ${type.name}: $text")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        logger.info("Connected to gateway.")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        logger.info("Disconnected from gateway. Code: $code, reason: '$reason'")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        logger.error("Gateway raised an error!", t)
        if (t is ConnectException) {
            logger.error("Failed to connect to gateway - Diagnosing...")
            val isInternetAvailable = isInternetAvailable(instance.httpClient)
            if (!isInternetAvailable) {
                logger.error("No internet connection available.")
                return
            }

            logger.error("Internet connection is available, the gateway may be unreachable. Will attempt to reconnect with a new URL provided by Discord.")
            instance.gatewayMetadata.refresh()
            instance.shardManager.reconnectShard(shardId)
        }
    }
}
