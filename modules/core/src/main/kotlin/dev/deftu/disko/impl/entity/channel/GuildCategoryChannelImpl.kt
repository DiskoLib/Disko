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

package dev.deftu.disko.impl.entity.channel

import dev.deftu.disko.api.Disko
import dev.deftu.disko.api.entity.channel.*
import dev.deftu.disko.api.entity.guild.Guild
import dev.deftu.disko.api.entity.user.User
import dev.deftu.disko.api.utils.GuildExtensions.getMember
import dev.deftu.disko.api.utils.Snowflake

public class GuildCategoryChannelImpl(
    override val shardId: Int,
    override val guild: Guild,
    override val position: Int?,
    override val permissionOverwrites: Set<PermissionOverwrite>,
    override val topic: String,
    override val isNsfw: Boolean,
    override val rateLimitPerUser: Int,
    override val parent: GuildChannel?,
    override val id: Snowflake,
    override val type: ChannelType,
    override val name: String,
    public val instance: Disko
) : GuildCategoryChannel {

    override fun isVisibleTo(user: User): Boolean {
        return guild.getMember(instance, user)?.hasPermission(Permission.VIEW_CHANNEL) ?: false
    }

}
