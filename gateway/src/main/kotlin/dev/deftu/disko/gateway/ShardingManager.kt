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

package dev.deftu.disko.gateway

import kotlinx.coroutines.CoroutineScope
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

public open class ShardingManager(
    final override val coroutineContext: CoroutineContext,
    private val gatewayBuilder: (Shard) -> DiskoGateway
) : CoroutineScope {

    private companion object {
        private val logger = LoggerFactory.getLogger("Disko Sharding Manager")
    }

    private val shards: MutableMap<Shard, DiskoGateway> = mutableMapOf()

    public fun login(amount: Int) {
        logger.info("Creating $amount shards")
        for (shardId in 0 until amount) {
            val shard = Shard(shardId, amount)
            shards[shard] = gatewayBuilder(shard)
        }
    }

}
