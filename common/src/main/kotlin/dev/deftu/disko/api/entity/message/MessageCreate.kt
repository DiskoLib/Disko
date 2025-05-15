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
import dev.deftu.disko.api.entity.channel.MessageChannel
import dev.deftu.disko.api.utils.Snowflake
import dev.deftu.disko.api.utils.add
import dev.deftu.disko.api.utils.buildJsonArray
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

public data class MessageCreate(
    public val content: String?,
    public val nonce: String?,
    public val isEnforceNonce: Boolean,
    public val isTts: Boolean,
    public val embeds: List<MessageEmbed>,
    // TODO - public val allowedMentions: AllowedMentions,
    // TODO - public val messageReference: MessageReference,
    // TODO - public val components: List<MessageComponent>,
    public val stickerIds: List<Snowflake>,
    public val files: List<File>
) {

    public fun createRequestBody(): RequestBody {
        val json = JsonObject()
        content?.let { json.add("content", it) }
        nonce?.let { json.add("nonce", it) }
        json.add("tts", isTts)
        json.add("embeds", buildJsonArray { embeds.map(MessageEmbed::toJson).forEach { add(it) } })
        json.add("sticker_ids", buildJsonArray { stickerIds.map(Snowflake::value).forEach { add(it) } })

        return if (files.isEmpty()) {
            json.toString().toRequestBody("application/json".toMediaType())
        } else {
            val formData = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("payload_json", json.toString())
            files.forEachIndexed { index, file -> formData.addFormDataPart("file$index", file.name, file.asRequestBody("application/octet-stream".toMediaType())) }
            formData.build()
        }
    }

}

public class MessageCreateBuilder {

    public var content: String? = null

    public var nonce: String? = null

    public var enforceNonce: Boolean = false

    public var tts: Boolean = false

    public var embeds: MutableList<MessageEmbed> = mutableListOf()

    public var stickerIds: MutableList<Snowflake> = mutableListOf()

    public var files: MutableList<File> = mutableListOf()

    public fun addEmbed(embed: MessageEmbed): MessageCreateBuilder = apply {
        embeds.add(embed)
    }

    public fun removeEmbed(embed: MessageEmbed): MessageCreateBuilder = apply {
        embeds.remove(embed)
    }

    public fun addStickerId(stickerId: Snowflake): MessageCreateBuilder = apply {
        stickerIds.add(stickerId)
    }

    public fun removeStickerId(stickerId: Snowflake): MessageCreateBuilder = apply {
        stickerIds.remove(stickerId)
    }

    public fun addFile(file: File): MessageCreateBuilder = apply {
        files.add(file)
    }

    public fun removeFile(file: File): MessageCreateBuilder = apply {
        files.remove(file)
    }

    public fun build(): MessageCreate {
        return MessageCreate(content, nonce, enforceNonce, tts, embeds, stickerIds, files)
    }

}

public fun message(block: MessageCreateBuilder.() -> Unit): MessageCreate {
    return MessageCreateBuilder().apply(block).build()
}

public fun MessageChannel.send(block: MessageCreateBuilder.() -> Unit) {
    send(message(block))
}

public operator fun MessageCreateBuilder.plusAssign(embed: MessageEmbed) {
    addEmbed(embed)
}

public operator fun MessageCreateBuilder.minusAssign(embed: MessageEmbed) {
    removeEmbed(embed)
}

public operator fun MessageCreateBuilder.plusAssign(stickerId: Snowflake) {
    addStickerId(stickerId)
}

public operator fun MessageCreateBuilder.minusAssign(stickerId: Snowflake) {
    removeStickerId(stickerId)
}

public operator fun MessageCreateBuilder.plusAssign(file: File) {
    addFile(file)
}

public operator fun MessageCreateBuilder.minusAssign(file: File) {
    removeFile(file)
}
