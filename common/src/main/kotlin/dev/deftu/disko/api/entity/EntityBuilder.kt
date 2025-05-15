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

package dev.deftu.disko.api.entity

import com.google.gson.JsonObject
import dev.deftu.disko.api.entity.channel.*
import dev.deftu.disko.api.entity.emoji.Emoji
import dev.deftu.disko.api.entity.emoji.Sticker
import dev.deftu.disko.api.entity.guild.*
import dev.deftu.disko.api.entity.guild.member.Member
import dev.deftu.disko.api.entity.message.Message
import dev.deftu.disko.api.entity.message.MessageEmbed
import dev.deftu.disko.api.entity.user.SelfUser
import dev.deftu.disko.api.entity.user.User

public interface EntityBuilder {

    public fun user(obj: JsonObject): User?

    public fun selfUser(obj: JsonObject): SelfUser?

    public fun emoji(obj: JsonObject): Emoji?

    public fun sticker(obj: JsonObject): Sticker?

    public fun voiceRegion(obj: JsonObject): VoiceRegion?

    public fun roleTags(obj: JsonObject): RoleTags

    public fun role(obj: JsonObject): Role?

    public fun permissionOverwrite(obj: JsonObject): PermissionOverwrite?

    public fun channel(guild: Guild?, shardId: Int, obj: JsonObject): Channel?

    public fun guildTextChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildMessageChannel?

    public fun directMessageChannel(shardId: Int, name: String, obj: JsonObject): DirectMessageChannel?

    public fun guildVoiceChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildVoiceChannel?

    public fun groupDirectMessageChannel(shardId: Int, name: String, obj: JsonObject): GroupDirectMessageChannel?

    public fun guildCategoryChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildCategoryChannel?

    public fun guildAnnouncementChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildAnnouncementChannel?

    // TODO - public fun announcementThreadChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): AnnouncementThreadChannel?

    public fun threadChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): ThreadChannel?

    // TODO - public fun guildStageChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildStageChannel?

    // TODO - public fun guildDirectoryChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): GuildDirectoryChannel?

    // TODO - public fun forumChannel(guild: Guild, shardId: Int, name: String, obj: JsonObject): ForumChannel?

    public fun welcomeScreenChannel(obj: JsonObject): WelcomeScreenChannel?

    public fun welcomeScreen(obj: JsonObject): WelcomeScreen

    public fun guild(obj: JsonObject): Guild?

    public fun member(user: User?, guild: Guild, obj: JsonObject): Member?

    public fun message(
        channel: MessageChannel,
        guild: Guild?,
        shardId: Int,
        obj: JsonObject
    ): Message?

    public fun message(guild: Guild?, shardId: Int, obj: JsonObject): Message?

    public fun message(shardId: Int, obj: JsonObject): Message?

    public fun messageEmbed(obj: JsonObject): MessageEmbed

    public fun messageEmbedFooter(obj: JsonObject): MessageEmbed.MessageEmbedFooter?

    public fun messageEmbedImage(obj: JsonObject): MessageEmbed.MessageEmbedImage?

    public fun messageEmbedThumbnail(obj: JsonObject): MessageEmbed.MessageEmbedThumbnail?

    public fun messageEmbedVideo(obj: JsonObject): MessageEmbed.MessageEmbedVideo?

    public fun messageEmbedProvider(obj: JsonObject): MessageEmbed.MessageEmbedProvider?

    public fun messageEmbedAuthor(obj: JsonObject): MessageEmbed.MessageEmbedAuthor?

    public fun messageEmbedField(obj: JsonObject): MessageEmbed.MessageEmbedField?

}
