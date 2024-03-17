package dev.deftu.disko.shards

import dev.deftu.disko.Disko
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.packets.BasePacket
import okhttp3.WebSocket

public data class Shard(
    val instance: Disko,
    val id: Int,
    val totalShards: Int,
    val gateway: WebSocket,
    val gatewayHandler: DiskoGateway
) {
    public fun send(packet: BasePacket): Unit =
        gatewayHandler.send(packet)
}
