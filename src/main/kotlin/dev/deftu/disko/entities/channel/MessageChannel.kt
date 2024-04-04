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

package dev.deftu.disko.entities.channel

import dev.deftu.disko.Disko
import dev.deftu.disko.entities.message.Message
import dev.deftu.disko.entities.message.MessageCreateBlock
import dev.deftu.disko.utils.Snowflake
import dev.deftu.disko.utils.botAuth
import dev.deftu.disko.utils.parseJson
import dev.deftu.disko.utils.scheduleAtFixedRate
import kotlinx.coroutines.CoroutineScope
import okhttp3.Request
import java.time.Instant
import java.util.concurrent.TimeUnit

public interface MessageChannel : Channel {
    public companion object {
        public var typingIndicatorRoute: String = "channels/%s/typing"

        public fun getTypingIndicatorRoute(channelId: Snowflake): String =
            typingIndicatorRoute.format(channelId)
    }

    public val name: String
    public var lastMessageId: Snowflake?
    public val lastPinTimestamp: Instant?

    /**
     * Sends a typing indicator to the channel. The indicator will only last for 10 seconds or until a message is sent, if you would like to keep the typing indicator active you will need to call this method every 10 seconds.
     *
     * @since 0.1.0
     * @return Unit
     * @author Deftu
     * @see startTypingContinuously
     */
    public fun startTyping() {
        val request = Request.Builder()
            .url("${disko.discordBaseUrl}/${MessageChannel.getTypingIndicatorRoute(id)}")
            .botAuth(disko)
            .post(Disko.EMPTY_REQUEST_BODY)
            .build()
        disko.httpClient
            .newCall(request)
            .execute()
    }

    /**
     * Sends a typing indicator to the channel. The indicator will last until the returned function is called, or until a message is sent.
     *
     * @since 0.1.0
     * @param interval Long - The amount of time between each request to keep the typing indicator active.
     * @return () -> Unit - A function that when called will stop the typing indicator.
     * @see startTyping
     */
    public fun startTypingContinuously(scope: CoroutineScope, interval: Long = 10_000): () -> Unit {
        val job = scope.scheduleAtFixedRate(
            timeUnit = TimeUnit.MILLISECONDS,
            initialDelay = 0,
            period = interval
        ) {
            startTyping()
        }

        return {
            job.cancel()
        }
    }

    // TODO - public fun send(builder: MessageCreateBuilder): Message

    public fun send(block: MessageCreateBlock.() -> Unit): Message? {
        val messageCreateBlock = MessageCreateBlock()
        block(messageCreateBlock)

        val request = Request.Builder()
            .url("${disko.discordBaseUrl}/channels/$id/messages")
            .botAuth(disko)
            .post(messageCreateBlock.build().createRequestBody())
            .build()
        val response = disko.httpClient
            .newCall(request)
            .execute()
        val body = response.body?.string() ?: return null
        val json = body.parseJson()
        if (!json.isJsonObject) return null

        val message = disko.entityConstructor.constructMessage(json.asJsonObject) ?: return null
        lastMessageId = message.id
        return message
    }
    
    public fun getMessageById(id: Snowflake): Message?

    public fun getMessageHistory(limit: Int): List<Message>

    public fun getMessageHistoryBefore(message: Message, limit: Int): List<Message>

    public fun getMessageHistoryBefore(id: Snowflake, limit: Int): List<Message>

    public fun getMessageHistoryAfter(message: Message, limit: Int): List<Message>

    public fun getMessageHistoryAfter(id: Snowflake, limit: Int): List<Message>
}
