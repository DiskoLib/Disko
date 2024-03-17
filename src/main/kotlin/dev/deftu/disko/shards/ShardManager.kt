package dev.deftu.disko.shards

import dev.deftu.disko.Disko
import dev.deftu.disko.DiskoConstants
import okhttp3.Request
import org.slf4j.LoggerFactory

public class ShardManager(
    private val instance: Disko
) {
    private companion object {
        private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Shard Manager")
    }

    private val shards = mutableListOf<Shard>()

    public fun login() {
        val shardCount = instance.gatewayMetadata.getShards()
        logger.info("Creating $shardCount shards")
        for (shardId in 0 until shardCount) {
            shards.add(constructShard(shardCount, shardId))
        }
    }

    public fun reconnectShard(shardId: Int) {
        val shard = getShard(shardId) ?: return
        shard.gateway.close(1000, "Reconnecting")
        shards.remove(shard)
        shards.add(constructShard(shard.totalShards, shardId))
    }

    public fun forEachShard(action: (Shard) -> Unit) {
        shards.forEach(action)
    }

    public fun getShard(shardId: Int): Shard? =
        shards.find { shard -> shard.id == shardId }

    public fun isConnected(): Boolean = shards.isNotEmpty()

    private fun constructShard(
        totalShards: Int,
        shardId: Int
    ): Shard {
        val listener = instance.gatewayBuilder(instance, shardId)
        return Shard(
            instance,
            shardId,
            totalShards,
            instance.httpClient.newWebSocket(
                Request.Builder()
                    .url(instance.gatewayMetadata.getUrl())
                    .build(),
                listener
            ),
            listener
        )
    }
}
