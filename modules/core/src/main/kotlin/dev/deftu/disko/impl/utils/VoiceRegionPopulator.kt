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

package dev.deftu.disko.impl.utils

import dev.deftu.disko.api.entity.EntityBuilder
import dev.deftu.disko.api.utils.ApiVersion
import dev.deftu.disko.api.utils.parseJson
import dev.deftu.disko.impl.entity.EntityCache
import okhttp3.OkHttpClient
import okhttp3.Request

public object VoiceRegionPopulator {

    public fun populate(
        httpClient: OkHttpClient,
        entityBuilder: EntityBuilder,
        entityCache: EntityCache
    ) {
        val request = Request.Builder()
            .url("https://discord.com/api/${ApiVersion.latest.formatted}/voice/regions")
            .get()
            .build()
        val response = httpClient
            .newCall(request)
            .execute()
        val body = response.body?.string()
        if (body == null) {
            response.close()
            return
        }

        response.close()
        entityCache.clearVoiceRegions()

        val json = body.parseJson()
        if (!json.isJsonArray) {
            return
        }

        val arr = json.asJsonArray
        for (element in arr) {
            if (!element.isJsonObject) {
                continue
            }

            val region = entityBuilder.voiceRegion(element.asJsonObject) ?: continue
            entityCache.addVoiceRegion(region)
        }
    }

}
