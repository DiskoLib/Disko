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

import dev.deftu.disko.api.Disko
import dev.deftu.disko.api.entity.message.send
import dev.deftu.disko.api.events.guild.GuildSetupEvent
import dev.deftu.disko.api.events.ReadyEvent
import dev.deftu.disko.api.events.message.MessageCreateEvent
import dev.deftu.disko.gateway.GatewayIntent
import dev.deftu.disko.gateway.presence.Activity
import dev.deftu.disko.api.presence.Status
import dev.deftu.eventbus.on
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Test Bot")

fun main() = try {
    if (System.getenv("DEBUG").toBoolean()) {
        Configurator.setRootLevel(Level.ALL)
    }

    logger.info("Starting test Disko bot...")
    val token = System.getenv("TOKEN") ?: error("No token provided.")
    val disko = Disko(token, GatewayIntent.all)
    disko.setPresence {
        status = Status.DND
        activities {
            +Activity.playing("Disko is so cool :3")
        }
    }

    disko.eventBus.on<ReadyEvent> {
        logger.info("Ready event received!")
    }

    disko.eventBus.on<GuildSetupEvent> {
        logger.info("Guild setup event received for guild ${guild.name}")
    }

    disko.eventBus.on<MessageCreateEvent> {
        if (message.content != "Heya!") {
            return@on
        }

        channel.send {
            content = "Hello, ${guild?.name ?: author.username}!"
        }
    }

    disko.login()
} catch (t: Throwable) {
    t.printStackTrace()
}
