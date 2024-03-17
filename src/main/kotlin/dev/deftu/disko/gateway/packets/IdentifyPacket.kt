package dev.deftu.disko.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.Disko
import dev.deftu.disko.DiskoConstants
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonObject
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.intents.GatewayIntent
import dev.deftu.disko.utils.buildJsonArray

public class IdentifyPacket(
    private val instance: Disko
) : BasePacket {
    override fun createSendJson(
        listener: DiskoGateway
    ): JsonElement = buildJsonObject {
        add("token", instance.token)
        add("intents", GatewayIntent.calculate(*instance.intentManager.get().toTypedArray()))

        add("properties", buildJsonObject {
            addProperty("os", System.getProperty("os.name"))
            addProperty("browser", DiskoConstants.NAME)
            addProperty("device", DiskoConstants.NAME)
        })

        add("presence", instance.presenceManager.toJson())
        add("shard", buildJsonArray {
            add(listener.shardId)
            add(listener.instance.gatewayMetadata.getShards())
        })
    }

    override fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    ) {
        // no-op
    }
}
