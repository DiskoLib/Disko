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

package dev.deftu.disko.entities.channel

import dev.deftu.disko.Disko
import dev.deftu.disko.DiskoConstants
import dev.deftu.disko.utils.parseJson
import okhttp3.Request
import org.slf4j.LoggerFactory

public class VoiceRegions(
    private val disko: Disko
) {
    private companion object {
        private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Voice Regions")
    }

    private val regions: MutableList<VoiceRegion> = mutableListOf()

    public fun refresh() {
        val request = Request.Builder()
            .url("${disko.baseUrl}/voice/regions")
            .build()
        val response = disko.httpClient.newCall(request)
            .execute()
        val body = response.body?.string() ?: return
        regions.clear()
        val json = body.parseJson()
        if (!json.isJsonArray) return

        val arr = json.asJsonArray
        for (element in arr) {
            if (!element.isJsonObject) continue
            val region = disko.entityConstructor.constructVoiceRegion(element.asJsonObject) ?: continue
            regions.add(region)
        }
    }

    public fun getRegionById(id: String): VoiceRegion? =
        getRegions().firstOrNull { it.id == id }

    public fun getRegions(): List<VoiceRegion> {
        if (regions.isEmpty()) {
            logger.info("No voice regions found, refreshing")
            refresh()
        }

        return regions.toList()
    }
}

public data class VoiceRegion(
    public val id: String,
    public val name: String,
    public val optimal: Boolean,
    public val deprecated: Boolean,
    public val custom: Boolean
)
