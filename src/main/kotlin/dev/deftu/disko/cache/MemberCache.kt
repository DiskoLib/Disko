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

import dev.deftu.disko.entities.Member
import dev.deftu.disko.utils.Snowflake

public class MemberCache {
    private val cache: MutableMap<Snowflake, Member> = mutableMapOf()

    public fun getMember(id: Snowflake): Member? =
        cache[id]

    public fun getMember(id: Long): Member? =
        getMember(Snowflake(id))

    public fun addMember(member: Member) {
        cache[member.user.id] = member
    }

    public fun removeMember(id: Snowflake) {
        cache.remove(id)
    }

    public fun removeMember(id: Long) {
        removeMember(Snowflake(id))
    }
}
