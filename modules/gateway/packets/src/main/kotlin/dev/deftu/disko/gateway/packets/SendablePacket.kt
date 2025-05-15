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

package dev.deftu.disko.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.gateway.GatewayContext

/**
 * Represents a packet that can be sent to Discord's Gateway API.
 *
 * @see Packet
 * @see ReceivablePacket
 *
 * @since 0.1.0
 * @author Deftu
 */
public interface SendablePacket : Packet {

    public val op: Int

    /**
     * Creates the data within a payload that will be sent to Discord's Gateway API. This is the `"d"` field of the JSON content sent to Discord.
     *
     * @since 0.1.0
     * @author Deftu
     */
    public fun createPayload(context: GatewayContext): JsonElement

}
