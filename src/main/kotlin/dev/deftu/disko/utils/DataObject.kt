package dev.deftu.disko.utils

import com.google.gson.JsonElement

internal interface DataObject {
    public fun toJson(): JsonElement
}
