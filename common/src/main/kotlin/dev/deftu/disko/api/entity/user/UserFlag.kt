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

package dev.deftu.disko.api.entity.user

import dev.deftu.disko.api.utils.BitsetFlag

public enum class UserFlag(
    override val offset: Int
) : BitsetFlag {

    STAFF(0),
    PARTNER(1),
    HYPESQUAD(2),
    BUG_HUNTER_1(3),
    BUG_HUNTER_2(14),
    HYPESQUAD_BRAVERY(6),
    HYPESQUAD_BRILLIANCE(7),
    HYPESQUAD_BALANCE(8),
    EARLY_SUPPORTER(9),
    VERIFIED_BOT(16),
    VERIFIED_DEVELOPER(17),
    CERTIFIED_MODERATOR(18),
    ACTIVE_DEVELOPER(22);

    public companion object : BitsetFlag.BitsetFlagCompanion<UserFlag> {

        override val values: Array<UserFlag>
            get() = values()

    }

}
