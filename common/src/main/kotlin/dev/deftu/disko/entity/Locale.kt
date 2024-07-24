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

package dev.deftu.disko.entity

public enum class Locale(
    public val locale: String,
    public val displayName: String,
    public val nativeName: String
) {

    INDONESIAN("id", "Indonesian", "Bahasa Indonesia"),
    DANISH("da", "Danish", "Dansk"),
    GERMAN("de", "German", "Deutsch"),
    ENGLISH_UK("en-GB", "English, UK", "English, UK"),
    ENGLISH_US("en-US", "English, US", "English, US"),
    SPANISH("es-ES", "Spanish", "Español"),
    SPANISH_LATAM("es-419", "Spanish, LATAM", "Español, LATAM"),
    FRENCH("fr", "French", "Français"),
    CROATIAN("hr", "Croatian", "Hrvatski"),
    ITALIAN("it", "Italian", "Italiano"),
    LITHUANIAN("lt", "Lithuanian", "Lietuviškai"),
    HUNGARIAN("hu", "Hungarian", "Magyar"),
    DUTCH("nl", "Dutch", "Nederlands"),
    NORWEGIAN("no", "Norwegian", "Norsk"),
    POLISH("pl", "Polish", "Polski"),
    PORTUGUESE_BRAZILIAN("pt-BR", "Portuguese, Brazilian", "Português do Brasil"),
    ROMANIAN("ro", "Romanian, Romania", "Română"),
    FINNISH("fi", "Finnish", "Suomi"),
    SWEDISH("sv-SE", "Swedish", "Svenska"),
    VIETNAMESE("vi", "Vietnamese", "Tiếng Việt"),
    TURKISH("tr", "Turkish", "Türkçe"),
    CZECH("cs", "Czech", "Čeština"),
    GREEK("el", "Greek", "Ελληνικά"),
    BULGARIAN("bg", "Bulgarian", "български"),
    RUSSIAN("ru", "Russian", "Pусский"),
    UKRAINIAN("uk", "Ukrainian", "Українська"),
    HINDI("hi", "Hindi", "हिन्दी"),
    THAI("th", "Thai", "ไทย"),
    CHINESE_CHINA("zh-CN", "Chinese, China", "中文"),
    JAPANESE("ja", "Japanese", "日本語"),
    CHINESE_TAIWAN("zh-TW", "Chinese, Taiwan", "繁體中文"),
    KOREAN("ko", "Korean", "한국어");

    public companion object {

        public fun from(value: String): Locale? {
            return entries.firstOrNull { locale ->
                locale.locale == value
            }
        }

    }

}
