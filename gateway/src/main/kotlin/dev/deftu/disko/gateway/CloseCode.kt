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

package dev.deftu.disko.gateway

public enum class CloseCode(
    public val code: Int,
    public val isReconnectable: Boolean,
) {
    UNKNOWN_ERROR(4000, true),
    UNKNOWN_OPCODE(4001, false),
    DECODE_ERROR(4002, false),
    NOT_AUTHENTICATED(4003, false),
    AUTHENTICATION_FAILED(4004, false),
    ALREADY_AUTHENTICATED(4005, true),
    INVALID_SEQ(4007, true),
    RATE_LIMITED(4008, true),
    SESSION_TIMEOUT(4009, true),
    INVALID_SHARD(4010, false),
    SHARDING_REQUIRED(4011, false),
    INVALID_VERSION(4012, false),
    INVALID_INTENT(4013, false),
    DISALLOWED_INTENT(4014, false);

    public companion object {
        public fun fromCode(code: Int): CloseCode =
            @Suppress("EnumValuesSoftDeprecate") // Not all use cases may use newer versions of Kotlin
            values().find { it.code == code } ?: UNKNOWN_ERROR
    }
}
