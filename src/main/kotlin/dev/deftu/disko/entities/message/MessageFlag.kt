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

package dev.deftu.disko.entities.message

public enum class MessageFlag {
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

    public companion object {
        public fun from(value: Int): List<MessageFlag> =
            entries.filter { flag -> (value and (1 shl flag.ordinal)) != 0 }
    }
}
