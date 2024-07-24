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

package dev.deftu.disko.webhook

import dev.deftu.disko.entity.message.Message
import dev.deftu.disko.utils.ApiVersion
import dev.deftu.disko.utils.Snowflake
import dev.deftu.disko.utils.parseJson
import okhttp3.OkHttpClient
import okhttp3.Request

public data class Webhook(
    public val id: Snowflake,
    public val type: WebhookType,
    public val guildId: Snowflake?,
    public val channelId: Snowflake?,
    // TODO - user,
    public val name: String?,
    public val avatar: String?,
    public val token: String?,
    public val applicationId: Snowflake?,
    // TODO - sourceGuild,
    // TODO - sourceChannel
    public val url: String?
) {

    public companion object {

        private val webhookUrlRegex = "https://discord.com/api/webhooks/(?<id>\\d+)/(?<token>[a-zA-Z0-9_-]+)".toRegex()

        private val baseUrl: String
            get() = "https://discord.com/api/${ApiVersion.V10.formatted}"

        @JvmStatic
        public fun createBasic(
            id: Snowflake,
            token: String
        ): Webhook {
            return Webhook(
                id = id,
                type = WebhookType.INCOMING,
                guildId = null,
                channelId = null,
                name = null,
                avatar = null,
                token = token,
                applicationId = null,
                url = null
            )
        }

        @JvmStatic
        public fun createFromUrl(url: String): Webhook {
            val match = webhookUrlRegex.matchEntire(url) ?: throw IllegalArgumentException("Invalid webhook URL.")
            val matchId = match.groups["id"]?.value ?: throw IllegalArgumentException("Invalid webhook URL. (ID)")
            val matchToken = match.groups["token"]?.value ?: throw IllegalArgumentException("Invalid webhook URL. (Token)")
            val id = Snowflake(matchId.toLong())
            return createBasic(id, matchToken)
        }

    }

    public fun send(client: OkHttpClient, data: WebhookMessageCreate): Message? {
        val request = Request.Builder()
            .url("$baseUrl/webhooks/${id.value}/${token}")
            .post(data.createRequestBody())
            .build()
        val response = client
            .newCall(request)
            .execute()
        val body = response.body?.string()
        if (body == null) {
            response.close()
            return null
        }

        val json = body.parseJson()
        if (!json.isJsonObject) {
            return null
        }

        return Message.parseJson(json.asJsonObject)
    }

    public fun send(data: WebhookMessageCreate): Message? {
        return send(OkHttpClient(), data)
    }

}
