package dev.deftu.disko.presence

import com.google.gson.JsonObject
import dev.deftu.disko.utils.DataObject
import dev.deftu.disko.utils.Snowflake
import dev.deftu.disko.utils.buildJsonObject

public data class Activity(
    val name: String,
    val type: ActivityType,
    val url: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val timestamps: ActivityTimestamps? = null,
    val applicationId: Snowflake? = null,
    val details: String? = null,
    val state: String? = null,
    // TODO - emoji
    // TODO - party
    // TODO - assets
    // TODO - secrets
    val instance: Boolean? = null,
    // TODO - flags
    // TODO - buttons
) : DataObject {
    override fun toJson(): JsonObject = buildJsonObject {
        addProperty("name", name)
        addProperty("type", type.ordinal)
        if (url != null) addProperty("url", url)
        addProperty("created_at", createdAt)
        if (timestamps != null) add("timestamps", timestamps.toJson())
        // TODO
    }
}

public enum class ActivityType {
    PLAYING,
    STREAMING,
    LISTENING,
    WATCHING,
    CUSTOM,
    COMPETING,
}

public data class ActivityTimestamps(
    val start: Long? = null,
    val end: Long? = null
) : DataObject {
    override fun toJson(): JsonObject = buildJsonObject {
        if (start != null) addProperty("start", start)
        if (end != null) addProperty("end", end)
    }
}
