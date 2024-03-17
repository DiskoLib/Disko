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
