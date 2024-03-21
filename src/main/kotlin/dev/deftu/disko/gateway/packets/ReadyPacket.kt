package dev.deftu.disko.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.events.ReadyEvent
import dev.deftu.disko.gateway.DiskoGateway

public class ReadyPacket : BasePacket {
    public companion object : PacketRegistrationData(0, "READY", ReadyPacket::class)

    override fun createSendJson(
        listener: DiskoGateway
    ): JsonElement? = null

    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        if (data == null || !data.isJsonObject) return

        val userJson = data.asJsonObject["user"].asJsonObject
        val selfUser = listener.instance.entityConstructor.constructSelfUser(userJson) ?: return
        listener.instance.selfUser = selfUser

        val rawSessionId = data.asJsonObject["session_id"] ?: return
        listener.sessionId = rawSessionId.asString

        // TODO - guilds, private channels, presences, relationships

        listener.instance.eventBus.post(ReadyEvent(listener.instance, listener.shardId, selfUser))
    }
}
