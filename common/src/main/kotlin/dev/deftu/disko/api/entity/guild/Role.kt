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

import dev.deftu.disko.api.entity.channel.Permission
import dev.deftu.disko.api.utils.Snowflake
import java.awt.Color

public data class Role(
    public val id: Snowflake,
    public val name: String,
    public val color: Color,
    public val isHoisted: Boolean,
    private val icon: String?,
    public val position: Int,
    public val permissions: Set<Permission>,
    public val managed: Boolean,
    public val isMentionable: Boolean,
    public val tags: RoleTags?,
    public val flags: Set<RoleFlag>
) {

    public fun getIconUrl(): String {
        return "https://cdn.discordapp.com/icons/$id/$icon.png"
    }

}

public data class RoleTags(
    public val botId: Snowflake?,
    public val integrationId: Snowflake?,
    public val isPremiumSubscriber: Boolean,
    public val subscriptionListingId: Snowflake?,
    public val isAvailableForPurchase: Boolean,
    public val isLinkedRole: Boolean
)
