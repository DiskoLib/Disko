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

import dev.deftu.disko.utils.DataObject
import dev.deftu.disko.utils.Snowflake
import java.time.Instant

public interface BaseActivity : DataObject {
    public val name: String?
    public val type: ActivityType
    public val url: String?
    public val createdAt: Instant
    public val timestamps: ActivityTimestamps?
    public val applicationId: Snowflake?
    public val details: String?
    public val state: String?
    public val emoji: ActivityEmoji?
    public val party: ActivityParty?
    public val assets: ActivityAssets?
    public val secrets: ActivitySecrets?
    public val isInstance: Boolean?
    public val flags: List<ActivityFlag>
    public val buttons: List<ActivityButton>
}
