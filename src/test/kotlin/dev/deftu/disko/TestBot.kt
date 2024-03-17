package dev.deftu.disko

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
}
