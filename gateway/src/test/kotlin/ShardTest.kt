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

import dev.deftu.disko.gateway.DefaultDiskoGateway
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.GatewayIntent
import dev.deftu.disko.gateway.Sharder
import dev.deftu.disko.gateway.presence.Activity
import dev.deftu.disko.presence.Status
import dev.deftu.disko.utils.ApiVersion
import org.slf4j.LoggerFactory

private const val NAME = "@PROJECT_NAME@"
private val logger = LoggerFactory.getLogger("TestBot")

fun main() {
    logger.info("Starting test Disko Sharding Manager...")
    val token = System.getenv("TOKEN") ?: error("No token provided.")
    val sharder = Sharder(
        token = token,
        httpClient = DiskoGateway.createOptimalHttpClient(),
        gatewayBuilder = { token, httpClient, shard ->
            DefaultDiskoGateway(
                name = NAME,
                token = token,
                httpClient = httpClient,
                intents = GatewayIntent.all
            )
        }
    ).registerShutdownHook().login(ApiVersion.V10)

    sharder.forEach { shard, gateway ->
        gateway.setPresence {
            status = Status.DND
            activities {
                +Activity.playing("[${shard.id + 1}/${shard.total}] Disko is so cool :3")
            }
        }
    }
}
