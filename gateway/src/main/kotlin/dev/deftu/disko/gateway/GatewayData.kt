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
import dev.deftu.disko.utils.authorized
import dev.deftu.disko.utils.parseJson
import okhttp3.OkHttpClient
import okhttp3.Request

public class GatewayData(
    private val token: String,
    private val apiVersion: ApiVersion,
    private val httpClient: OkHttpClient,
) {

    public companion object {
        private var cachedData: GatewayData? = null

        public fun getOrCreate(
            token: String,
            apiVersion: ApiVersion,
            httpClient: OkHttpClient
        ): GatewayData {
            if (cachedData != null)
                return cachedData!!
            return GatewayData(token, apiVersion, httpClient)
        }

    }

    public var gatewayUrl: String? = null
        private set
    public var shards: Int = 0
        private set

    public fun update(isCaching: Boolean = true) {
        val request = Request.Builder()
            .url("https://discord.com/api/${apiVersion.formatted}/gateway/bot")
            .authorized(token)
            .build()
        val response = httpClient.newCall(request).execute()
        val body = response.body?.string() ?: error("No body found in response.")
        val jsonElement = body.parseJson()
        if (!jsonElement.isJsonObject) error("Response is not a JSON object.")

        val jsonObject = jsonElement.asJsonObject

        if (jsonObject.has("url")) {
            val url = jsonObject["url"]
            if (url.isJsonPrimitive) {
                this.gatewayUrl = url.asString
            }
        }

        if (jsonObject.has("shards")) {
            val shards = jsonObject["shards"]
            if (shards.isJsonPrimitive) {
                this.shards = shards.asInt
            }
        }

        if (isCaching) {
            cachedData = this
        }
    }

    public fun isCached(): Boolean {
        return cachedData != null && cachedData == this
    }

}
