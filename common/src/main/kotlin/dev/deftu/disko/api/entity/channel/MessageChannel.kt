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

package dev.deftu.disko.api.entity.channel

import dev.deftu.disko.api.entity.message.Message
import dev.deftu.disko.api.entity.message.MessageCreate
import dev.deftu.disko.api.utils.Snowflake
import java.time.Instant

public interface MessageChannel : Channel {

    public val name: String
    public var lastMessageId: Snowflake?
    public val lastPinTimestamp: Instant?

    public fun getMessageHistory(limit: Int): Set<Message>

    public fun getMessageHistoryBefore(message: Message, limit: Int): Set<Message>

    public fun getMessageHistoryBefore(id: Snowflake, limit: Int): Set<Message>

    public fun getMessageHistoryAfter(message: Message, limit: Int): Set<Message>

    public fun getMessageHistoryAfter(id: Snowflake, limit: Int): Set<Message>

    public fun send(data: MessageCreate): Message?

}
