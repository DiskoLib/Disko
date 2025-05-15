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

import dev.deftu.disko.api.utils.ImageFormat
import dev.deftu.disko.rest.CdnUrl
import dev.deftu.disko.rest.CdnUrlFormatter
import kotlin.test.*

class CdnUrlTest {

    private val base = "https://cdn.discordapp.com/avatars/123/abc"

    @Test
    fun `basic toUrl returns default png format with no params`() {
        val url = CdnUrl(base).toUrl()
        assertEquals("$base.png", url)
    }

    @Test
    fun `toUrl with format and size via lambda`() {
        val url = CdnUrl(base).toUrl {
            format = ImageFormat.WEBP
            size = 512
        }

        assertEquals("$base.webp?size=512", url)
    }

    @Test
    fun `toUrl with format and size via Consumer`() {
        val formatter = CdnUrlFormatter()
        formatter.format = ImageFormat.GIF
        formatter.size = 2048

        val url = CdnUrl(base).toUrl(formatter)
        assertEquals("$base.gif?size=2048", url)
    }

    @Test
    fun `throws if size is out of bounds`() {
        val formatter = CdnUrlFormatter()
        val ex = assertFailsWith<IllegalArgumentException> {
            formatter.size = 4097
        }

        assertTrue("Size must be between 16 and 4096" in ex.message.orEmpty())
    }

    @Test
    fun `toString delegates to toUrl`() {
        val cdn = CdnUrl(base)
        assertEquals("$base.png", cdn.toString())
    }

    @Test
    fun `each image format has correct extension`() {
        val formats = listOf(
            ImageFormat.PNG to "png",
            ImageFormat.WEBP to "webp",
            ImageFormat.GIF to "gif",
            ImageFormat.JPEG to "jpeg"
        )

        formats.forEach { (format, ext) ->
            val url = CdnUrl(base).toUrl {
                this.format = format
            }
            assertTrue(url.endsWith(".$ext"), "Expected .$ext in $url")
        }
    }

    @Test
    fun `formatter reuse does not cause side effects`() {
        val formatter = CdnUrlFormatter()
        formatter.format = ImageFormat.WEBP
        formatter.size = 512

        val url1 = CdnUrl(base).toUrl(formatter)
        val url2 = CdnUrl(base).toUrl() // default

        assertEquals("$base.webp?size=512", url1)
        assertEquals("$base.png", url2)
    }

}
