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
import dev.deftu.disko.presence.*
import dev.deftu.disko.utils.Snowflake
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonObject
import java.time.Instant

/**
 * Represents a single user activity status. This can be used to show what a user is doing.
 *
 * Bot users are only able to set the `name`, `type`, `url` and `state` fields.
 *
 * @param name The name of the activity
 * @param type The type of the activity
 * @param url The URL of the activity
 * @param createdAt The time the activity was created
 * @param timestamps The timestamps for the activity
 * @param applicationId The application ID of the activity
 * @param details The details of the activity
 * @param state The state of the activity
 * @param emoji The emoji of the activity
 * @param party The party of the activity
 * @param assets The assets of the activity
 * @param secrets The secrets of the activity
 * @param isInstance Whether the activity is an instanced game session or not.
 * @param flags The flags of the activity
 * @param buttons The buttons of the activity
 *
 * @since 0.1.0
 * @author Deftu
 */
public open class Activity(
    override val name: String? = null,
    override val type: ActivityType,
    override val url: String? = null,
    override val createdAt: Instant = Instant.now(),
    override val timestamps: ActivityTimestamps? = null,
    override val applicationId: Snowflake? = null,
    override val details: String? = null,
    override val state: String? = null,
    override val emoji: ActivityEmoji? = null,
    override val party: ActivityParty? = null,
    override val assets: ActivityAssets? = null,
    override val secrets: ActivitySecrets? = null,
    override val isInstance: Boolean? = null,
    override val flags: List<ActivityFlag> = emptyList(),
    override val buttons: List<ActivityButton> = emptyList()
) : BaseActivity {

    public companion object {

        @JvmStatic
        public fun playing(name: String): Activity =
            Activity(name = name, type = ActivityType.PLAYING)

        @JvmStatic
        public fun streaming(details: String, url: String): Activity =
            Activity(details = details, url = url, type = ActivityType.STREAMING)

        @JvmStatic
        public fun listening(name: String): Activity =
            Activity(name = name, type = ActivityType.LISTENING)

        @JvmStatic
        public fun watching(name: String): Activity =
            Activity(name = name, type = ActivityType.WATCHING)

        @JvmStatic
        public fun competing(name: String): Activity =
            Activity(name = name, type = ActivityType.COMPETING)

    }

    override fun toJson(): JsonObject = buildJsonObject {
        if (name != null) add("name", name)
        add("type", type.ordinal)
        if (url != null) add("url", url)
        if (state != null) add("state", state)
        if (timestamps != null) add("timestamps", timestamps!!.toJson())
        if (details != null) add("details", details)
        if (emoji != null) add("emoji", emoji!!.toJson())
        if (assets != null) add("assets", assets!!.toJson())
    }
}
