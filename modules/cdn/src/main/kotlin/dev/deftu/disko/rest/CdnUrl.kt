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

package dev.deftu.disko.rest

import dev.deftu.disko.api.utils.ImageFormat
import java.util.function.Consumer

@JvmInline
public value class CdnUrl(private val rawUrl: String) {

    public fun toUrl(): String {
        return toUrl(CdnUrlFormatter())
    }

    public fun toUrl(block: CdnUrlFormatter.() -> Unit): String {
        val formatter = CdnUrlFormatter()
        formatter.block()
        return toUrl(formatter)
    }

    public fun toUrl(block: Consumer<CdnUrlFormatter>): String {
        val formatter = CdnUrlFormatter()
        block.accept(formatter)
        return toUrl(formatter)
    }

    public fun toUrl(formatter: CdnUrlFormatter): String {
        val builder = StringBuilder(rawUrl).append(".").append(formatter.format.defaultExtension)
        formatter.size?.let { builder.append("?size=").append(it) }
        return builder.toString()
    }

    override fun toString(): String {
        return toUrl()
    }

}

public class CdnUrlFormatter {

    public var format: ImageFormat = ImageFormat.PNG

    public var size: Int? = null
        set(value) {
            require(value == null || value in 16..4096) { "Size must be between 16 and 4096" }
            field = value
        }

}
