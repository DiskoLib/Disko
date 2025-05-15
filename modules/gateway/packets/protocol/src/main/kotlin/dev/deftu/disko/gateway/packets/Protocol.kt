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
@file:JvmName("Protocol")

package dev.deftu.disko.gateway.packets

public val Protocol: PacketRegistry
    @JvmName("get")
    get() = PacketRegistry {
        registerDecoder(1) { HeartbeatPacket }
        registerDecoder(10) { it?.asJsonObject?.let(::HelloPacket) }
        registerDecoder(11) { HeartbeatAckPacket }

        registerHandler<HeartbeatPacket> { context, _ ->
            println("Heartbeat received")
            context.heart.forcedBeat()
        }

        registerHandler<HelloPacket> { context, packet ->
            println("Hello received")
            context.heart.hello(packet.data)
        }

        registerHandler<HeartbeatAckPacket> { context, _ ->
            println("Heartbeat ACK received")
            context.heart.acknowledge()
        }
    }
