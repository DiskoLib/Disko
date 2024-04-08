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

import com.google.gson.JsonObject
import dev.deftu.disko.entities.channel.Channel
import dev.deftu.disko.entities.channel.PermissionOverwrite
import dev.deftu.disko.entities.channel.VoiceRegion
import dev.deftu.disko.entities.channel.impl.*
import dev.deftu.disko.entities.guild.Guild
import dev.deftu.disko.entities.guild.member.Member
import dev.deftu.disko.entities.guild.WelcomeScreen
import dev.deftu.disko.entities.guild.WelcomeScreenChannel
import dev.deftu.disko.entities.guild.member.Role
import dev.deftu.disko.entities.guild.member.RoleTags
import dev.deftu.disko.entities.message.Message
import dev.deftu.disko.entities.message.MessageEmbed

public interface EntityConstructor {
    public fun constructUser(json: JsonObject): User?
    public fun constructSelfUser(json: JsonObject): SelfUser?

    public fun constructMessage(shardId: Int, json: JsonObject): Message?
    public fun constructMessageEmbed(json: JsonObject): MessageEmbed?
    public fun constructEmbedFooter(json: JsonObject): MessageEmbed.MessageEmbedFooter?
    public fun constructEmbedImage(json: JsonObject): MessageEmbed.MessageEmbedImage?
    public fun constructEmbedThumbnail(json: JsonObject): MessageEmbed.MessageEmbedThumbnail?
    public fun constructEmbedVideo(json: JsonObject): MessageEmbed.MessageEmbedVideo?
    public fun constructEmbedProvider(json: JsonObject): MessageEmbed.MessageEmbedProvider?
    public fun constructEmbedAuthor(json: JsonObject): MessageEmbed.MessageEmbedAuthor?
    public fun constructEmbedField(json: JsonObject): MessageEmbed.MessageEmbedField?

    public fun constructRole(json: JsonObject): Role?
    public fun constructRoleTags(json: JsonObject): RoleTags
    public fun constructMember(
        user: User?,
        guild: Guild?,
        json: JsonObject
    ): Member?

    public fun constructGuild(json: JsonObject): Guild?
    public fun constructGuildWelcomeScreen(json: JsonObject): WelcomeScreen
    public fun constructGuildWelcomeScreenChannel(json: JsonObject): WelcomeScreenChannel?

    public fun constructPermissionOverwrite(json: JsonObject): PermissionOverwrite?
    public fun constructVoiceRegion(json: JsonObject): VoiceRegion?
    public fun constructChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): Channel?
    public fun constructGuildMessageChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): GuildMessageChannel?
    public fun constructDirectMessageChannel(
        shardId: Int,
        json: JsonObject
    ): DirectMessageChannel?
    public fun constructGuildVoiceChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): GuildVoiceChannel?
    public fun constructGroupDirectMessageChannel(
        shardId: Int,
        json: JsonObject
    ): GroupDirectMessageChannel?
    public fun constructGuildCategoryChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): GuildCategoryChannel?
    public fun constructGuildAnnouncementChannel(
        shardId: Int,
        guild: Guild?,
        json: JsonObject
    ): GuildAnnouncementChannel?
}
