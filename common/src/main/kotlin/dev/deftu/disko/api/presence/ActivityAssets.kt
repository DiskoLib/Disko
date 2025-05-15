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

package dev.deftu.disko.api.presence

import com.google.gson.JsonObject
import dev.deftu.disko.api.utils.DataObject
import dev.deftu.disko.api.utils.add
import dev.deftu.disko.api.utils.buildJsonObject

public data class ActivityAssets(
    public val large: Asset? = null,
    public val small: Asset? = null,
) : DataObject {

    public data class Asset(
        public val image: String?,
        public val text: String?
    )

    override fun toJson(): JsonObject = buildJsonObject {
        large?.let { large ->
            add("large_image", large.image)
            add("large_text", large.text)
        }

        small?.let { small ->
            add("small_image", small.image)
            add("small_text", small.text)
        }
    }

}
