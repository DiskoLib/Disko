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

package dev.deftu.disko.api.entity.channel

import dev.deftu.disko.api.utils.Snowflake

public interface PermissionOverwrite {

    public val id: Snowflake
    public val type: PermissionOverwriteType
    public val allow: Set<Permission>
    public val deny: Set<Permission>

}

public data class RolePermissionOverwrite(
    override val id: Snowflake,
    override val allow: Set<Permission>,
    override val deny: Set<Permission>
) : PermissionOverwrite {

    override val type: PermissionOverwriteType = PermissionOverwriteType.ROLE

}

public data class MemberPermissionOverwrite(
    override val id: Snowflake,
    override val allow: Set<Permission>,
    override val deny: Set<Permission>
) : PermissionOverwrite {

    override val type: PermissionOverwriteType = PermissionOverwriteType.MEMBER

}

public enum class PermissionOverwriteType(public val identifiers: Set<Any>) {

    ROLE(0, "role"),
    MEMBER(1, "member");

    constructor(vararg identifiers: Any) : this(identifiers.toSet())

    public companion object {

        public fun from(value: Any?): PermissionOverwriteType? {
            return values().firstOrNull { type ->
                type.identifiers.contains(value)
            }

        }
    }

}
