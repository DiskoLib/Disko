package dev.deftu.disko.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.presence.Activity
import dev.deftu.disko.presence.OnlineStatus
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonArray
import dev.deftu.disko.utils.buildJsonObject

public class PresenceUpdatePacket(
    private val activities: List<Activity>,
    private val status: OnlineStatus,
    private val since: Long?
) : BasePacket {
    public companion object : PacketRegistrationData(3, null, PresenceUpdatePacket::class)

    public constructor() : this(emptyList(), OnlineStatus.ONLINE, null)

    override fun createSendJson(
        listener: DiskoGateway
    ): JsonElement = buildJsonObject {
        add("activities", buildJsonArray {
            activities.forEach { add(it.toJson()) }
        })

        add("status", status.status)
        addProperty("since", since)
    }

    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        // no-op
    }
}
