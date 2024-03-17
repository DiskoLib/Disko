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
