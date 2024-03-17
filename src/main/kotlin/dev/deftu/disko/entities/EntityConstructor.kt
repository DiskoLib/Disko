package dev.deftu.disko.entities

import com.google.gson.JsonObject

public interface EntityConstructor {
    public fun constructSelfUser(json: JsonObject): SelfUser?
}
