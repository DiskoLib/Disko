package dev.deftu.disko.gateway.packets

import com.google.gson.JsonElement
import dev.deftu.disko.gateway.DiskoGateway
import kotlin.reflect.KClass

public interface BasePacket {
    public fun createSendJson(listener: DiskoGateway): JsonElement?
    public fun handleDataReceived(
        listener: DiskoGateway,
        data: JsonElement?,
        seq: Int
    )
}

public interface PacketRegistrationHandler {
    public fun register(registry: PacketRegistry)
}

public open class PacketRegistrationData(
    public val op: Int,
    public val name: String?,
    public val packet: KClass<out BasePacket>
) : PacketRegistrationHandler {
    override fun register(registry: PacketRegistry) {
        registry.register(op, name, packet)
    }
}
