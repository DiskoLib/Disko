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

import dev.deftu.disko.gateway.packets.PacketHandler
import dev.deftu.disko.gateway.packets.PacketRegistry
import dev.deftu.disko.gateway.packets.ReceivablePacket

public class GatewayDispatcher(public val registry: PacketRegistry) {

    public fun dispatch(gateway: GatewayClient, packet: ReceivablePacket) {
        val handler = registry.handlerFor(packet::class) ?: return

        @Suppress("UNCHECKED_CAST")
        (handler as PacketHandler<ReceivablePacket>).handle(gateway, packet)
    }

}
