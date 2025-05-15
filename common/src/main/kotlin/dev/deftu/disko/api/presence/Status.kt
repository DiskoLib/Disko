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

/**
 * Enumerable values for a user's possible online status
 *
 * @param value The status of the user
 * @param description The description of the status
 *
 * @since 0.1.0
 * @author Deftu
 */
public enum class Status(
    public val value: String,
    public val description: String
) {

    ONLINE("online", "Online"),
    DND("dnd", "Do Not Disturb"),
    IDLE("idle", "Idle"),
    INVISIBLE("invisible", "Invisible"),
    OFFLINE("offline", "Offline");

    public companion object {

        @JvmStatic
        public fun from(value: String): Status? {
            return values().find { status ->
                status.value == value
            }
        }

    }

}
