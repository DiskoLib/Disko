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

import dev.deftu.disko.utils.ApiVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

public open class Sharder(
    final override val coroutineContext: CoroutineContext,
    private val token: String,
    private val httpClient: OkHttpClient,
    private val gatewayBuilder: (token: String, httpClient: OkHttpClient, shard: Shard) -> DiskoGateway
) : CoroutineScope {

    private companion object {
        private val logger = LoggerFactory.getLogger("Disko Sharding Manager")
    }

    private val shards: MutableMap<Shard, DiskoGateway> = mutableMapOf()

    public constructor(
        token: String,
        httpClient: OkHttpClient,
        gatewayBuilder: (token: String, httpClient: OkHttpClient, shard: Shard) -> DiskoGateway
    ) : this(Dispatchers.Default + SupervisorJob(), token, httpClient, gatewayBuilder)

    public fun populateShards(token: String, amount: Int) {
        logger.info("Creating $amount shards")
        for (shardId in 0 until amount) {
            val shard = Shard(shardId, amount)
            shards[shard] = gatewayBuilder(token, httpClient, shard)
        }
    }

    public fun populateShards(token: String) {
        val gatewayData = GatewayData.getOrCreate(token, ApiVersion.V8, httpClient)
        if (!gatewayData.isCached()) gatewayData.update()

        populateShards(token, gatewayData.shards)
    }

    public fun loginManually(): Sharder = apply {
        for ((_, gateway) in shards) {
            gateway.login()
        }
    }

    public fun login(amount: Int): Sharder = apply {
        populateShards(token, amount)
        loginManually()
    }

    public fun login(
        apiVersion: ApiVersion
    ): Sharder = apply {
        val gatewayData = GatewayData.getOrCreate(token, apiVersion, httpClient)
        if (!gatewayData.isCached()) gatewayData.update()

        login(gatewayData.shards)
    }

    public fun close(code: Int, reason: String) {
        for ((_, gateway) in shards) {
            gateway.close(code, reason)
        }
    }

    public fun close(code: CloseCode, reason: String) {
        for ((_, gateway) in shards) {
            gateway.close(code, reason)
        }
    }

    public fun forEach(action: (shard: Shard, gateway: DiskoGateway) -> Unit) {
        shards.forEach(action)
    }

}
