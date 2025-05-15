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

import dev.deftu.disko.api.utils.Snowflake
import dev.deftu.disko.api.entity.Locale
import dev.deftu.disko.api.entity.channel.Permission
import dev.deftu.disko.api.entity.emoji.Emoji
import dev.deftu.disko.api.entity.emoji.Sticker

public open class Guild(
    public val id: Snowflake,
    public val name: String,
    public val icon: String?,
    public val splash: String?,
    public val discoverySplash: String?,
    public val isOwner: Boolean,
    public val ownerId: Snowflake,
    public val permissions: Set<Permission>,
    public val afkChannelId: Snowflake?,
    public val afkTimeout: Int,
    public val isWidgetEnabled: Boolean,
    public val widgetChannelId: Snowflake?,
    public val verificationLevel: VerificationLevel,
    public val defaultMessageNotifications: DefaultNotificationLevel,
    public val explicitContentFilter: ExplicitContentFilterLevel,
    public val roles: Set<Role>,
    public val emojis: Set<Emoji>,
    public val features: Set<String>,
    public val mfaLevel: GuildMfaLevel,
    public val applicationId: Snowflake?,
    public val systemChannelId: Snowflake?,
    public val systemChannelFlags: Set<SystemChannelFlag>,
    public val rulesChannelId: Snowflake?,
    public val maxPresences: Int?,
    public val maxMembers: Int?,
    public val vanityUrlCode: String?,
    public val description: String?,
    public val banner: String?,
    public val premiumTier: BoostLevel,
    public val premiumSubscriptionCount: Int,
    public val preferredLocale: Locale,
    public val publicUpdatesChannelId: Snowflake?,
    public val maxVideoChannelUsers: Int?,
    public val maxStageVideoChannelUsers: Int?,
    public val approximateMemberCount: Int,
    public val approximatePresenceCount: Int,
    public val welcomeScreen: WelcomeScreen?,
    public val nsfwLevel: NsfwLevel,
    public val stickers: List<Sticker>,
    public val premiumProgressBarEnabled: Boolean,
    public val safetyAlertsChannelId: Snowflake?,
) {

//    public val afkChannel: GuildVoiceChannel?
//        get() = afkChannelId?.let { channelId -> getChannel(channelId).asGuildVoiceChannel() }
//    public val widgetChannel: GuildMessageChannel?
//        get() = widgetChannelId?.let { channelId -> getChannel(channelId).asGuildMessageChannel() }
//    public val systemChannel: GuildMessageChannel?
//        get() = systemChannelId?.let { channelId -> getChannel(channelId).asGuildMessageChannel() }
//    public val rulesChannel: GuildMessageChannel?
//        get() = rulesChannelId?.let { channelId -> getChannel(channelId).asGuildMessageChannel() }
//    public val publicUpdatesChannel: GuildMessageChannel?
//        get() = publicUpdatesChannelId?.let { channelId -> getChannel(channelId).asGuildMessageChannel() }
//    public val safetyAlertsChannel: GuildMessageChannel?
//        get() = safetyAlertsChannelId?.let { channelId -> getChannel(channelId).asGuildMessageChannel() }

//    public val members: List<Member>
//        get() = disko.memberCache.getMembersInGuild(id)
//    public val owner: Member
//        get() = disko.memberCache.getMember(ownerId)!!
//
//    public val channels: List<GuildChannel>
//        get() = disko.channelCache.getChannelsInGuild(id)
//    public val categoryChannels: List<GuildCategoryChannel>
//        get() = channels.filterIsInstance<GuildCategoryChannel>()
//    public val messageChannels: List<GuildMessageChannel>
//        get() = channels.filterIsInstance<GuildMessageChannel>()
//    public val announcementChannels: List<GuildAnnouncementChannel>
//        get() = channels.filterIsInstance<GuildAnnouncementChannel>()
//    public val voiceChannels: List<GuildVoiceChannel>
//        get() = channels.filterIsInstance<GuildVoiceChannel>()
//    public val threadChannels: List<ThreadChannel>
//        get() = channels.filterIsInstance<ThreadChannel>()
//
//    public fun getMember(id: Snowflake): Member? =
//        members.firstOrNull { member -> member.id == id }
//    public fun getMember(user: User): Member? =
//        members.firstOrNull { member -> member.user == user }
//
//    public fun getChannel(id: Snowflake): GuildChannel? =
//        channels.firstOrNull { channel -> channel.id == id }

}
