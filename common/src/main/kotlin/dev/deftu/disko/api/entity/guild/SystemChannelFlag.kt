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

package dev.deftu.disko.api.entity.guild

import dev.deftu.disko.api.utils.BitsetFlag

public enum class SystemChannelFlag : BitsetFlag {
    SUPPRESS_JOIN_NOTIFICATIONS,
    SUPPRESS_PREMIUM_SUBSCRIPTIONS,
    SUPPRESS_GUILD_REMINDER_NOTIFICATIONS,
    SUPPRESS_JOIN_NOTIFICATION_REPLIES,
    SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATIONS,
    SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATION_REPLIES,;

    override val offset: Int = ordinal

    public companion object : BitsetFlag.BitsetFlagCompanion<SystemChannelFlag> {

        override val values: Array<SystemChannelFlag>
            get() = values()

    }
}
