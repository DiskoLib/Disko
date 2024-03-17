package dev.deftu.disko.presence

public enum class OnlineStatus(
    public val status: String,
    public val description: String
) {
    ONLINE("online", "Online"),
    DND("dnd", "Do Not Disturb"),
    IDLE("idle", "Idle"),
    INVISIBLE("invisible", "Invisible"),
    OFFLINE("offline", "Offline")
}
