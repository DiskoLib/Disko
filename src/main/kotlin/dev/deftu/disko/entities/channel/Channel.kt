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

package dev.deftu.disko.entities.channel

import dev.deftu.disko.Disko
import dev.deftu.disko.entities.User
import dev.deftu.disko.entities.channel.impl.*
import dev.deftu.disko.utils.Snowflake
import java.util.Optional

public interface Channel {
    public companion object {
        public fun Channel?.asMessageChannel(): MessageChannel? = this as? MessageChannel
        public fun Channel?.asGuildChannel(): GuildChannel? = this as? GuildChannel
        public fun Channel?.asGuildMessageChannel(): GuildMessageChannel? = this as? GuildMessageChannel
        public fun Channel?.asDirectMessageChannel(): DirectMessageChannel? = this as? DirectMessageChannel
        public fun Channel?.asGuildVoiceChannel(): GuildVoiceChannel? = this as? GuildVoiceChannel
        public fun Channel?.asGroupDirectMessageChannel(): GroupDirectMessageChannel? = this as? GroupDirectMessageChannel
        public fun Channel?.asGuildCategoryChannel(): GuildCategoryChannel? = this as? GuildCategoryChannel
        public fun Channel?.asGuildAnnouncementChannel(): GuildAnnouncementChannel? = this as? GuildAnnouncementChannel
        // TODO - threads, stages, etc.
    }

    public val disko: Disko
    public val shardId: Int

    public val id: Snowflake
    public val type: ChannelType

    public fun maybeAsGuildMessageChannel(): Optional<GuildMessageChannel> = Optional.ofNullable(this.asGuildMessageChannel())
    public fun maybeAsDirectMessageChannel(): Optional<DirectMessageChannel> = Optional.ofNullable(this.asDirectMessageChannel())
    public fun maybeAsGuildVoiceChannel(): Optional<GuildVoiceChannel> = Optional.ofNullable(this.asGuildVoiceChannel())
    public fun maybeAsGroupDirectMessageChannel(): Optional<GroupDirectMessageChannel> = Optional.ofNullable(this.asGroupDirectMessageChannel())
    public fun maybeAsGuildCategoryChannel(): Optional<GuildCategoryChannel> = Optional.ofNullable(this.asGuildCategoryChannel())
    public fun maybeAsGuildAnnouncementChannel(): Optional<GuildAnnouncementChannel> = Optional.ofNullable(this.asGuildAnnouncementChannel())

    // TODO - fun isVisibleTo(user: User): Boolean
}
