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

package dev.deftu.disko.entity.message

import com.google.gson.JsonObject
import dev.deftu.disko.utils.*
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
    public val fields: List<MessageEmbedField>? = null
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

    public companion object {

        @JvmStatic
        public fun parseJson(obj: JsonObject): MessageEmbed {
            val title = obj.maybeGetString("title")
            val type = MessageEmbedType.from(obj.maybeGetString("type") ?: "rich") ?: MessageEmbed.MessageEmbedType.RICH
            val description = obj.maybeGetString("description")
            val url = obj.maybeGetString("url")
            val timestamp = obj.maybeGetString("timestamp")?.let { Instant.parse(it) }
            val color = obj.maybeGetInteger("color")
            val footer = obj.maybeGetJsonObject("footer")?.let { parseFooterJson(it) }
            val image = obj.maybeGetJsonObject("image")?.let { parseImageJson(it) }
            val thumbnail = obj.maybeGetJsonObject("thumbnail")?.let { parseThumbnailJson(it) }
            val video = obj.maybeGetJsonObject("video")?.let { parseVideoJson(it) }
            val provider = obj.maybeGetJsonObject("provider")?.let { parseEmbedProviderJson(it) }
            val author = obj.maybeGetJsonObject("author")?.let { parseAuthorJson(it) }
            val fields = obj.get("fields")?.asJsonArray?.mapNotNull { parseFieldJson(it.asJsonObject) } ?: emptyList()

            return MessageEmbed(
                title,
                type,
                description,
                url,
                timestamp,
                color,
                footer,
                image,
                thumbnail,
                video,
                provider,
                author,
                fields
            )
        }

        @JvmStatic
        public fun parseFooterJson(obj: JsonObject): MessageEmbedFooter? {
            val text = obj.maybeGetString("text") ?: return null
            val iconUrl = obj.maybeGetString("icon_url")
            val proxyIconUrl = obj.maybeGetString("proxy_icon_url")

            return MessageEmbedFooter(
                text,
                iconUrl,
                proxyIconUrl
            )
        }

        @JvmStatic
        public fun parseImageJson(obj: JsonObject): MessageEmbedImage? {
            val url = obj.maybeGetString("url") ?: return null
            val proxyUrl = obj.maybeGetString("proxy_url")
            val height = obj.maybeGetInteger("height")
            val width = obj.maybeGetInteger("width")

            return MessageEmbedImage(
                url,
                proxyUrl,
                height,
                width
            )
        }

        @JvmStatic
        public fun parseThumbnailJson(obj: JsonObject): MessageEmbedThumbnail? {
            val url = obj.maybeGetString("url") ?: return null
            val proxyUrl = obj.maybeGetString("proxy_url")
            val height = obj.maybeGetInteger("height")
            val width = obj.maybeGetInteger("width")

            return MessageEmbedThumbnail(
                url,
                proxyUrl,
                height,
                width
            )
        }

        @JvmStatic
        public fun parseVideoJson(obj: JsonObject): MessageEmbedVideo? {
            val url = obj.maybeGetString("url") ?: return null
            val height = obj.maybeGetInteger("height")
            val width = obj.maybeGetInteger("width")

            return MessageEmbedVideo(
                url,
                height,
                width
            )
        }

        @JvmStatic
        public fun parseEmbedProviderJson(obj: JsonObject): MessageEmbedProvider? {
            val name = obj.maybeGetString("name") ?: return null
            val url = obj.maybeGetString("url")

            return MessageEmbedProvider(
                name,
                url
            )
        }

        @JvmStatic
        public fun parseAuthorJson(obj: JsonObject): MessageEmbedAuthor? {
            val name = obj.maybeGetString("name") ?: return null
            val url = obj.maybeGetString("url")
            val iconUrl = obj.maybeGetString("icon_url")
            val proxyIconUrl = obj.maybeGetString("proxy_icon_url")

            return MessageEmbedAuthor(
                name,
                url,
                iconUrl,
                proxyIconUrl
            )
        }

        @JvmStatic
        public fun parseFieldJson(obj: JsonObject): MessageEmbedField? {
            val name = obj.maybeGetString("name") ?: return null
            val value = obj.maybeGetString("value") ?: return null
            val inline = obj.maybeGetBoolean("inline") ?: false

            return MessageEmbedField(
                name,
                value,
                inline
            )
        }
        
    }

}
