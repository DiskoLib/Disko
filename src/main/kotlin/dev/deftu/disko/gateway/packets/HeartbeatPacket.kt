package dev.deftu.disko.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.utils.buildJsonObject
import dev.deftu.disko.gateway.DiskoGateway

public class HeartbeatPacket : BasePacket {
    public companion object : PacketRegistrationData(1, null, HeartbeatPacket::class)
    }

    override fun createSendJson(
        listener: DiskoGateway
    ): JsonElement = buildJsonObject {
        addProperty("d", if (listener.lastSeq == -1) null else listener.lastSeq)
    }

    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        // no-op
    }
}
