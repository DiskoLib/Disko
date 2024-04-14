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

public val JsonElement?.isJsonObject: Boolean
    get() = this != null && isJsonObject

public fun buildJsonObject(init: JsonObject.() -> Unit): JsonObject {
    val obj = JsonObject()
    obj.init()
    return obj
}

public fun JsonObject.add(key: String, value: String?) {
    addProperty(key, value)
}

public fun JsonObject.add(key: String, value: Number?) {
    addProperty(key, value)
}

public fun JsonObject.add(key: String, value: Boolean?) {
    addProperty(key, value)
}

public fun buildJsonArray(init: JsonArray.() -> Unit): JsonArray {
    val array = JsonArray()
    array.init()
    return array
}

public fun JsonObject.isNull(key: String): Boolean {
    return has(key) && get(key).isJsonNull
}

public fun JsonObject.maybeGet(key: String): JsonElement? {
    return if (has(key)) get(key) else null
}

public fun JsonObject.maybeGetString(key: String): String? {
    val element = maybeGet(key)
    return if (element != null && element.isJsonPrimitive) element.asString else null
}

public fun JsonObject.maybeGetInteger(key: String): Int? {
    val element = maybeGet(key)
    return if (element != null && element.isJsonPrimitive) element.asInt else null
}

public fun JsonObject.maybeGetLong(key: String): Long? {
    val element = maybeGet(key)
    return if (element != null && element.isJsonPrimitive) element.asLong else null
}

public fun JsonObject.maybeGetSnowflake(key: String): Snowflake? {
    val element = maybeGet(key)
    return if (element != null && element.isJsonPrimitive) Snowflake(element.asLong) else null
}

public fun JsonObject.maybeGetBoolean(key: String): Boolean? {
    val element = maybeGet(key)
    return if (element != null && element.isJsonPrimitive) element.asBoolean else null
}

public fun JsonObject.maybeGetJsonObject(key: String): JsonObject? {
    val element = maybeGet(key)
    return if (element != null && element.isJsonObject) element.asJsonObject else null
}

public fun JsonObject.maybeGetJsonArray(key: String): JsonArray? {
    val element = maybeGet(key)
    return if (element != null && element.isJsonArray) element.asJsonArray else null
}
