package dev.deftu.disko.entities

public enum class UserFlag(
    public val value: Int
) {
    STAFF(0),
    PARTNER(1),
    HYPESQUAD(2),
    BUG_HUNTER_1(3),
    BUG_HUNTER_2(14),
    HYPESQUAD_BRAVERY(6),
    HYPESQUAD_BRILLIANCE(7),
    HYPESQUAD_BALANCE(8),
    EARLY_SUPPORTER(9),
    VERIFIED_BOT(16),
    VERIFIED_DEVELOPER(17),
    CERTIFIED_MODERATOR(18),
    ACTIVE_DEVELOPER(22);

    public companion object {
        public fun from(value: Int): List<UserFlag> {
            val flags = mutableListOf<UserFlag>()
            entries.forEach {
                if (value and (1 shl it.value) != 0) {
                    flags.add(it)
                }
            }

            return flags
        }
    }
}
