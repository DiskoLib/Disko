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

package dev.deftu.disko.impl.entity

import dev.deftu.disko.api.entity.VoiceRegion
import dev.deftu.disko.api.entity.channel.Channel
import dev.deftu.disko.api.entity.channel.ChannelType
import dev.deftu.disko.api.entity.guild.Guild
import dev.deftu.disko.api.entity.guild.Role
import dev.deftu.disko.api.entity.guild.member.Member
import dev.deftu.disko.api.entity.user.User
import dev.deftu.disko.api.utils.Snowflake
import dev.deftu.disko.impl.utils.Cache

public class EntityCache {

    private companion object {

        // Global
        private const val ID_INDEX = "id"

        // Member
        private const val GUILD_INDEX = "guild"

        // Channel
        private const val CHANNEL_TYPE_INDEX = "type"

    }

    private val voiceRegionCache = Cache<VoiceRegion>()
        .createIndex(ID_INDEX, VoiceRegion::id)

    private val userCache = Cache<User>()
        .createIndex(ID_INDEX, User::id)

    private val roleCache = Cache<Role>()
        .createIndex(ID_INDEX, Role::id)

    private val memberCache = Cache<Member>()
        .createIndex(ID_INDEX, Member::id)
        .createIndex(GUILD_INDEX) { this.guild.id }

    private val guildCache = Cache<Guild>()
        .createIndex(ID_INDEX, Guild::id)

    private val channelCache = Cache<Channel>()
        .createIndex(ID_INDEX, Channel::id)
        .createIndex(CHANNEL_TYPE_INDEX, Channel::type)

    public fun getVoiceRegion(id: String): VoiceRegion? {
        return voiceRegionCache.findFirstByIndex(ID_INDEX, id)
    }

    public fun addVoiceRegion(voiceRegion: VoiceRegion) {
        voiceRegionCache.add(voiceRegion)
    }

    public fun removeVoiceRegion(voiceRegion: VoiceRegion) {
        voiceRegionCache.remove(voiceRegion)
    }

    public fun clearVoiceRegions() {
        voiceRegionCache.clear()
    }

    public fun getUser(id: Snowflake): User? {
        return userCache.findFirstByIndex(ID_INDEX, id)
    }

    public fun addUser(user: User) {
        userCache.add(user)
    }

    public fun removeUser(user: User) {
        userCache.remove(user)
    }

    public fun getRole(id: Snowflake): Role? {
        return roleCache.findFirstByIndex(ID_INDEX, id)
    }

    public fun addRole(role: Role) {
        roleCache.add(role)
    }

    public fun removeRole(role: Role) {
        roleCache.remove(role)
    }

    public fun getMember(id: Snowflake): Member? {
        return memberCache.findFirstByIndex(ID_INDEX, id)
    }

    public fun getMembersByGuild(guild: Guild): Set<Member> {
        return memberCache.findByIndex(GUILD_INDEX, guild.id)
    }

    public fun addMember(member: Member) {
        memberCache.add(member)
    }

    public fun removeMember(member: Member) {
        memberCache.remove(member)
    }

    public fun getGuild(id: Snowflake): Guild? {
        return guildCache.findFirstByIndex(ID_INDEX, id)
    }

    public fun addGuild(guild: Guild) {
        guildCache.add(guild)
    }

    public fun removeGuild(guild: Guild) {
        guildCache.remove(guild)
    }

    public fun getChannel(id: Snowflake): Channel? {
        return channelCache.findFirstByIndex(ID_INDEX, id)
    }

    public fun getChannelsByType(type: ChannelType): Set<Channel> {
        return channelCache.findByIndex(CHANNEL_TYPE_INDEX, type)
    }

    public fun addChannel(channel: Channel) {
        channelCache.add(channel)
    }

    public fun removeChannel(channel: Channel) {
        channelCache.remove(channel)
    }

}
