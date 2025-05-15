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

import dev.deftu.disko.gateway.GatewayClient
import dev.deftu.disko.gateway.Shard
import dev.deftu.disko.gateway.dispatcher
import dev.deftu.disko.gateway.packets.Protocol
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient

fun main(args: Array<String>) {
    val token = System.getenv("DISCORD_TOKEN")
        ?: error("Please set the DISCORD_TOKEN environment variable")
    val gateway = GatewayClient(
        coroutineContext = Dispatchers.IO,
        name = "Disko Test",
        token = token,
        httpClient = OkHttpClient(),
        gatewayUrl = "wss://gateway.discord.gg/?v=10&encoding=json",
        intents = emptySet(),
        threshold = 1,
        shard = Shard(0, 1),
        dispatcher = dispatcher {
            install(Protocol)
        },
    )

    gateway.login()
}
