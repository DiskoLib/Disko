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

import dev.deftu.disko.presence.Status

public class PresenceUpdateBuilder {
    public var since: Long? = null
    private var activities: List<Activity> = emptyList()
    public var status: Status = Status.ONLINE
    public var afk: Boolean = false

    public fun activities(init: ActivityListBuilder.() -> Unit) {
        val builder = ActivityListBuilder(activities)
        builder.init()
        activities = builder.build()
    }

    public fun build(): PresenceUpdate =
        PresenceUpdate(since, activities, status, afk)
}

public class ActivityListBuilder(
    activities: List<Activity>
) {
    private val activities: MutableList<Activity> = activities.toMutableList()

    public fun add(activity: Activity) {
        activities.add(activity)
    }

    public fun addAll(activities: List<Activity>) {
        this.activities.addAll(activities)
    }

    public fun remove(activity: Activity) {
        activities.remove(activity)
    }

    public operator fun Activity.unaryPlus() {
        add(this)
    }

    public operator fun List<Activity>.unaryPlus() {
        addAll(this)
    }

    public operator fun Activity.unaryMinus() {
        remove(this)
    }

    public fun build(): List<Activity> = activities
}
