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

public interface BitsetFlag {
    public interface BitsetFlagCompanion<T : BitsetFlag> {
        public val values: Array<T>

        public fun fromBit(bit: Int): T? =
            values.firstOrNull { item -> bit.toLong() and item.mask != 0L }

        public fun fromBitset(bits: Int): List<T> =
            values.filter { item -> bits.toLong() and item.mask != 0L }

        public fun fromBitset(bits: Long): List<T> =
            values.filter { item -> bits and item.mask != 0L }

        public fun toBitset(items: List<T>): Long =
            items.fold(0L) { acc, item -> acc or item.mask }

        public fun isPresent(bits: Int, item: T): Boolean =
            bits.toLong() and item.mask != 0L

        public fun isPresent(bits: Long, item: T): Boolean =
            bits and item.mask != 0L
    }

    public val offset: Int

    public val mask: Long
        get() = 1L shl offset

    public fun isSet(flags: Long): Boolean =
        flags and mask != 0L
}
