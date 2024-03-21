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

package dev.deftu.disko.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

public fun String.parseJson(): JsonElement {
    return JsonParser.parseString(this)
}

public fun isValidJson(json: String): Boolean {
    return try {
        json.parseJson()
        true
    } catch (e: Exception) {
        false
    }
}

public fun buildJsonObject(init: JsonObject.() -> Unit): JsonObject {
    val obj = JsonObject()
    obj.init()
    return obj
}

public fun JsonObject.add(key: String, value: String) {
    addProperty(key, value)
}

public fun JsonObject.add(key: String, value: Number) {
    addProperty(key, value)
}

public fun JsonObject.add(key: String, value: Boolean) {
    addProperty(key, value)
}

public fun buildJsonArray(init: JsonArray.() -> Unit): JsonArray {
    val array = JsonArray()
    array.init()
    return array
}
