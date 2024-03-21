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

package dev.deftu.disko.utils

import dev.deftu.disko.Disko
import dev.deftu.disko.DiskoConstants
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Internet Availability Checker")

public fun isInternetAvailable(
    httpClient: OkHttpClient
): Boolean {
    val google = canPingGoogle(httpClient)
    if (!google) logger.warn("Couldn't ping Google. Internet availability may be limited or Google's is unavailable.")

    val cloudflare = canPingCloudflare(httpClient)
    if (!cloudflare) logger.warn("Couldn't ping Cloudflare. Internet availability may be limited or Cloudflare's is unavailable.")

    return google || cloudflare
}

private fun canPingGoogle(
    httpClient: OkHttpClient
): Boolean = try {
    val request = Request.Builder()
        .url("http://8.8.8.8")
        .build()

    httpClient.newCall(request).execute().isSuccessful
} catch (e: Exception) {
    false
}

private fun canPingCloudflare(
    httpClient: OkHttpClient
): Boolean = try {
    val request = Request.Builder()
        .url("http://1.1.1.1")
        .build()

    httpClient.newCall(request).execute().isSuccessful
} catch (e: Exception) {
    false
}
