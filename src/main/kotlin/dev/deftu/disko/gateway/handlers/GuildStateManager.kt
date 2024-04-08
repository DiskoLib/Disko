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

package dev.deftu.disko.gateway.handlers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.deftu.disko.DiskoConstants
import dev.deftu.disko.events.*
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.utils.*
import org.slf4j.LoggerFactory

public class GuildStateManager(
    private val listener: DiskoGateway
) {
    private companion object {
        private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Guild StateManager")
    }

    private var isReady = false
    private val unavailableGuilds = mutableSetOf<Snowflake>()
    private val pendingChunkRequests = mutableSetOf<Snowflake>()
    private val guilds = mutableMapOf<Snowflake, GuildStateNode>()

    public fun markReady() {
        isReady = true
    }

    public fun handleReady(id: Snowflake) {
        val node = GuildStateNode(this, listener, id, GuildStateNode.NodeType.READY)
        guilds[id] = node
        checkReady()
    }

    public fun handleCreate(
        id: Snowflake,
        json: JsonObject
    ) {
        logger.debug("Received GUILD_CREATE event for guild {}", id)
        val isAvailable = json.isNull("unavailable") || !(json.maybeGetBoolean("unavailable") ?: false)
        if (
            isAvailable &&
            unavailableGuilds.contains(id) &&
            !guilds.containsKey(id)
        ) {
            // The guild was marked as unavailable but is now available
            unavailableGuilds.remove(id)
            guilds[id] = GuildStateNode(this, listener, id, GuildStateNode.NodeType.AVAILABLE)
            logger.debug("Guild {} may now be available", id)
        }

        var node = guilds[id]
        if (node == null) {
            // We joined the guild
            node = GuildStateNode(this, listener, id, GuildStateNode.NodeType.JOIN)
            guilds[id] = node
            logger.debug("Might have joined guild {}", id)
        }

        node.handleCreate(id, json)
        checkReady()
    }

    public fun handleDelete(
        id: Snowflake,
        json: JsonObject
    ) {
        val isAvailable = json.isNull("unavailable") || !(json.maybeGetBoolean("unavailable") ?: false)
        if (
            isAvailable &&
            unavailableGuilds.contains(id)
        ) {
            // The unavailable field was non-present because the user was removed from or left the unavailable guild
            listener.instance.eventBus.post(UnavailableGuildLeaveEvent(listener.instance, listener.shardId, id))
            return
        }

        if (!isAvailable && unavailableGuilds.contains(id)) {
            // This is a duplicate GUILD_DELETE event for an unavailable guild
            logger.debug("Received a duplicate GUILD_DELETE event for guild {} (unavailable)", id)
            return
        }

        invalidate(id)
        val guild = listener.instance.guildCache.getGuild(id)
        if (guild == null) {
            logger.debug("Received a GUILD_DELETE event for guild {}, which we don't have in the cache. Availability: {}", id, isAvailable)
            return
        }

        listener.instance.eventBus.post(when (isAvailable) {
            true -> GuildLeaveEvent(listener.instance, listener.shardId, guild)
            false -> {
                unavailableGuilds.add(id)
                GuildUnavailableEvent(listener.instance, listener.shardId, guild)
            }
        })

        listener.instance.guildCache.removeGuild(guild)
        guilds[id]?.markUnavailable()
        invalidate(id)
        checkReady()
    }

    public fun handleChunk(
        id: Snowflake,
        lastChunk: Boolean,
        json: JsonArray
    ) {
        guilds[id]?.handleChunk(lastChunk, json)
        checkReady()
    }

    internal fun chunkGuild(
        id: Snowflake,
        isJoin: Boolean
    ) {
        listener.chunkGuild(id, isJoin)
    }

    private fun invalidate(id: Snowflake) {
        guilds.remove(id)
        pendingChunkRequests.remove(id)
        unavailableGuilds.add(id)
    }

    private fun checkReady() {
        if (!isReady && guilds.all { (_, node) ->
            node.isFinished
        }) {
            markReady()
            listener.instance.eventBus.post(ReadyEvent(listener.instance, listener.shardId))
        }
    }
}

private class GuildStateNode(
    private val manager: GuildStateManager,
    private val listener: DiskoGateway,
    private val id: Snowflake,
    private val type: NodeType
) {
    private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Guild State Node ($id)")

    enum class NodeType {
        READY,
        AVAILABLE,
        JOIN
    }

    enum class NodeStatus {
        UNAVAILABLE,
        CREATED,
        CHUNKING,
        FINISHED;
    }

    private var status = NodeStatus.CREATED
    private var json = JsonObject()
    private var expectedMemberCount = 0
    private var processedMembers = mutableMapOf<Snowflake, JsonObject>()
    val isJoin: Boolean
        get() = type == NodeType.JOIN
    val isChunking: Boolean
        get() = status == NodeStatus.CHUNKING
    val isFinished: Boolean
        get() = status == NodeStatus.FINISHED

    fun markUnavailable() {
        status = NodeStatus.UNAVAILABLE
    }

    fun handleCreate(
        id: Snowflake,
        json: JsonObject
    ) {
        this.json = json
        this.expectedMemberCount = json.maybeGetInteger("member_count") ?: 0
        val initialMembers = json.maybeGetJsonArray("members") ?: JsonArray()

        if (!listener.instance.shouldChunk) {
            logger.debug("Not chunking {} - Will use initial members", id)
            handleChunk(true, initialMembers)
        } else if (initialMembers.size() < expectedMemberCount && !isChunking) {
            logger.debug("Chunking {} - Check 1 - Initial members size: {}, Expected member count: {}", id, initialMembers.size(), expectedMemberCount)
            manager.chunkGuild(id, isJoin)
            status = NodeStatus.CHUNKING
        } else if (handleChunk(false, initialMembers) && !isChunking) {
            logger.debug("Chunking {} - Check 2 - Initial members size: {}, Expected member count: {}", id, initialMembers.size(), expectedMemberCount)
            this.processedMembers.clear()
            manager.chunkGuild(id, isJoin)
            status = NodeStatus.CHUNKING
        }
    }

    fun handleChunk(
        lastChunk: Boolean,
        json: JsonArray
    ): Boolean {
        for (element in json) {
            if (!element.isJsonObject) continue

            val obj = element.asJsonObject
            val user = obj.maybeGetJsonObject("user") ?: continue
            val id = user.maybeGetSnowflake("id") ?: continue
            this.processedMembers[id] = obj
        }

        if (
            lastChunk ||
            this.processedMembers.size >= this.expectedMemberCount ||
            !listener.instance.shouldChunk
        ) {
            logger.debug("Finishing guild {} with {} members", id, this.processedMembers.size)
            finish()
            return false
        }

        logger.debug("Waiting for more chunks for guild {} - Current member count: {}, Expected member count: {}", id, this.processedMembers.size, this.expectedMemberCount)
        return true
    }

    private fun finish() {
        logger.debug("Finishing guild {}", id)
        val guild = listener.instance.entityConstructor.constructGuild(json)
        if (guild == null) {
            logger.warn("Failed to construct guild from JSON: {}", json)
            return
        }

        listener.instance.guildCache.addGuild(guild)

        val members = this.processedMembers.values.mapNotNull { listener.instance.entityConstructor.constructMember(null, guild, it) }
        for (member in members) {
            listener.instance.memberCache.addMember(member)
            listener.instance.userCache.addUser(member.user)
        }

        val channels = json.maybeGetJsonArray("channels") ?: JsonArray()
        for (element in channels) {
            if (!element.isJsonObject) continue

            val channel = listener.instance.entityConstructor.constructChannel(listener.shardId, guild, element.asJsonObject) ?: continue
            listener.instance.channelCache.addChannel(channel)
        }

        listener.instance.eventBus.post(when (type) {
            NodeType.READY -> GuildSetupEvent(listener.instance, listener.shardId, guild)
            NodeType.AVAILABLE -> GuildAvailableEvent(listener.instance, listener.shardId, guild)
            NodeType.JOIN -> GuildJoinEvent(listener.instance, listener.shardId, guild)
        })

        logger.debug("Finished guild {} - JSON: {}", id, json)
        status = NodeStatus.FINISHED
    }
}
