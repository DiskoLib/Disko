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
