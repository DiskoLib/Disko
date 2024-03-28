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

package dev.deftu.disko.entities.guild

public enum class SystemChannelFlag {
    SUPPRESS_JOIN_NOTIFICATIONS,
    SUPPRESS_PREMIUM_SUBSCRIPTIONS,
    SUPPRESS_GUILD_REMINDER_NOTIFICATIONS,
    SUPPRESS_JOIN_NOTIFICATION_REPLIES,
    SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATIONS,
    SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATION_REPLIES,
    ;

    public companion object {
        public fun from(value: Int): List<SystemChannelFlag> {
            val flags = mutableListOf<SystemChannelFlag>()
            entries.forEach { flag ->
                if ((value and (1 shl flag.ordinal)) != 0) {
                    flags.add(flag)
                }
            }

            return flags
        }
    }
}
