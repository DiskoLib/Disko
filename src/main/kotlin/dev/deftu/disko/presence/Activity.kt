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

package dev.deftu.disko.presence

import com.google.gson.JsonObject
import dev.deftu.disko.utils.DataObject
import dev.deftu.disko.utils.Snowflake
import dev.deftu.disko.utils.buildJsonObject

public data class Activity(
    val name: String,
    val type: ActivityType,
    val url: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val timestamps: ActivityTimestamps? = null,
    val applicationId: Snowflake? = null,
    val details: String? = null,
    val state: String? = null,
    // TODO - emoji
    // TODO - party
    // TODO - assets
    // TODO - secrets
    val instance: Boolean? = null,
    // TODO - flags
    // TODO - buttons
) : DataObject {
    override fun toJson(): JsonObject = buildJsonObject {
        addProperty("name", name)
        addProperty("type", type.ordinal)
        if (url != null) addProperty("url", url)
        addProperty("created_at", createdAt)
        if (timestamps != null) add("timestamps", timestamps.toJson())
        // TODO
    }
}

public enum class ActivityType {
    PLAYING,
    STREAMING,
    LISTENING,
    WATCHING,
    CUSTOM,
    COMPETING,
}

public data class ActivityTimestamps(
    val start: Long? = null,
    val end: Long? = null
) : DataObject {
    override fun toJson(): JsonObject = buildJsonObject {
        if (start != null) addProperty("start", start)
        if (end != null) addProperty("end", end)
    }
}
