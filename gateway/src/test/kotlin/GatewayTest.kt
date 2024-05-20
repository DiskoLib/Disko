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
import dev.deftu.disko.gateway.presence.Activity
import dev.deftu.disko.presence.Status
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

private const val NAME = "@PROJECT_NAME@"
private val logger = LoggerFactory.getLogger("TestBot")

fun main() {
    logger.info("Starting test Disko Gateway...")
    val token = System.getenv("TOKEN") ?: error("No token provided.")
    val gateway = DefaultDiskoGateway(
        name = NAME,
        token = token,
        httpClient = DiskoGateway.createOptimalHttpClient(),
        intents = GatewayIntent.all,
    )

    gateway.setPresence {
        status = Status.DND
        activities {
            +Activity.playing("Disko is so cool :3")
        }
    }

    // TODO - Maybe add a shutdown hook to close the gateway?
    Runtime.getRuntime().addShutdownHook(Thread({
        logger.info("Shutting down Disko Test Bot...")
        gateway.close(1000, "Shutting down")
        logger.info("Disko Test Bot has been shut down.")
    }, "Disko Test Bot Shutdown"))

    gateway.login()
}
