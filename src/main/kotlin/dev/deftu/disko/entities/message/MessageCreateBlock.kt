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

public class MessageCreateBlock {
    public var content: String? = null
    public var nonce: String? = null
    public var enforceNonce: Boolean = false
    public var tts: Boolean = false

    private val embeds: MutableList<MessageEmbed> = mutableListOf()
    private val stickers: MutableList<Snowflake> = mutableListOf()
    private val files: MutableList<File> = mutableListOf()

    // TODO

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
