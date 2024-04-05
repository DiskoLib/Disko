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

import dev.deftu.disko.entities.guild.member.Member
import dev.deftu.disko.entities.guild.Guild
import dev.deftu.disko.utils.Snowflake

public class GuildCache {
    private companion object {
        const val ID_INDEX = "id"
        const val MEMBER_INDEX = "member"
    }

    private val cache = Cache<Guild>()
        .createIndex(ID_INDEX) {
            this.id
        }.createIndex(MEMBER_INDEX) {
            this.members
                .map(Member::id)
        }

    public fun getGuild(id: Snowflake): Guild? =
        cache.findFirstByIndex(ID_INDEX, id)

    public fun getGuildByMember(id: Snowflake): Guild? =
        cache.findFirstByIndex(MEMBER_INDEX, id)

    public fun addGuild(guild: Guild) {
        cache.add(guild)
    }

    public fun removeGuild(guild: Guild) {
        cache.remove(guild)
    }
}
