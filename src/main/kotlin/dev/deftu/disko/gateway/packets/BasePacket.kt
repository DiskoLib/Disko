package dev.deftu.disko.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.gateway.DiskoGateway

public interface BasePacket {
    public fun createSendJson(listener: DiskoGateway): JsonElement?
    public fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    )
}
