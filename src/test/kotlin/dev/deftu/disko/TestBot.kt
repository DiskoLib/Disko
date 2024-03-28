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

package dev.deftu.disko

import dev.deftu.disko.events.GuildCreateEvent
import dev.deftu.disko.events.ReadyEvent
import dev.deftu.disko.gateway.intents.GatewayIntent
import dev.deftu.disko.presence.OnlineStatus
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Test Bot")

fun main() {
    logger.info("Starting bot")
    val disko = Disko {
        intents {
            +GatewayIntent.GUILDS
            +GatewayIntent.MESSAGE_CONTENT
            +GatewayIntent.GUILD_MESSAGES
        }

        presence {
            status = OnlineStatus.DND
        }
    }

    disko.login(System.getenv("TOKEN"))

    disko.eventBus.on<ReadyEvent> { event ->
        logger.info("${event.selfUser.username} is ready")
    }

    disko.eventBus.on<GuildCreateEvent> { event ->
        logger.info("Joined guild ${event.guild.name} - ${event.guild.getIconUrl()}")
    }
}
