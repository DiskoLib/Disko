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

package dev.deftu.disko.entities

public enum class Permission(
    public val value: Int
) {
    CREATE_INSTANT_INVITE(0),
    KICK_MEMBERS(1),
    BAN_MEMBERS(2),
    ADMINISTRATOR(3),
    MANAGE_CHANNELS(4),
    MANAGE_GUILD(5),
    ADD_REACTIONS(6),
    VIEW_AUDIT_LOG(7),
    PRIORITY_SPEAKER(8),
    STREAM(9),
    VIEW_CHANNEL(10),
    SEND_MESSAGES(11),
    SEND_TTS_MESSAGES(12),
    MANAGE_MESSAGES(13),
    EMBED_LINKS(14),
    ATTACH_FILES(15),
    READ_MESSAGE_HISTORY(16),
    MENTION_EVERYONE(17),
    USE_EXTERNAL_EMOJIS(18),
    VIEW_GUILD_INSIGHTS(19),
    CONNECT(20),
    SPEAK(21),
    MUTE_MEMBERS(22),
    DEAFEN_MEMBERS(23),
    MOVE_MEMBERS(24),
    USE_VAD(25),
    CHANGE_NICKNAME(26),
    MANAGE_NICKNAMES(27),
    MANAGE_ROLES(28),
    MANAGE_WEBHOOKS(29),
    MANAGE_GUILD_EXPRESSIONS(30),
    USE_SLASH_COMMANDS(31),
    REQUEST_TO_SPEAK(32),
    MANAGE_EVENTS(33),
    MANAGE_THREADS(34),
    CREATE_PUBLIC_THREADS(35),
    CREATE_PRIVATE_THREADS(36),
    USE_EXTERNAL_STICKERS(37),
    SEND_MESSAGES_IN_THREADS(38),
    START_EMBEDDED_ACTIVITIES(39),
    MODERATE_MEMBERS(40),
    VIEW_CREATOR_MONETIZATION_ANALYTICS(41),
    USE_SOUNDBOARD(42),
    CREATE_GUILD_EXPRESSIONS(43),
    CREATE_EVENTS(44),
    USE_EXTERNAL_SOUNDS(45),
    SEND_VOICE_MESSAGES(46),
    ;

    public companion object {
        public fun fromSingleBit(value: Int): Permission? {
            return entries.firstOrNull { permission ->
                value and (1 shl permission.value) != 0
            }
        }

        public fun from(value: Int): List<Permission> {
            val permissions = mutableListOf<Permission>()
            entries.forEach {
                if (value and (1 shl it.value) != 0) {
                    permissions.add(it)
                }
            }

            return permissions
        }
    }
}
