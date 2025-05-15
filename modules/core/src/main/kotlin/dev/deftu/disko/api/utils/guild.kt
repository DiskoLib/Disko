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

package dev.deftu.disko.api.utils

import dev.deftu.disko.api.Disko
import dev.deftu.disko.api.entity.channel.Channel
import dev.deftu.disko.api.entity.guild.Guild
import dev.deftu.disko.api.entity.guild.member.Member
import dev.deftu.disko.api.entity.user.User
import dev.deftu.disko.api.utils.Snowflake.Companion.toSnowflake

public object GuildExtensions {

    // Members

    @JvmStatic
    public fun getMember(instance: Disko, guild: Guild, id: Snowflake): Member? {
        val member = instance.entityCache.getMember(id) ?: return null
        return if (member.guild.id == guild.id) {
            member
        } else {
            null
        }
    }

    @JvmName("-getMember")
    public fun Disko.getMember(guild: Guild, id: Snowflake): Member? {
        return getMember(this, guild, id)
    }

    @JvmName("-getChannel")
    public fun Guild.getMember(instance: Disko, id: Snowflake): Member? {
        return getMember(instance, this, id)
    }

    @JvmStatic
    public fun getMember(instance: Disko, guild: Guild, id: Long): Member? {
        return getMember(instance, guild, id.toSnowflake())
    }

    @JvmName("-getMember")
    public fun Disko.getMember(guild: Guild, id: Long): Member? {
        return getMember(this, guild, id)
    }

    @JvmName("-getChannel")
    public fun Guild.getMember(instance: Disko, id: Long): Member? {
        return getMember(instance, this, id)
    }

    @JvmStatic
    public fun getMember(instance: Disko, guild: Guild, user: User): Member? {
        return getMember(instance, guild, user.id)
    }

    @JvmName("-getMember")
    public fun Disko.getMember(guild: Guild, user: User): Member? {
        return getMember(this, guild, user)
    }

    @JvmName("-getChannel")
    public fun Guild.getMember(instance: Disko, user: User): Member? {
        return getMember(instance, this, user)
    }

    // Channels

    @JvmStatic
    public fun getChannel(instance: Disko, guild: Guild, id: Snowflake): Channel? {
        val channel = instance.entityCache.getChannel(id)?.asGuildChannel() ?: return null
        return if (channel.guild.id == guild.id) {
            channel
        } else {
            null
        }
    }

    @JvmName("-getChannel")
    public fun Disko.getChannel(guild: Guild, id: Snowflake): Channel? {
        return getChannel(this, guild, id)
    }

    @JvmName("-getChannel")
    public fun Guild.getChannel(instance: Disko, id: Snowflake): Channel? {
        return getChannel(instance, this, id)
    }

    @JvmStatic
    public fun getChannel(instance: Disko, guild: Guild, id: Long): Channel? {
        return getChannel(instance, guild, id.toSnowflake())
    }

    @JvmName("-getChannel")
    public fun Disko.getChannel(guild: Guild, id: Long): Channel? {
        return getChannel(this, guild, id)
    }

    @JvmName("-getChannel")
    public fun Guild.getChannel(instance: Disko, id: Long): Channel? {
        return getChannel(instance, this, id)
    }

}
