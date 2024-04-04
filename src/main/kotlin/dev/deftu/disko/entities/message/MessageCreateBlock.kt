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

package dev.deftu.disko.entities.message

import dev.deftu.disko.utils.Snowflake
import java.io.File
import java.time.Instant

public class MessageCreateBlock {
    public var content: String? = null
    public var nonce: String? = null
    public var enforceNonce: Boolean = false
    public var tts: Boolean = false

    private val embeds: MutableList<MessageEmbed> = mutableListOf()
    private val stickers: MutableList<Snowflake> = mutableListOf()
    private val files: MutableList<File> = mutableListOf()

    public fun createEmbed(block: EmbedBlock.() -> Unit): MessageEmbed {
        val builder = EmbedBlock()
        builder.block()
        return builder.build()
    }

    public fun embed(block: EmbedBlock.() -> Unit): () -> Unit {
        val embed = createEmbed(block)
        addEmbed(embed)

        return {
            removeEmbed(embed)
        }
    }

    public fun addEmbed(embed: MessageEmbed) {
        embeds.add(embed)
    }

    public fun removeEmbed(embed: MessageEmbed) {
        embeds.remove(embed)
    }

    public operator fun MessageEmbed.unaryPlus() {
        addEmbed(this)
    }

    public operator fun MessageEmbed.unaryMinus() {
        removeEmbed(this)
    }

    public fun stickers(block: StickersBlock.() -> Unit) {
        val builder = StickersBlock()
        builder.block()
        stickers.addAll(builder.newStickers)
        stickers.removeAll(builder.removedStickers)
    }

    public fun files(block: FilesBlock.() -> Unit) {
        val builder = FilesBlock()
        builder.block()
        files.addAll(builder.newFiles)
        files.removeAll(builder.removedFiles)
    }

    public fun build(): MessageCreate {
        return MessageCreate(
            content,
            nonce,
            enforceNonce,
            tts,
            embeds,
            stickers,
            files
        )
    }
}

public class EmbedBlock {
    public class EmbedFooterBlock {
        public var text: String? = null
        public var iconUrl: String? = null
        public var proxyIconUrl: String? = null

        public fun build(): MessageEmbed.MessageEmbedFooter {
            if (text.isNullOrBlank()) throw IllegalArgumentException("Footer text cannot be null or blank")

            return MessageEmbed.MessageEmbedFooter(text!!, iconUrl, proxyIconUrl)
        }
    }

    public class EmbedImageBlock {
        public var url: String? = null
        public var proxyUrl: String? = null
        public var height: Int? = null
        public var width: Int? = null

        public fun build(): MessageEmbed.MessageEmbedImage {
            if (url.isNullOrBlank()) throw IllegalArgumentException("Image URL cannot be null or blank")

            return MessageEmbed.MessageEmbedImage(url!!, proxyUrl, height, width)
        }
    }

    public class EmbedThumbnailBlock {
        public var url: String? = null
        public var proxyUrl: String? = null
        public var height: Int? = null
        public var width: Int? = null

        public fun build(): MessageEmbed.MessageEmbedThumbnail {
            if (url.isNullOrBlank()) throw IllegalArgumentException("Thumbnail URL cannot be null or blank")

            return MessageEmbed.MessageEmbedThumbnail(url!!, proxyUrl, height, width)
        }
    }

    public class EmbedVideoBlock {
        public var url: String? = null
        public var height: Int? = null
        public var width: Int? = null

        public fun build(): MessageEmbed.MessageEmbedVideo {
            if (url.isNullOrBlank()) throw IllegalArgumentException("Video URL cannot be null or blank")

            return MessageEmbed.MessageEmbedVideo(url!!, height, width)
        }
    }

    public class EmbedProviderBlock {
        public var name: String? = null
        public var url: String? = null

        public fun build(): MessageEmbed.MessageEmbedProvider {
            if (name.isNullOrBlank()) throw IllegalArgumentException("Provider name cannot be null or blank")

            return MessageEmbed.MessageEmbedProvider(name!!, url)
        }
    }

    public class EmbedAuthorBlock {
        public var name: String? = null
        public var url: String? = null
        public var iconUrl: String? = null
        public var proxyIconUrl: String? = null

        public fun build(): MessageEmbed.MessageEmbedAuthor {
            if (name.isNullOrBlank()) throw IllegalArgumentException("Author name cannot be null or blank")

            return MessageEmbed.MessageEmbedAuthor(name!!, url, iconUrl, proxyIconUrl)
        }
    }

    public class EmbedFieldBlock {
        public var name: String? = null
        public var value: String? = null
        public var inline: Boolean = false

        public fun build(): MessageEmbed.MessageEmbedField {
            if (name.isNullOrBlank()) throw IllegalArgumentException("Field name cannot be null or blank")
            if (value.isNullOrBlank()) throw IllegalArgumentException("Field value cannot be null or blank")

            return MessageEmbed.MessageEmbedField(name!!, value!!, inline)
        }
    }

    public var title: String? = null
    public var description: String? = null
    public var url: String? = null
    public var timestamp: Instant? = null
    public var color: Int? = null
    public var footer: MessageEmbed.MessageEmbedFooter? = null
    public var image: MessageEmbed.MessageEmbedImage? = null
    public var thumbnail: MessageEmbed.MessageEmbedThumbnail? = null
    public var video: MessageEmbed.MessageEmbedVideo? = null
    public var provider: MessageEmbed.MessageEmbedProvider? = null
    public var author: MessageEmbed.MessageEmbedAuthor? = null
    public val fields: MutableList<MessageEmbed.MessageEmbedField> = mutableListOf()

    public fun footer(block: EmbedFooterBlock.() -> Unit) {
        val builder = EmbedFooterBlock()
        builder.block()
        footer = builder.build()
    }

    public fun image(block: EmbedImageBlock.() -> Unit) {
        val builder = EmbedImageBlock()
        builder.block()
        image = builder.build()
    }

    public fun thumbnail(block: EmbedThumbnailBlock.() -> Unit) {
        val builder = EmbedThumbnailBlock()
        builder.block()
        thumbnail = builder.build()
    }

    public fun video(block: EmbedVideoBlock.() -> Unit) {
        val builder = EmbedVideoBlock()
        builder.block()
        video = builder.build()
    }

    public fun provider(block: EmbedProviderBlock.() -> Unit) {
        val builder = EmbedProviderBlock()
        builder.block()
        provider = builder.build()
    }

    public fun author(block: EmbedAuthorBlock.() -> Unit) {
        val builder = EmbedAuthorBlock()
        builder.block()
        author = builder.build()
    }

    public fun field(block: EmbedFieldBlock.() -> Unit) {
        val builder = EmbedFieldBlock()
        builder.block()
        fields.add(builder.build())
    }

    public fun build(): MessageEmbed {
        return MessageEmbed(
            title,
            MessageEmbed.MessageEmbedType.RICH,
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
}

public class StickersBlock {
    internal val newStickers: MutableList<Snowflake> = mutableListOf()
    internal val removedStickers: MutableList<Snowflake> = mutableListOf()

    public fun add(sticker: Snowflake) {
        newStickers.add(sticker)
    }

    public fun add(sticker: Long) {
        add(Snowflake(sticker))
    }

    public fun remove(sticker: Snowflake) {
        removedStickers.add(sticker)
    }

    public fun remove(sticker: Long) {
        remove(Snowflake(sticker))
    }

    public operator fun Snowflake.unaryPlus() {
        add(this)
    }

    public operator fun Long.unaryMinus() {
        remove(this)
    }

    public operator fun Snowflake.unaryMinus() {
        remove(this)
    }

    public operator fun Long.unaryPlus() {
        add(this)
    }
}

public class FilesBlock {
    internal val newFiles: MutableList<File> = mutableListOf()
    internal val removedFiles: MutableList<File> = mutableListOf()

    public fun add(file: File) {
        newFiles.add(file)
    }

    public fun remove(file: File) {
        removedFiles.add(file)
    }

    public operator fun File.unaryPlus() {
        add(this)
    }

    public operator fun File.unaryMinus() {
        remove(this)
    }
}
