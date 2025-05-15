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

package dev.deftu.disko.api.entity.message

import dev.deftu.disko.api.utils.BitsetFlag

public enum class MessageFlag : BitsetFlag {

    CROSSPOSTED,
    IS_CROSSPOST,
    SUPPRESS_EMBEDS,
    SOURCE_MESSAGE_DELETED,
    URGENT,
    HAS_THREAD,
    EPHEMERAL,
    LOADING,
    FAILED_TO_MENTION_SOME_ROLES_IN_THREAD,
    SUPPRESS_NOTIFICATIONS,
    IS_VOICE_MESSAGE;

    override val offset: Int = ordinal

    public companion object : BitsetFlag.BitsetFlagCompanion<MessageFlag> {

        @Suppress("EnumValuesSoftDeprecate")
        override val values: Array<MessageFlag>
            get() = values()

    }

}
