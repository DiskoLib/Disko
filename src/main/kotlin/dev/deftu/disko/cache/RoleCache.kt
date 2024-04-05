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

import dev.deftu.disko.entities.guild.member.Role
import dev.deftu.disko.utils.Snowflake

public class RoleCache {
    private companion object {
        const val ID_INDEX = "id"
    }

    private val cache = Cache<Role>()
        .createIndex(ID_INDEX) {
            this.id
        }

    public fun getRole(id: Snowflake): Role? =
        cache.findFirstByIndex(ID_INDEX, id)

    public fun addRole(role: Role) {
        cache.add(role)
    }

    public fun removeRole(role: Role) {
        cache.remove(role)
    }
}
