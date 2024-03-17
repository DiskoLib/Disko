package dev.deftu.disko.entities

import dev.deftu.disko.utils.Snowflake

public class SelfUser(
    override val id: Snowflake,
    override val username: String,
    override val discriminator: String,
    avatar: String?,
    override val mfaEnabled: Boolean,
    override val locale: String?,
    override val publicFlags: List<UserFlag>
) : User {
    override val bot: Boolean = true
    override val system: Boolean = true
    override val avatar: String? = avatar
        get() = field ?: "https://cdn.discordapp.com/embed/avatars/${(id.value shr 22) % 5}.png"


}
