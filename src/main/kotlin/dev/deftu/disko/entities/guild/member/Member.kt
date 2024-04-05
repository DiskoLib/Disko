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

package dev.deftu.disko.entities.guild.member

import dev.deftu.disko.entities.MemberFlag
import dev.deftu.disko.entities.Permission
import dev.deftu.disko.entities.User
import dev.deftu.disko.entities.guild.Guild
import dev.deftu.disko.utils.Snowflake
import java.time.Instant

public class Member(
    public val guild: Guild,
    public val user: User,
    public val nickname: String?,
    public val avatar: String?,
    public val roles: List<Role>,
    public val joinedAt: Instant,
    public val premiumSince: Instant?,
    public val deaf: Boolean,
    public val mute: Boolean,
    public val flags: List<MemberFlag>,
    public val pending: Boolean?,
    public val permissions: List<Permission>,
    public val communicationDisabledUntil: Instant?
) {
    public val id: Snowflake
        get() = user.id
    public val isOwner: Boolean
        get() = guild.owner == this
    public val isTimedOut: Boolean
        get() = communicationDisabledUntil?.isAfter(Instant.now()) ?: false

    public fun hasPermission(permission: Permission): Boolean {
        return permissions.contains(permission)
    }
}
