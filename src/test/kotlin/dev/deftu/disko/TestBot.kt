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

import dev.deftu.disko.events.*
import dev.deftu.disko.gateway.intents.GatewayIntent
import dev.deftu.disko.presence.OnlineStatus
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Test Bot")
private val schedulerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

fun main() {
    logger.info("Starting bot")
    val disko = Disko {
        intents {
            +GatewayIntent.GUILDS
            +GatewayIntent.MESSAGE_CONTENT
            +GatewayIntent.GUILD_MESSAGES
            +GatewayIntent.GUILD_MEMBERS
        }

        presence {
            status = OnlineStatus.DND
        }
    }

    disko.login(System.getenv("TOKEN"))

    disko.eventBus.on<ReadyEvent> { event ->
        logger.info("${event.instance.selfUser?.username} is ready")
    }

    disko.eventBus.on<GuildSetupEvent> { event ->
        logger.info("Set up guild ${event.guild.name} - ${event.guild.getIconUrl()}")
    }

    disko.eventBus.on<GuildJoinEvent> { event ->
        logger.info("Joined guild ${event.guild.name} - ${event.guild.getIconUrl()}")
    }

    disko.eventBus.on<GuildAvailableEvent> { event ->
        logger.info("Guild ${event.guild.name} is available")
    }

    disko.eventBus.on<GuildLeaveEvent> { event ->
        logger.info("Left guild ${event.guild.name}")
    }

    disko.eventBus.on<GuildUnavailableEvent> { event ->
        logger.info("Guild ${event.guild.name} is unavailable")
    }

    disko.eventBus.on<MessageCreateEvent> { event ->
        if (event.message.content.startsWith("test")) {
            if (event.author.isBot) return@on

            event.channel.send {
                embed {
                    title = "Performance Metrics"
                    description = buildString {
                        val totalMemory = Runtime.getRuntime().totalMemory()
                        val freeMemory = Runtime.getRuntime().freeMemory()
                        val usedMemory = totalMemory - freeMemory
                        val maxMemory = Runtime.getRuntime().maxMemory()

                        appendLine("Total Memory: ${totalMemory / 1024 / 1024}MB")
                        appendLine("Free Memory: ${freeMemory / 1024 / 1024}MB")
                        appendLine("Used Memory: ${usedMemory / 1024 / 1024}MB")
                        appendLine("Max Memory: ${maxMemory / 1024 / 1024}MB")
                    }
                }

                val guild = event.guild
                if (guild == null) {
                    content = "Hello, ${event.message.author.username}! (DM)"

                    return@send
                }

                val member = event.member
                if (member == null) {
                    content = "Hello, ${event.message.author.username}! (Guild: ${guild.name})"

                    return@send
                }

                content = "test : Hello, ${event.author.username}! (Guild: ${guild.name}, Member: ${member.nickname ?: member.user.username})"
            }
        }
    }
}
