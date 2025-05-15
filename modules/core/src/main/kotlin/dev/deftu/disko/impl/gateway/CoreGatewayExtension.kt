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

package dev.deftu.disko.impl.gateway

import dev.deftu.disko.api.Disko
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.Sharder
import dev.deftu.disko.gateway.packets.*
import dev.deftu.disko.impl.gateway.packets.*

public object CoreGatewayExtension {

    @JvmStatic
    public fun registerOn(instance: Disko, gateway: DiskoGateway) {
        // Receivable
        gateway.registerPacketWithParameters(0, "READY", ReadyPacket::class.java) { arrayOf(instance) }
        gateway.registerPacketWithParameters(0, "GUILD_CREATE", GuildCreatePacket::class.java) { arrayOf(instance) }
        gateway.registerPacketWithParameters(0, "GUILD_DELETE", GuildDeletePacket::class.java) { arrayOf(instance) }
        gateway.registerPacketWithParameters(0, "GUILD_MEMBERS_CHUNK", GuildMemberChunkPacket::class.java) { arrayOf(instance) }
        gateway.registerPacketWithParameters(0, "MESSAGE_CREATE", MessageCreatePacket::class.java) { arrayOf(instance) }
        gateway.registerPacketWithParameters(0, "MESSAGE_DELETE", MessageDeletePacket::class.java) { arrayOf(instance) }

        // Sendable
        gateway.registerPacket(8, null, RequestGuildMembersPacket::class.java)

    }

    @JvmStatic
    public fun registerOn(instance: Disko, sharder: Sharder) {
        sharder.forEach { _, gateway -> registerOn(instance, gateway) }
    }

}
