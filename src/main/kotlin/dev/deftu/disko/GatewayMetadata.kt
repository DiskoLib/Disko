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

package dev.deftu.disko

import dev.deftu.disko.utils.parseJson
import okhttp3.Request
import java.lang.IllegalStateException

public class GatewayMetadata(
    private val instance: Disko
) {
    private var apiVersion: ApiVersion = ApiVersion.V10
    private var url: String? = null
    private var shards: Int = 0

    public fun setApiVersion(version: ApiVersion) {
        apiVersion = version
    }

    public fun getApiVersion(): ApiVersion = apiVersion

    public fun getUrl(): String {
        if (url == null) refresh()
        return url!!
    }

    public fun setUrl(url: String) {
        this.url = url
    }

    public fun getShards(): Int {
        if (shards == 0) refresh()
        return shards
    }

    public fun setShards(shards: Int) {
        this.shards = shards
    }

    public fun refresh() {
        val response = instance.httpClient.newCall(
            Request.Builder()
                .url("https://discord.com/api/v${apiVersion.value}/gateway/bot")
                .addHeader("Authorization", "Bot ${instance.token}")
                .build()
        ).execute()
        val body = response.body?.string() ?: throw IllegalStateException() // TODO - Don't throw an exception here
        val json = body.parseJson()
        if (
            !json.isJsonObject ||
            !json.asJsonObject.has("url") ||
            !json.asJsonObject.has("shards") ||
            !json.asJsonObject["url"].isJsonPrimitive ||
            !json.asJsonObject["shards"].isJsonPrimitive
        ) throw IllegalStateException() // TODO - Don't throw an exception here
        setUrl(json.asJsonObject["url"].asString)
        setShards(json.asJsonObject["shards"].asInt)
    }
}
