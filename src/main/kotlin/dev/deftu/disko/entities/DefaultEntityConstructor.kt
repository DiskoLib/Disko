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

package dev.deftu.disko.entities

import com.google.gson.JsonObject
import dev.deftu.disko.utils.Snowflake

public object DefaultEntityConstructor : EntityConstructor {
    override fun constructSelfUser(json: JsonObject): SelfUser? {
        val rawId = json.get("id")?.asLong ?: return null
        val id = Snowflake(rawId)
        val username = json.get("username")?.asString ?: return null
        val discriminator = json.get("discriminator")?.asString ?: "0"
        val rawAvatar = json.get("avatar")
        val avatar = if (rawAvatar != null && !rawAvatar.isJsonNull) rawAvatar.asString else null
        val mfaEnabled = json.get("mfa_enabled")?.asBoolean ?: false
        val locale = json.get("locale")?.asString ?: "en-US"
        val rawFlags = json.get("flags")?.asInt ?: 0
        val flags = UserFlag.from(rawFlags)

        return SelfUser(
            id,
            username,
            discriminator,
            avatar,
            mfaEnabled,
            locale,
            flags
        )
    }
}
