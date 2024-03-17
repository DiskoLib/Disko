package dev.deftu.disko.entities

import dev.deftu.disko.utils.Snowflake

public interface User {
    public val id: Snowflake
    public val username: String
    public val discriminator: String
    public val avatar: String?
    public val bot: Boolean
    public val system: Boolean
    public val mfaEnabled: Boolean
    public val locale: String?
    public val publicFlags: List<UserFlag>
}
