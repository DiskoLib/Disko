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

import dev.deftu.disko.entities.User
import dev.deftu.disko.utils.Snowflake

public class UserCache {
    private val cache: MutableMap<Snowflake, User> = mutableMapOf()

    public fun getUser(id: Snowflake): User? =
        cache[id]

    public fun getUser(id: Long): User? =
        getUser(Snowflake(id))

    public fun addUser(user: User) {
        cache[user.id] = user
    }

    public fun removeUser(id: Snowflake) {
        cache.remove(id)
    }

    public fun removeUser(id: Long) {
        removeUser(Snowflake(id))
    }
}