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

import com.google.gson.JsonObject
import dev.deftu.disko.entity.channel.Channel
import dev.deftu.disko.entity.user.User
import dev.deftu.disko.utils.*
import java.time.Instant

public data class Message(
    public val id: Snowflake,
//    public val channel: Channel,
//    public val author: User,
    public val content: String,
    public val timestamp: Instant,
    public val editedTimestamp: Instant?,
    public val tts: Boolean,
    public val mentionEveryone: Boolean,
//    public val mentions: List<User>,
    // TODO - public val mentionRoles: List<Role>,
//    public val mentionChannels: List<Channel>,
    // TODO - public val attachments: List<Attachment>,
    public val embeds: List<MessageEmbed>,
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
    public val approximatePosition: Int?,
//    public val guild: Guild?,
//    public val member: Member?
) {
    
    public companion object {
        
        @JvmStatic
        public fun parseJson(shardId: Int, obj: JsonObject): Message? {
            val id = obj.maybeGetSnowflake("id") ?: return null
            val channelId = obj.maybeGetSnowflake("channel_id") ?: return null
//            val channel = disko.channelCache.getChannelOrPopulate(shardId, null, channelId).asMessageChannel() ?: return null
//            val author = constructUser(obj.maybeGetJsonObject("author") ?: return null) ?: return null
            val content = obj.maybeGetString("content") ?: ""
            val timestamp = obj.maybeGetString("timestamp")?.let { Instant.parse(it) } ?: return null
            val editedTimestamp = obj.maybeGetString("edited_timestamp")?.let { Instant.parse(it) }
            val tts = obj.maybeGetBoolean("tts") ?: false
            val mentionEveryone = obj.maybeGetBoolean("mention_everyone") ?: false
//            val mentions = obj.get("mentions")?.asJsonArray?.mapNotNull { constructUser(it.asJsonObject) } ?: emptyList()
            //val mentionRoles = obj.get("mention_roles")?.asJsonArray?.map { it.asString } ?: emptyList()
//            val mentionChannels = obj.get("mention_channels")?.asJsonArray?.mapNotNull { constructChannel(0, null, it.asJsonObject) } ?: emptyList()
            //val attachments = obj.get("attachments")?.asJsonArray?.map { constructAttachment(it.asJsonObject) } ?: emptyList()
            val embeds = obj.get("embeds")?.asJsonArray?.mapNotNull { MessageEmbed.parseJson(it.asJsonObject) } ?: emptyList()
            //val reactions = obj.get("reactions")?.asJsonArray?.map { constructReaction(it.asJsonObject) } ?: emptyList()
            val nonce = obj.maybeGetString("nonce")
            val pinned = obj.maybeGetBoolean("pinned") ?: false
            //val webhookId = obj.maybeGetSnowflake("webhook_id")
            val type = MessageType.from(obj.maybeGetInteger("type") ?: 0) ?: MessageType.DEFAULT
            //val activity = obj.maybeGetJsonObject("activity")?.let { constructMessageActivity(it) }
            //val application = obj.maybeGetJsonObject("application")?.let { constructMessageApplication(it) }
            //val messageReference = obj.maybeGetJsonObject("message_reference")?.let { constructMessageReference(it) }
            val flags = MessageFlag.fromBitset(obj.maybeGetInteger("flags") ?: 0)
            val guildId = obj.maybeGetSnowflake("guild_id")
//            val guild = if (guildId != null) disko.guildCache.getGuild(guildId) else null
//            val user = obj.maybeGetJsonObject("author")?.let { constructUser(it) } // TODO - Support webhook messages
//            val member = obj.maybeGetJsonObject("member")?.let { constructMember(user, null, it) }

            return Message(
                id,
//                channel,
//                author,
                content,
                timestamp,
                editedTimestamp,
                tts,
                mentionEveryone,
//                mentions,
                //mentionRoles,
//                mentionChannels,
                //attachments,
                embeds,
                //reactions,
                nonce,
                pinned,
                //webhookId,
                type,
                //activity,
                //application,
                //messageReference,
                flags,
                null,
                //interactionMetadata,
                //thread,
                //components,
                //stickerItems,
                null,
//                guild,
//                member
            )
        }

        @JvmStatic
        public fun parseJson(obj: JsonObject): Message? {
            return parseJson(0, obj)
        }
        
    }
    
}
