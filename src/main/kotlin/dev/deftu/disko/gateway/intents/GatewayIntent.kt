package dev.deftu.disko.gateway.intents

public enum class GatewayIntent(
    public val value: Int,
    public val isPrivileged: Boolean = false
) {
    GUILDS(0),
    GUILD_MEMBERS(1),
    GUILD_MODERATION(2),
    GUILD_EMOJIS_AND_STICKERS(3),
    GUILD_INTEGRATIONS(4),
    GUILD_WEBHOOKS(5),
    GUILD_INVITES(6),
    GUILD_VOICE_STATES(7),
    GUILD_PRESENCES(8),
    GUILD_MESSAGES(9),
    GUILD_MESSAGE_REACTIONS(10),
    GUILD_MESSAGE_TYPING(11),
    GUILD_SCHEDULED_EVENTS(16),

    AUTO_MODERATION_CONFIGURATION(20),
    AUTO_MODERATION_EXECUTION(21),

    DIRECT_MESSAGES(12),
    DIRECT_MESSAGE_REACTIONS(13),
    DIRECT_MESSAGE_TYPING(14),

    MESSAGE_CONTENT(15);

    public companion object {
        public fun calculate(vararg intents: GatewayIntent): Int {
            var value = 0
            for (i in intents) value += (1 shl i.value)

            return value
        }

        public fun all(): Int = calculate(*entries.toTypedArray())
    }
}