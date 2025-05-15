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

package dev.deftu.disko.api

import dev.deftu.disko.api.DiskoVersion.PreRelease.Companion.compareTo

public class DiskoVersion(version: String) : Comparable<DiskoVersion> {

    public companion object {

        private val REGEX = "(?<major>\\d+).(?<minor>\\d+).?(?<patch>\\d+)?(-(?<preRelease>[a-zA-Z]+)\\+(?<buildMetadata>[a-zA-Z]+))?".toRegex()

        @JvmStatic
        public val instance: DiskoVersion = DiskoVersion(DiskoConstants.VERSION)

    }

    private val cachedVersionInfo: VersionInfo by lazy {
        val match = REGEX.find(version) ?: throw IllegalStateException("Invalid version format")
        val groups = match.groups
        val major = groups["major"]?.value?.toInt() ?: throw IllegalStateException("Invalid version format")
        val minor = groups["minor"]?.value?.toInt() ?: throw IllegalStateException("Invalid version format")
        val patch = groups["patch"]?.value?.toInt() ?: throw IllegalStateException("Invalid version format")
        val preRelease = groups["preRelease"]?.value?.let(PreRelease.Companion::from)
        val buildMetadata = groups["buildMetadata"]?.value
        VersionInfo(major, minor, patch, preRelease, buildMetadata)
    }

    public val major: Int
        get() = cachedVersionInfo.major

    public val minor: Int
        get() = cachedVersionInfo.minor

    public val patch: Int
        get() = cachedVersionInfo.patch

    public val preRelease: PreRelease?
        get() = cachedVersionInfo.preRelease

    public val buildMetadata: String?
        get() = cachedVersionInfo.buildMetadata

    override operator fun compareTo(other: DiskoVersion): Int {
        val majorComparison = major.compareTo(other.major)
        if (majorComparison != 0) {
            return majorComparison
        }

        val minorComparison = minor.compareTo(other.minor)
        if (minorComparison != 0) {
            return minorComparison
        }

        val patchComparison = patch.compareTo(other.patch)
        if (patchComparison != 0) {
            return patchComparison
        }

        val preReleaseComparison = preRelease.compareTo(other.preRelease)
        if (preReleaseComparison != 0) {
            return preReleaseComparison
        }

        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is DiskoVersion) {
            return false
        }

        return compareTo(other) == 0
    }

    override fun hashCode(): Int {
        var result = major
        result = 31 * result + minor
        result = 31 * result + patch
        result = 31 * result + (preRelease?.hashCode() ?: 0)
        result = 31 * result + (buildMetadata?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return buildString {
            append(major)
            append('.')
            append(minor)
            append('.')
            append(patch)

            preRelease?.let {
                append('-')
                append(it.name)
            }

            buildMetadata?.let {
                append('+')
                append(it)
            }
        }
    }

    public enum class PreRelease {

        ALPHA,
        BETA,
        RC,
        RELEASE;

        public companion object {

            internal fun from(value: String): PreRelease? {
                return values().find { release ->
                    release.name.equals(value, ignoreCase = true)
                }
            }

            public fun PreRelease?.compareTo(other: PreRelease?): Int {
                if (this == null && other == null) {
                    return 0
                }

                if (this == null) {
                    return -1
                }

                if (other == null) {
                    return 1
                }

                return ordinal.compareTo(other.ordinal)
            }

        }

    }

    private data class VersionInfo(
        val major: Int,
        val minor: Int,
        val patch: Int,
        val preRelease: PreRelease?,
        val buildMetadata: String?
    )

}
