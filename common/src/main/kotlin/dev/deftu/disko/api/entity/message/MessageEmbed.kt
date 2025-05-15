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

package dev.deftu.disko.api.entity.message

import com.google.gson.JsonObject
import dev.deftu.disko.api.utils.*
import java.time.Instant

public data class MessageEmbed(
    public val title: String? = null,
    public val type: MessageEmbedType? = null,
    public val description: String? = null,
    public val url: String? = null,
    public val timestamp: Instant? = null,
    public val color: Int? = null,
    public val footer: MessageEmbedFooter? = null,
    public val image: MessageEmbedImage? = null,
    public val thumbnail: MessageEmbedThumbnail? = null,
    public val video: MessageEmbedVideo? = null,
    public val provider: MessageEmbedProvider? = null,
    public val author: MessageEmbedAuthor? = null,
    public val fields: Set<MessageEmbedField>? = null
) : DataObject {

    override fun toJson(): JsonObject {
        val json = JsonObject()
        title?.let { json.add("title", it) }
        description?.let { json.add("description", it) }
        url?.let { json.add("url", it) }
        timestamp?.let { json.add("timestamp", it.toString()) }
        color?.let { json.add("color", it) }
        footer?.let { json.add("footer", it.toJson()) }
        image?.let { json.add("image", it.toJson()) }
        thumbnail?.let { json.add("thumbnail", it.toJson()) }
        video?.let { json.add("video", it.toJson()) }
        provider?.let { json.add("provider", it.toJson()) }
        author?.let { json.add("author", it.toJson()) }
        fields?.let { json.add("fields", buildJsonArray {
                it.forEach {
                    add(it.toJson())
                }
            })
        }

        return json
    }

    public enum class MessageEmbedType(public val value: String) {

        RICH("rich"),
        IMAGE("image"),
        VIDEO("video"),
        GIFV("gifv"),
        ARTICLE("article"),
        LINK("link");

        public companion object {

            public fun from(value: String): MessageEmbedType? {
                return entries.firstOrNull { type ->
                    type.value == value
                }
            }

        }

    }

    public data class MessageEmbedFooter(
        public val text: String,
        public val iconUrl: String? = null,
        public val proxyIconUrl: String? = null
    ) : DataObject {

        override fun toJson(): JsonObject {
            val json = JsonObject()
            json.addProperty("text", text)
            iconUrl?.let { json.addProperty("icon_url", it) }
            proxyIconUrl?.let { json.addProperty("proxy_icon_url", it) }
            return json
        }

    }

    public data class MessageEmbedImage(
        public val url: String,
        public val proxyUrl: String? = null,
        public val height: Int? = null,
        public val width: Int? = null
    ) : DataObject {

        override fun toJson(): JsonObject {
            val json = JsonObject()
            json.addProperty("url", url)
            proxyUrl?.let { json.addProperty("proxy_url", it) }
            height?.let { json.addProperty("height", it) }
            width?.let { json.addProperty("width", it) }
            return json
        }

    }

    public data class MessageEmbedThumbnail(
        public val url: String,
        public val proxyUrl: String? = null,
        public val height: Int? = null,
        public val width: Int? = null
    ) : DataObject {

        override fun toJson(): JsonObject {
            val json = JsonObject()
            json.addProperty("url", url)
            proxyUrl?.let { json.addProperty("proxy_url", it) }
            height?.let { json.addProperty("height", it) }
            width?.let { json.addProperty("width", it) }
            return json
        }

    }

    public data class MessageEmbedVideo(
        public val url: String,
        public val height: Int? = null,
        public val width: Int? = null
    ) : DataObject {

        override fun toJson(): JsonObject {
            val json = JsonObject()
            json.addProperty("url", url)
            height?.let { json.addProperty("height", it) }
            width?.let { json.addProperty("width", it) }
            return json
        }

    }

    public data class MessageEmbedProvider(
        public val name: String,
        public val url: String? = null
    ) : DataObject {

        override fun toJson(): JsonObject {
            val json = JsonObject()
            json.addProperty("name", name)
            url?.let { json.addProperty("url", it) }
            return json
        }

    }

    public data class MessageEmbedAuthor(
        public val name: String,
        public val url: String? = null,
        public val iconUrl: String? = null,
        public val proxyIconUrl: String? = null
    ) : DataObject {

        override fun toJson(): JsonObject {
            val json = JsonObject()
            json.addProperty("name", name)
            url?.let { json.addProperty("url", it) }
            iconUrl?.let { json.addProperty("icon_url", it) }
            proxyIconUrl?.let { json.addProperty("proxy_icon_url", it) }
            return json
        }

    }

    public data class MessageEmbedField(
        public val name: String,
        public val value: String,
        public val inline: Boolean = false
    ) : DataObject {

        override fun toJson(): JsonObject {
            val json = JsonObject()
            json.addProperty("name", name)
            json.addProperty("value", value)
            json.addProperty("inline", inline)
            return json
        }

    }

}
