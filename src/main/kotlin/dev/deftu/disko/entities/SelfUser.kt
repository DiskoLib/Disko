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

package dev.deftu.disko.entities

import dev.deftu.disko.utils.Snowflake

public class SelfUser(
    override val id: Snowflake,
    override val username: String,
    override val discriminator: String,
    avatar: String?,
    override val mfaEnabled: Boolean,
    override val locale: String?,
    override val publicFlags: List<UserFlag>
) : User {
    override val bot: Boolean = true
    override val system: Boolean = false
    override val avatar: String? = avatar
        get() = field ?: "https://cdn.discordapp.com/embed/avatars/${(id.value shr 22) % 6}.png"

}
