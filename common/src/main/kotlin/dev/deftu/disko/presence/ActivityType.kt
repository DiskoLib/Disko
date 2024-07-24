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

package dev.deftu.disko.presence

/**
 * Enumerable value for all possible types of activities supported within Discord.
 *
 * @since 0.1.0
 * @author Deftu
 */
public enum class ActivityType {

    /**
     * The user is playing a game.
     */
    PLAYING,

    /**
     * The user is streaming to either YouTube or Twitch - May be a custom URL for bots.
     */
    STREAMING,

    /**
     * The user is listening to something.
     */
    LISTENING,

    /**
     * The user is watching something.
     */
    WATCHING,

    /**
     * The user has a custom status.
     */
    CUSTOM,

    /**
     * The user is competing in a game.
     */
    COMPETING,

}
