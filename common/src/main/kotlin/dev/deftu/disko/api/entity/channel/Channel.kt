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

import dev.deftu.disko.api.entity.user.User
import dev.deftu.disko.api.utils.Snowflake

public interface Channel {

    public val shardId: Int

    public val id: Snowflake
    public val type: ChannelType

    public fun asOwnedChannel(): OwnedChannel? {
        return this as? OwnedChannel
    }

    public fun asNamedChannel(): NamedChannel? {
        return this as? NamedChannel
    }

    public fun asMessageChannel(): MessageChannel? {
        return this as? MessageChannel
    }

    public fun asGuildChannel(): GuildChannel? {
        return this as? GuildChannel
    }

    public fun asDirectMessageChannel(): DirectMessageChannel? {
        return this as? DirectMessageChannel
    }

    public fun asGroupDirectMessageChannel(): GroupDirectMessageChannel? {
        return this as? GroupDirectMessageChannel
    }

    public fun asGuildMessageChannel(): GuildMessageChannel? {
        return this as? GuildMessageChannel
    }

    public fun asGuildVoiceChannel(): GuildVoiceChannel? {
        return this as? GuildVoiceChannel
    }

    public fun asGuildCategoryChannel(): GuildCategoryChannel? {
        return this as? GuildCategoryChannel
    }

    public fun asGuildAnnouncementChannel(): GuildAnnouncementChannel? {
        return this as? GuildAnnouncementChannel
    }

    public fun asThreadChannel(): ThreadChannel? {
        return this as? ThreadChannel
    }

    public fun isVisibleTo(user: User): Boolean

}
