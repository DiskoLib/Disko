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

import dev.deftu.disko.entities.channel.Channel
import dev.deftu.disko.entities.channel.GuildChannel
import dev.deftu.disko.entities.channel.MessageChannel
import dev.deftu.disko.entities.channel.impl.GuildVoiceChannel
import dev.deftu.disko.utils.Snowflake

public class ChannelCache {
    private val cache: MutableMap<Snowflake, Channel> = mutableMapOf()

    public fun getChannel(id: Snowflake): Channel? =
        cache[id]

    public fun getMessageChannel(id: Snowflake): MessageChannel? =
        cache[id] as? MessageChannel

    public fun getGuildChannel(id: Snowflake): GuildChannel? =
        cache[id] as? GuildChannel

    public fun getChannelsInGuild(guildId: Snowflake): List<GuildChannel> =
        cache.values.filterIsInstance<GuildChannel>().filter { it.guild.id == guildId }

    public fun getGuildVoiceChannel(id: Snowflake): GuildVoiceChannel? =
        cache[id] as? GuildVoiceChannel

    public fun addChannel(channel: Channel) {
        cache[channel.id] = channel
    }

    public fun removeChannel(id: Snowflake) {
        cache.remove(id)
    }

    public fun removeChannel(id: Long) {
        removeChannel(Snowflake(id))
    }
}
