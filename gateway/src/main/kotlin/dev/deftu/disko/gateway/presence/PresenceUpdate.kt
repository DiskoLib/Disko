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

package dev.deftu.disko.gateway.presence

import com.google.gson.JsonObject
import dev.deftu.disko.presence.Activity
import dev.deftu.disko.presence.Status
import dev.deftu.disko.utils.DataObject
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonArray
import dev.deftu.disko.utils.buildJsonObject

public fun PresenceUpdate(builder: PresenceUpdateBuilder.() -> Unit): PresenceUpdate =
    PresenceUpdateBuilder().apply(builder).build()

public data class PresenceUpdate(
    public val since: Long?,
    public val activities: List<Activity>,
    public val status: Status,
    public val afk: Boolean
) : DataObject {
    override fun toJson(): JsonObject = buildJsonObject {
        add("activities", buildJsonArray {
            for (activity in activities) {
                add(activity.toJson())
            }
        })

        add("since", since)
        add("status", status.value)
        add("afk", afk)
    }
}
