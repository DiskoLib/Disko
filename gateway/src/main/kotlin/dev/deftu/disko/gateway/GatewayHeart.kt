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

import com.google.gson.JsonObject
import dev.deftu.disko.gateway.packets.HeartbeatPacket
import dev.deftu.disko.gateway.packets.IdentifyPacket
import dev.deftu.disko.utils.maybeGetLong
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import kotlin.math.roundToLong

public class GatewayHeart(
    private val gateway: DiskoGateway
) {
    private val logger = LoggerFactory.getLogger("Gateway Heart ${gateway.shard.id}")
    private var job: Job? = null
    private var lifetimeInterval = 0L
    private var receivedHello = false
    private var missedBeats = 0

    public fun hello(data: JsonObject) {
        lifetimeInterval = data.maybeGetLong("heartbeat_interval") ?: return
        receivedHello = true

        val jitter = (lifetimeInterval * Math.random()).roundToLong()
        logger.debug("Received hello, sending identify.")
        // TODO - Check for resume
        gateway.send(IdentifyPacket())
        logger.debug("Scheduling first heartbeat in $jitter ms.")
        scheduleBeat(jitter)
    }

    public fun acknowledge() {
        if (!receivedHello) return

        missedBeats = 0
        val leeway = (lifetimeInterval * Math.random()).roundToLong()
        logger.debug("Acknowledged heartbeat, scheduling next beat in ${lifetimeInterval - leeway}ms")
        scheduleBeat(lifetimeInterval - leeway)
    }

    public fun forceBeat() {
        if (!receivedHello) {
            logger.debug("Discord sent a force heartbeat, but we haven't received hello yet. Ignoring.")
            return
        }

        job?.cancel()
        missedBeats = 0
        gateway.send(HeartbeatPacket())
    }

    public fun naturalBeat() {
        if (!receivedHello) return

        if (missedBeats >= 3) {
            logger.error("Missed 3 heartbeats, closing gateway.")
            gateway.close(3000, "Zombie Connection")
            return
        }

        missedBeats++
        gateway.send(HeartbeatPacket())
    }

    private fun scheduleBeat(delay: Long) {
        job?.cancel()
        job = gateway.launch {
            delay(delay)
            naturalBeat()
        }
    }
}
