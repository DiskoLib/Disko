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

package dev.deftu.disko.impl.entity.channel

import dev.deftu.disko.api.Disko
import dev.deftu.disko.api.entity.channel.MessageChannel
import dev.deftu.disko.api.entity.guild.Guild
import dev.deftu.disko.api.entity.message.Message
import dev.deftu.disko.api.entity.message.MessageCreate
import dev.deftu.disko.api.utils.ApiVersion
import dev.deftu.disko.api.utils.authorized
import dev.deftu.disko.api.utils.parseJson
import okhttp3.Request

public abstract class MessageChannelImpl(
    private val instance: Disko,
    private val guild: Guild? = null
) : MessageChannel {

    override fun send(data: MessageCreate): Message? {
        val request = Request.Builder()
            .url("https://discord.com/api/${ApiVersion.latest.formatted}/channels/$id/messages")
            .authorized(instance.token)
            .post(data.createRequestBody())
            .build()
        val response = instance.httpClient
            .newCall(request)
            .execute()
        val body = response.body?.string()
        if (body == null || response.code != 200) {
            response.close()
            return null
        }

        val json = body.parseJson()
        if (!json.isJsonObject) return null

        val message = instance.entityBuilder.message(guild, shardId, json.asJsonObject) ?: return null
        lastMessageId = message.id
        return message
    }

}
