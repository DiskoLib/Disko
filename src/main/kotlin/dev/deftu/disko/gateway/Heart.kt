package dev.deftu.disko.gateway

import com.google.gson.JsonObject
import dev.deftu.disko.Disko
import dev.deftu.disko.DiskoConstants
import dev.deftu.disko.gateway.packets.HeartbeatPacket
import dev.deftu.disko.gateway.packets.IdentifyPacket
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import kotlin.math.roundToLong

public class Heart(
    private val instance: Disko,
    private val listener: DiskoGateway
) {
    private companion object {
        private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Heart")
        private val scope = CoroutineScope(Dispatchers.IO)
    }

    private var job: Job? = null
    private var lifetimeInterval = 0L
    private var receivedHello = false

    public fun hello(data: JsonObject) {
        val interval = data["heartbeat_interval"]?.asLong ?: return
        lifetimeInterval = interval
        receivedHello = true

        val jittered = (interval * Math.random()).roundToLong()
        logger.debug("Jittered heartbeat interval: $jittered")
        listener.send(IdentifyPacket(instance))
        scheduleBeat(jittered)
    }

    public fun beat() {
        if (!receivedHello) return

        val leeway = (lifetimeInterval * Math.random() * 0.5).roundToLong()
        logger.debug("Received heartbeat ack! $leeway")
        scheduleBeat(lifetimeInterval - leeway)
    }

    public fun close() {
        job?.cancel()
        lifetimeInterval = 0
        receivedHello = false
    }

    private fun scheduleBeat(delay: Long) {
        job?.cancel()
        job = scope.launch {
            delay(delay)
            listener.send(HeartbeatPacket())
        }
    }
}
