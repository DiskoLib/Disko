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

import dev.deftu.disko.api.entity.channel.Channel
import dev.deftu.disko.api.entity.channel.MessageChannel
import dev.deftu.disko.api.entity.channel.ThreadChannel
import dev.deftu.disko.api.entity.guild.Guild
import dev.deftu.disko.api.entity.guild.Role
import dev.deftu.disko.api.entity.guild.member.Member
import dev.deftu.disko.api.entity.user.User
import dev.deftu.disko.api.utils.*
import java.time.Instant

public data class Message(
    public val id: Snowflake,
    public val channel: MessageChannel,
    public val author: User,
    public val content: String,
    public val timestamp: Instant,
    public val editedTimestamp: Instant?,
    public val tts: Boolean,
    public val mentionEveryone: Boolean,
    public val mentions: Set<User>,
    public val mentionRoles: Set<Role>,
    public val mentionChannels: Set<Channel>,
    // TODO - public val attachments: Set<Attachment>,
    public val embeds: Set<MessageEmbed>,
    // TODO - public val reactions: Set<Reaction>,
    public val nonce: String?,
    public val pinned: Boolean,
    public val webhookId: Snowflake?,
    public val type: MessageType,
    // TODO - public val activity: MessageActivity?,
    // TODO - public val application: MessageApplication?,
    // TODO - public val messageReference: MessageReference?,
    public val flags: Set<MessageFlag>,
    public val referencedMessage: Message?,
    // TODO - public val interactionMetadata: MessageInteractionMetadata?,
    public val thread: ThreadChannel?,
    // TODO - public val components: Set<Component>?,
    // TODO - public val stickerItems: Set<StickerItem>?,
    public val approximatePosition: Int?,
    public val guild: Guild?,
    public val member: Member?
)
