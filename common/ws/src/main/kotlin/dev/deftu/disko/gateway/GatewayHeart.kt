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

/**
 * Manages the heartbeat lifecycle for a Discord Gateway connection.
 *
 * This interface abstracts the logic required to maintain a connection by sending heartbeats,
 * handling ACKs, and controlling the heartbeat schedule.
 *
 * Implementations of this interface are typically bound to a single Gateway session
 * and should be driven by the gateway's lifecycle events.
 *
 * @since 0.1.0
 * @author Deftu
 */
public interface GatewayHeart {

    /**
     * Called when a `HELLO` payload is received from the Gateway.
     *
     * This initializes the heartbeat interval and schedules the first heartbeat.
     *
     * @param data The full `HELLO` payload data.
     */
    public fun hello(data: JsonObject)

    /**
     * Called when a `HEARTBEAT_ACK` packet is received from the Gateway.
     *
     * This indicates the server has acknowledged the most recent heartbeat.
     * Implementations should reset any timers or flags used for tracking missed ACKs.
     */
    public fun acknowledge()

    /**
     * Immediately sends a forced heartbeat to the Gateway, regardless of the current schedule.
     *
     * Useful when resuming or reconnecting, or when manual control over the connection is required.
     */
    public fun forcedBeat()

    /**
     * Sends a natural (scheduled) heartbeat to the Gateway as part of the regular heartbeat cycle.
     *
     * This should only be called by the heartbeat scheduler.
     */
    public fun naturalBeat()

    /**
     * Schedules the next heartbeat after the given delay.
     *
     * This is typically used after receiving the initial `HELLO` packet or when rescheduling due to
     * connection anomalies.
     *
     * @param delay The delay in milliseconds before the next heartbeat.
     */
    public fun scheduleBeat(delay: Long)

}
