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

package dev.deftu.disko.webhook

import dev.deftu.disko.entity.message.Message
import dev.deftu.disko.entity.message.MessageEmbed
import dev.deftu.disko.utils.Snowflake
import java.io.File

public class WebhookMessageCreateBuilder {

    public var content: String? = null

    public var nonce: String? = null

    public var enforceNonce: Boolean = false

    public var tts: Boolean = false

    public var embeds: MutableList<MessageEmbed> = mutableListOf()

    public var stickerIds: MutableList<Snowflake> = mutableListOf()

    public var files: MutableList<File> = mutableListOf()

    public fun addEmbed(embed: MessageEmbed): WebhookMessageCreateBuilder = apply {
        embeds.add(embed)
    }

    public fun removeEmbed(embed: MessageEmbed): WebhookMessageCreateBuilder = apply {
        embeds.remove(embed)
    }

    public fun addStickerId(stickerId: Snowflake): WebhookMessageCreateBuilder = apply {
        stickerIds.add(stickerId)
    }

    public fun removeStickerId(stickerId: Snowflake): WebhookMessageCreateBuilder = apply {
        stickerIds.remove(stickerId)
    }

    public fun addFile(file: File): WebhookMessageCreateBuilder = apply {
        files.add(file)
    }

    public fun removeFile(file: File): WebhookMessageCreateBuilder = apply {
        files.remove(file)
    }

    public fun build(): WebhookMessageCreate {
        return WebhookMessageCreate(content, nonce, enforceNonce, tts, embeds, stickerIds, files)
    }

}

public fun webhookMessage(block: WebhookMessageCreateBuilder.() -> Unit): WebhookMessageCreate {
    return WebhookMessageCreateBuilder().apply(block).build()
}

public fun Webhook.send(block: WebhookMessageCreateBuilder.() -> Unit): Message? {
    return send(webhookMessage(block))
}

public operator fun WebhookMessageCreateBuilder.plusAssign(embed: MessageEmbed) {
    addEmbed(embed)
}

public operator fun WebhookMessageCreateBuilder.minusAssign(embed: MessageEmbed) {
    removeEmbed(embed)
}

public operator fun WebhookMessageCreateBuilder.plusAssign(stickerId: Snowflake) {
    addStickerId(stickerId)
}

public operator fun WebhookMessageCreateBuilder.minusAssign(stickerId: Snowflake) {
    removeStickerId(stickerId)
}

public operator fun WebhookMessageCreateBuilder.plusAssign(file: File) {
    addFile(file)
}

public operator fun WebhookMessageCreateBuilder.minusAssign(file: File) {
    removeFile(file)
}
