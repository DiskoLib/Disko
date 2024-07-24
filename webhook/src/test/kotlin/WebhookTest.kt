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

import dev.deftu.disko.utils.Snowflake
import dev.deftu.disko.webhook.Webhook
import dev.deftu.disko.webhook.send
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Test Webhook")

fun main() {
    logger.info("Starting test Disko Webhook...")

    val webhookUrl = System.getenv("WEBHOOK_URL") ?: System.getProperty("webhookUrl")
    val webhook = if (webhookUrl != null) {
        Webhook.createFromUrl(webhookUrl)
    } else {
        val webhookId = System.getenv("WEBHOOK_ID")?.toLongOrNull() ?: System.getProperty("webhookId")?.toLongOrNull()
        val webhookToken = System.getenv("WEBHOOK_TOKEN") ?: System.getProperty("webhookToken")
        if (webhookId == null || webhookToken == null) {
            logger.error("No webhook URL or ID and token provided!")
            return
        }

        Webhook.createBasic(Snowflake(webhookId), webhookToken)
    }

    webhook.send {
        content = "Hello from Disko!"
    }
}
