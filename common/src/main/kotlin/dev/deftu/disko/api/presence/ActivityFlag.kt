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

package dev.deftu.disko.api.presence

import dev.deftu.disko.api.utils.BitsetFlag

public enum class ActivityFlag(override val offset: Int) : BitsetFlag {

    INSTANCE(0),
    JOIN(1),
    SPECTATE(2),
    JOIN_REQUEST(3),
    SYNC(4),
    PLAY(5),
    PARTY_PRIVACY_FRIENDS(6),
    PARTY_PRIVACY_VOICE_CHANNEL(7),
    EMBEDDED(8);

    public companion object : BitsetFlag.BitsetFlagCompanion<ActivityFlag> {

        override val values: Array<ActivityFlag>
            get() = @Suppress("EnumValuesSoftDeprecate") values()

    }

}
