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
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonObject

/**
 * Represents a single user activity status. This can be used to show what a user is doing.
 *
 * Bot users are only able to set the `name`, `type`, `url` and `state` fields for now, and such is reflected in the [toJson] function.
 *
 * @param name The name of the activity
 * @param type The type of the activity
 * @param url The URL of the activity
 * @param createdAt The time the activity was created
 * @param timestamps The timestamps for the activity
 * @param applicationId The application ID of the activity
 * @param details The details of the activity
 * @param state The state of the activity
 * @param instance Whether the activity is an instanced game session or not.
 *
 * @since 0.1.0
 * @author Deftu
 */
public open class Activity(
    public val name: String,
    public val type: ActivityType,
    public val url: String? = null,
    public val createdAt: Long = System.currentTimeMillis(),
    public val timestamps: ActivityTimestamps? = null,
    public val applicationId: Snowflake? = null,
    public val details: String? = null,
    public val state: String? = null,
    // TODO - emoji
    // TODO - party
    // TODO - assets
    // TODO - secrets
    public val instance: Boolean? = null,
    // TODO - flags
    // TODO - buttons
) : DataObject {
    public class Playing(
        name: String
    ) : Activity(name, ActivityType.PLAYING)

    public class Streaming(
        name: String,
        url: String
    ) : Activity(name, ActivityType.STREAMING, url)

    public class Listening(
        name: String
    ) : Activity(name, ActivityType.LISTENING)

    public class Watching(
        name: String
    ) : Activity(name, ActivityType.WATCHING)

    public class Competing(
        name: String
    ) : Activity(name, ActivityType.COMPETING)

    override fun toJson(): JsonObject = buildJsonObject {
        add("name", name)
        add("type", type.ordinal)
        if (url != null) add("url", url)
        if (state != null) add("state", state)
        // Bots are not allowed to define any more fields
    }
}
