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
import dev.deftu.disko.entities.Permission
import dev.deftu.disko.utils.Snowflake

public interface PermissionOverwrite {
    public val disko: Disko
    public val id: Snowflake
    public val type: PermissionOverwriteType
    public val allow: List<Permission>
    public val deny: List<Permission>
}

public data class RolePermissionOverwrite(
    override val disko: Disko,
    override val id: Snowflake,
    override val allow: List<Permission>,
    override val deny: List<Permission>
) : PermissionOverwrite {
    override val type: PermissionOverwriteType = PermissionOverwriteType.ROLE

    // TODO
    // public val role: Role
    //     get() = disko.roleCache.get(id)!!
}

public data class MemberPermissionOverwrite(
    override val disko: Disko,
    override val id: Snowflake,
    override val allow: List<Permission>,
    override val deny: List<Permission>
) : PermissionOverwrite {
    override val type: PermissionOverwriteType = PermissionOverwriteType.MEMBER

    // TODO
    // public val member: Member
    //     get() = disko.memberCache.get(id)!!
}

public enum class PermissionOverwriteType {
    ROLE,
    MEMBER;

    public companion object {
        public fun from(value: Any?): PermissionOverwriteType? {
            return when (value) {
                0, "role" -> ROLE
                1, "member" -> MEMBER
                else -> null
            }
        }
    }
}
