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

package dev.deftu.disko.cache

/**
 * Represents a mutable cache for storing entities, and fetching them by predefined indexes.
 *
 * @param T The type of the entity to store in the cache.
 */
public class Cache<T>(
    private val elements: MutableSet<T> = mutableSetOf(),
    private val indexes: MutableMap<String, Index<Any, T>> = mutableMapOf()
) {
    public class Index<K, E>(
        internal val mapper: (E) -> K
    ) {
        private val map: MutableMap<K, MutableSet<E>> = mutableMapOf()

        public fun add(element: E) {
            val key = mapper(element)
            map.computeIfAbsent(key) { mutableSetOf() }.add(element)
        }

        public fun remove(element: E) {
            val key = mapper(element)
            map[key]?.remove(element)
        }

        public fun get(key: K): Set<E> = map[key] ?: setOf()
    }

    public fun add(element: T) {
        elements.add(element)
        indexes.values.forEach { it.add(element) }
    }

    public fun remove(element: T) {
        elements.remove(element)
        indexes.values.forEach { it.remove(element) }
    }

    public fun findByIndex(indexName: String, key: Any): Set<T> {
        val index = indexes[indexName] ?: throw IllegalArgumentException("Index $indexName does not exist.")
        return index.get(key)
    }

    public fun findFirstByIndex(indexName: String, key: Any): T? {
        val index = indexes[indexName] ?: throw IllegalArgumentException("Index $indexName does not exist.")
        return index.get(key).firstOrNull()
    }

    public fun <K> createIndex(indexName: String, mapper: T.() -> K): Cache<T> = apply {
        if (indexes.containsKey(indexName)) throw IllegalArgumentException("Index $indexName already exists.")
        indexes[indexName] = Index(mapper) as Index<Any, T>
    }
}
