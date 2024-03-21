package dev.deftu.disko.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.gateway.DiskoGateway

public class HelloPacket : BasePacket {
    public companion object : PacketRegistrationData(10, null, HelloPacket::class)

    override fun createSendJson(
        listener: DiskoGateway
    ): JsonElement? = null

    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        if (data == null || !data.isJsonObject) return
        listener.heart.hello(data.asJsonObject)
    }
}
