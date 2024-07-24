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

package dev.deftu.disko.entity.message

import dev.deftu.disko.utils.ApiVersion

public enum class MessageType(
    public val id: Int,
    public val isDeletable: Boolean,
    public val minimumApiVersion: ApiVersion
) {
    DEFAULT(0, true, ApiVersion.V6),
    RECIPIENT_ADD(1, false, ApiVersion.V6),
    RECIPIENT_REMOVE(2, false, ApiVersion.V6),
    CALL(3, false, ApiVersion.V6),
    CHANNEL_NAME_CHANGE(4, false, ApiVersion.V6),
    CHANNEL_ICON_CHANGE(5, false, ApiVersion.V6),
    CHANNEL_PINNED_MESSAGE(6, true, ApiVersion.V6),
    USER_JOIN(7, true, ApiVersion.V6),
    GUILD_BOOST(8, true, ApiVersion.V6),
    GUILD_BOOST_TIER_1(9, true, ApiVersion.V6),
    GUILD_BOOST_TIER_2(10, true, ApiVersion.V6),
    GUILD_BOOST_TIER_3(11, true, ApiVersion.V6),
    CHANNEL_FOLLOW_ADD(12, true, ApiVersion.V6),
    GUILD_DISCOVERY_DISQUALIFIED(14, false, ApiVersion.V6),
    GUILD_DISCOVERY_REQUALIFIED(15, false, ApiVersion.V6),
    GUILD_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING(16, false, ApiVersion.V6),
    GUILD_DISCOVERY_GRACE_PERIOD_FINAL_WARNING(17, false, ApiVersion.V6),
    THREAD_CREATED(18, true, ApiVersion.V6),
    REPLY(19, true, ApiVersion.V8),
    CHAT_INPUT_COMMAND(20, true, ApiVersion.V8),
    THREAD_STARTER_MESSAGE(21, false, ApiVersion.V9),
    GUILD_INVITE_REMINDER(22, true, ApiVersion.V9),
    CONTEXT_MENU_COMMAND(23, true, ApiVersion.V9),
    AUTO_MODERATION_ACTION(24, true, ApiVersion.V9),
    ROLE_SUBSCRIPTION_PURCHASE(25, true, ApiVersion.V9),
    INTERACTION_PREMIUM_UPSELL(26, true, ApiVersion.V9),
    STAGE_START(27, true, ApiVersion.V9),
    STAGE_END(28, true, ApiVersion.V9),
    STAGE_SPEAKER(29, true, ApiVersion.V9),
    STAGE_TOPIC(31, true, ApiVersion.V9),
    GUILD_APPLICATION_PREMIUM_SUBSCRIPTION(32, false, ApiVersion.V9);

    public companion object {
        public fun from(value: Int): MessageType? =
            entries.firstOrNull { it.id == value }
    }
}
