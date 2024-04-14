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

package dev.deftu.disko.gateway

import dev.deftu.disko.utils.BitsetFlag

public enum class GatewayIntent(
    override val offset: Int,
    public val isPrivileged: Boolean = false
) : BitsetFlag {
    GUILDS(0),
    GUILD_MEMBERS(1, true),
    GUILD_MODERATION(2),
    GUILD_EMOJIS_AND_STICKERS(3),
    GUILD_INTEGRATIONS(4),
    GUILD_WEBHOOKS(5),
    GUILD_INVITES(6),
    GUILD_VOICE_STATES(7),
    GUILD_PRESENCES(8, true),
    GUILD_MESSAGES(9),
    GUILD_MESSAGE_REACTIONS(10),
    GUILD_MESSAGE_TYPING(11),
    GUILD_SCHEDULED_EVENTS(16),

    AUTO_MODERATION_CONFIGURATION(20),
    AUTO_MODERATION_EXECUTION(21),

    DIRECT_MESSAGES(12),
    DIRECT_MESSAGE_REACTIONS(13),
    DIRECT_MESSAGE_TYPING(14),

    MESSAGE_CONTENT(15, true);

    public companion object : BitsetFlag.BitsetFlagCompanion<GatewayIntent> {
        override val values: Array<GatewayIntent>
            get() = @Suppress("EnumValuesSoftDeprecate") values()

        public val all: List<GatewayIntent> by lazy { values.toList() }
        public val nonPrivileged: List<GatewayIntent> by lazy { values.filter { !it.isPrivileged } }
        public val privileged: List<GatewayIntent> by lazy { values.filter { it.isPrivileged } }
    }
}
