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

import dev.deftu.disko.entities.User
import dev.deftu.disko.entities.channel.Channel
import dev.deftu.disko.utils.Snowflake
import java.time.Instant

public data class Message(
    public val id: Snowflake,
    public val channel: Channel,
    public val author: User,
    public val content: String,
    public val timestamp: Instant,
    public val editedTimestamp: Instant?,
    public val tts: Boolean,
    public val mentionEveryone: Boolean,
    public val mentions: List<User>,
    // TODO - public val mentionRoles: List<Role>,
    public val mentionChannels: List<Channel>,
    // TODO - public val attachments: List<Attachment>,
    // TODO - public val embeds: List<Embed>,
    // TODO - public val reactions: List<Reaction>,
    public val nonce: String?,
    public val pinned: Boolean,
    // TODO - public val webhookId: Snowflake?,
    public val type: MessageType,
    // TODO - public val activity: MessageActivity?,
    // TODO - public val application: MessageApplication?,
    // TODO - public val messageReference: MessageReference?,
    public val flags: List<MessageFlag>,
    public val referencedMessage: Message?,
    // TODO - public val interactionMetadata: MessageInteractionMetadata?,
    // TODO - public val thread: ThreadChannel?,
    // TODO - public val components: List<Component>?,
    // TODO - public val stickerItems: List<StickerItem>?,
    public val approximatePosition: Int?
) {
}
