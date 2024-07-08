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

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.nio.file.Path
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

public enum class ImageFormat(
    public val extensions: List<String>
) {
    JPEG("jpeg", "jpg"),
    PNG("png"),
    WEBP("webp"),
    GIF("gif"),
    UNKNOWN("");

    public val defaultExtension: String
        get() = extensions.first()

    constructor(vararg extensions: String) : this(extensions.toList())

    public fun isFileSupported(fileName: String): Boolean {
        return extensions.any { fileName.endsWith(it) }
    }

    public companion object {

        public fun isFileSupported(fileName: String): Boolean {
            @Suppress("EnumValuesSoftDeprecate")
            return values().any { format ->
                format.isFileSupported(fileName)
            }
        }

        public fun fromContentType(contentType: String): ImageFormat {
            return when {
                contentType.contains("jpeg") -> JPEG
                contentType.contains("png") -> PNG
                contentType.contains("webp") -> WEBP
                contentType.contains("gif") -> GIF
                else -> UNKNOWN
            }
        }

    }
}

public class Image private constructor(
    public val data: ByteArray,
    public val format: ImageFormat
) {

    public companion object {

        public fun raw(data: ByteArray, format: ImageFormat): Image {
            return Image(data, format)
        }

        public fun file(file: File, format: ImageFormat): Image {
            return Image(file.readBytes(), format)
        }

        public fun path(path: Path, format: ImageFormat): Image {
            return file(path.toFile(), format)
        }

        public fun fromUrl(client: OkHttpClient, url: String): Image {
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            val response = client.newCall(request).execute()
            val contentType = response.header("Content-Type") ?: throw IllegalStateException("Failed to get image content type")
            val data = response.body?.bytes() ?: throw IllegalStateException("Failed to get image data")

            return Image(data, ImageFormat.fromContentType(contentType))
        }

    }

    @OptIn(ExperimentalEncodingApi::class)
    public fun createDataUri(): String {
        return "data:image/${format.defaultExtension};base64,${Base64.encode(data)}"
    }

}
