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

package dev.deftu.disko.entity.user

import dev.deftu.disko.entity.Locale
import dev.deftu.disko.utils.Snowflake

public open class User(
    public val id: Snowflake,
    public val username: String,
    public val discriminator: String,
    public val avatar: String?,
    public val isBot: Boolean,
    public val isSystem: Boolean,
    public val mfaEnabled: Boolean,
    public val locale: Locale,
    public val publicFlags: List<UserFlag>
) {

    public fun getAvatarUrl(): String =
        avatar ?: "https://cdn.discordapp.com/embed/avatars/${(id.value shr 22) % 6}.png"

}
