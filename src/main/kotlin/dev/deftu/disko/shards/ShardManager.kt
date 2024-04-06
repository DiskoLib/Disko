/*
 * Copyright (C) 2024 Deftu and the Disko contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.deftu.disko.shards

import dev.deftu.disko.Disko
import dev.deftu.disko.DiskoConstants
import okhttp3.Request
import org.slf4j.LoggerFactory

/**
 * Handles the connection state and construction of Shards
 * @since 0.1.0
 * @author Deftu
 */
public class ShardManager(
    private val instance: Disko
) {
    private companion object {
        private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Shard Manager")
    }

    private val shards = mutableListOf<Shard>()

    /**
     * Creates and connects the appropriate amount of Shards necessary
     * @since 0.1.0
     * @author Deftu
     */
    public fun login() {
        val shardCount = instance.gatewayMetadata.getShards()
        logger.info("Creating $shardCount shards")
        for (shardId in 0 until shardCount) {
            shards.add(constructShard(shardCount, shardId))
        }
    }

    /**
     * Attempts a reconnect to a Shard after a disconnect
     * @since 0.1.0
     * @author Deftu
     */
    public fun reconnectShard(shardId: Int) {
        val shard = getShard(shardId) ?: return
        shard.gateway.close(1000, "Reconnecting")
        shards.remove(shard)
        shards.add(constructShard(shard.totalShards, shardId))
    }

    /**
     * Resumes to a Shard after a disconnect
     * @since 0.1.0
     * @author Deftu
     */
    public fun resumeShard(shardId: Int) {
        val shard = getShard(shardId) ?: return
        shard.gateway.close(1000, "Resuming")
        shards.remove(shard)
        shards.add(constructShard(shard.totalShards, shardId, shard.gatewayHandler.resumeGatewayUrl))
        // TODO - shard.send(ResumePacket())
    }

    /**
     * Performs a Unit action for each Shard
     * @since 0.1.0
     * @author Deftu
     */
    public fun forEachShard(action: (Shard) -> Unit) {
        shards.forEach(action)
    }

    /**
     * Gets a specific Shard instance
     * @since 0.1.0
     * @author Deftu
     */
    public fun getShard(shardId: Int): Shard? =
        shards.find { shard -> shard.id == shardId }

    /**
     * Boolean for if a Shard is connected
     * @return [Boolean]
     * @since 0.1.0
     * @author Deftu
     */
    public fun isConnected(): Boolean = shards.isNotEmpty()

    private fun constructShard(
        totalShards: Int,
        shardId: Int,
        customUrl: String? = null
    ): Shard {
        val listener = instance.gatewayBuilder(instance, shardId)
        val shardUrl = customUrl ?: instance.gatewayMetadata.getGatewayUrl()
        return Shard(
            instance,
            shardId,
            totalShards,
            instance.httpClient.newWebSocket(
                Request.Builder()
                    .url(shardUrl)
                    .build(),
                listener
            ),
            listener
        )
    }
}
