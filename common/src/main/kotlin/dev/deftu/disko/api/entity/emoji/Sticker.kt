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

package dev.deftu.disko.api.entity.emoji

import dev.deftu.disko.api.entity.user.User
import dev.deftu.disko.api.utils.Snowflake

public data class Sticker(
    public val id: Snowflake,
    public val packId: Snowflake?,
    public val name: String,
    public val description: String?,
    public val tags: Set<String>,
    public val type: StickerType,
    public val formatType: StickerFormat,
    public val available: Boolean,
    public val guildId: Snowflake?,
    public val user: User?,
    public val sortValue: Int?
)
