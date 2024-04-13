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

import java.time.Instant

public class Snowflake {
    public companion object {
        private const val DISCORD_EPOCH = 1420070400000L

        private const val TIMESTAMP_SHIFT = 22

        private const val WORKER_MASK = 0x3E0000L
        private const val WORKER_SHIFT = 17

        private const val PROCESS_MASK = 0x1F000L
        private const val PROCESS_SHIFT = 12

        private const val INCREMENT_MASK = 0xFFFL

        public val validValues: LongRange = Long.MIN_VALUE..Long.MAX_VALUE
        public val minimum: Snowflake = Snowflake(validValues.first)
        public val maximum: Snowflake = Snowflake(validValues.last)
    }

    public constructor(value: Long) {
        this.value = value.coerceIn(validValues)
    }

    public constructor(value: String) : this(value.toLong())

    public constructor(timestamp: Instant) {
        this.value = timestamp.toEpochMilli()
            .coerceAtLeast(DISCORD_EPOCH)
            .minus(DISCORD_EPOCH)
            .shl(TIMESTAMP_SHIFT)
    }

    public val value: Long
    public val timestamp: Instant
        get() = Instant.ofEpochMilli(DISCORD_EPOCH + (value shr TIMESTAMP_SHIFT))
    public val workerId: Byte
        get() = value.and(WORKER_MASK).shr(WORKER_SHIFT).toByte()
    public val processId: Byte
        get() = value.and(PROCESS_MASK).shr(PROCESS_SHIFT).toByte()
    public val increment: Short
        get() = value.and(INCREMENT_MASK).toShort()

    public operator fun component1(): Long = value
    public operator fun component2(): Instant = timestamp
    public operator fun component3(): Byte = workerId
    public operator fun component4(): Byte = processId
    public operator fun component5(): Short = increment

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is Long) return value == other
        if (other !is Snowflake) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = value.toString()

    public operator fun compareTo(other: Snowflake): Int = value.compareTo(other.value)
    public operator fun compareTo(other: Long): Int = value.compareTo(other)
}
