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

package dev.deftu.disko.cache

import dev.deftu.disko.Disko
import dev.deftu.disko.DiskoConstants
import dev.deftu.disko.entities.User
import dev.deftu.disko.entities.channel.Channel
import dev.deftu.disko.entities.channel.Channel.Companion.asDirectMessageChannel
import dev.deftu.disko.entities.channel.Channel.Companion.asGuildChannel
import dev.deftu.disko.entities.channel.GuildChannel
import dev.deftu.disko.entities.guild.Guild
import dev.deftu.disko.utils.Snowflake
import dev.deftu.disko.utils.botAuth
import dev.deftu.disko.utils.parseJson
import okhttp3.Request
import org.slf4j.LoggerFactory

public class ChannelCache(
    private val instance: Disko
) {
    private companion object {
        private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Channel Cache")

        const val ID_INDEX = "id"
        const val GUILD_INDEX = "guild"
        const val USER_INDEX = "user"
    }

    private val cache = Cache<Channel>()
        .createIndex(ID_INDEX) {
            this.id
        }.createIndex(GUILD_INDEX) {
            this.asGuildChannel()
                ?.guild
                ?.id
        }.createIndex(USER_INDEX) {
            this.asDirectMessageChannel()
                ?.recipients
                ?.map(User::id)
        }

    public fun getChannel(id: Snowflake): Channel? =
        cache.findFirstByIndex(ID_INDEX, id)

    public fun getChannelOrPopulate(
        shardId: Int,
        guild: Guild?,
        id: Snowflake
    ): Channel? {
        val channel = getChannel(id)
        if (channel == null) {
            logger.debug("Channel {} not found in cache, populating from API", id)

            val request = Request.Builder()
                .url("${instance.baseUrl}/channels/$id")
                .botAuth(instance)
                .build()
            val response = instance.httpClient
                .newCall(request)
                .execute()
            val body = response.body?.string() ?: return null
            val json = body.parseJson()
            if (!json.isJsonObject) return null

            val newChannel = instance.entityConstructor.constructChannel(
                shardId,
                guild,
                json.asJsonObject
            ) ?: return null
            logger.debug("Populated channel {} from API", id)
            addChannel(newChannel)
        }

        return getChannel(id)
    }

    public fun getChannelsInGuild(guildId: Snowflake): List<GuildChannel> =
        cache.findByIndex(GUILD_INDEX, guildId).map { channel -> channel.asGuildChannel()!! }

    public fun addChannel(channel: Channel) {
        cache.add(channel)
    }

    public fun removeChannel(channel: Channel) {
        cache.remove(channel)
    }
}
