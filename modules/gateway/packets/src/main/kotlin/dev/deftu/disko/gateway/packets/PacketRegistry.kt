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
import kotlin.reflect.KClass

public class PacketRegistry @JvmOverloads constructor(init: PacketRegistry.() -> Unit = {}) {

    private val receivablePacketDecoders = mutableMapOf<Int, PacketDecoder<out ReceivablePacket>>()

    private val handlers = mutableMapOf<KClass<out ReceivablePacket>, PacketHandler<out ReceivablePacket>>()

    init {
        init()
    }

    public fun <T : ReceivablePacket> registerDecoder(op: Int, decoder: PacketDecoder<T>) {
        receivablePacketDecoders[op] = decoder
    }

    public fun decodePacket(op: Int, data: JsonElement?): ReceivablePacket? {
        val decoder = receivablePacketDecoders[op] ?: return null
        return decoder.decode(data)
    }

    public fun <T : ReceivablePacket> registerHandler(clz: KClass<T>, handler: PacketHandler<T>) {
        handlers[clz] = handler
    }

    public fun <T : ReceivablePacket> handlerFor(clz: KClass<T>): PacketHandler<T>? {
        @Suppress("UNCHECKED_CAST")
        return handlers[clz] as? PacketHandler<T>
    }

    /**
     * ```
     * registry.install(Guilds)
     * registry.install(Messages)
     * registry.install(Channels)
     * registry.install(Users)
     * ```
     */
    public fun install(vararg registries: PacketRegistry) {
        for (registry in registries) {
            receivablePacketDecoders.putAll(registry.receivablePacketDecoders)
            handlers.putAll(registry.handlers)
        }
    }

}
