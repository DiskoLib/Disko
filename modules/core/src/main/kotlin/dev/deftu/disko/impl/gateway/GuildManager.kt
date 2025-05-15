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

package dev.deftu.disko.impl.gateway

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.deftu.disko.api.Disko
import dev.deftu.disko.api.DiskoConstants
import dev.deftu.disko.api.events.ReadyEvent
import dev.deftu.disko.api.events.guild.*
import dev.deftu.disko.api.utils.*
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.impl.gateway.packets.RequestGuildMembersPacket
import org.slf4j.LoggerFactory

public class GuildManager(private val instance: Disko) {

    private companion object {

        private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Guild Manager")

    }

    private var isBotReady = false
    private val unavailableIds = mutableSetOf<Snowflake>()
    private val nodes = mutableMapOf<Snowflake, Node>()

    internal fun markBotReady() {
        logger.trace("Marking bot as ready")
        this.isBotReady = true
    }

    internal fun handleGuildReady(gateway: DiskoGateway, id: Snowflake) {
        logger.trace("Received GUILD_READY event for {}", id)
        val node = Node(instance, this, gateway, id, NodeType.READY)
        this.nodes[id] = node
        this.checkStatus(gateway)
    }

    internal fun handleGuildCreate(gateway: DiskoGateway, id: Snowflake, obj: JsonObject) {
        logger.trace("Received GUILD_CREATE event for {}", id)

        val isAvailable = obj.isNull("unavailable") || !(obj.maybeGetBoolean("unavailable") ?: false)
        if (
            isAvailable &&
            this.unavailableIds.contains(id) &&
            !this.nodes.containsKey(id)
        ) {
            // The guild was marked as unavailable but is now available
            this.unavailableIds.remove(id)
            this.nodes[id] = Node(instance, this, gateway, id, NodeType.AVAILABLE)
            logger.trace("Guild {} may now be available", id)
        }

        var node = this.nodes[id]
        if (node == null) {
            // We joined the guild
            node = Node(instance, this, gateway, id, NodeType.JOINING)
            this.nodes[id] = node
            logger.trace("Might have joined guild {}", id)
        }

        node.handleCreate(id, obj)
        this.checkStatus(gateway)
    }

    internal fun handleGuildDelete(gateway: DiskoGateway, id: Snowflake, obj: JsonObject) {
        logger.trace("Received GUILD_DELETE event for {}", id)

        val isAvailable = obj.isNull("unavailable") || !(obj.maybeGetBoolean("unavailable") ?: false)
        if (isAvailable && unavailableIds.contains(id)) {
            TODO("Guild is available but we left")
        }

        if (!isAvailable && this.unavailableIds.contains(id)) {
            // This is a duplicate GUILD_DELETE event for an unavailable guild
            logger.trace("Received a duplicate GUILD_DELETE event for guild {} (unavailable)", id)
            return
        }

        val guild = this.instance.entityCache.getGuild(id)
        if (guild == null) {
            logger.trace("Received a GUILD_DELETE event for guild {}, which we don't have in the cache. Availability: {}", id, isAvailable)
            this.invalidate(id) // Invalidate anyway, just in case
            return
        }

        setOf(
            GuildDeleteEvent(this.instance, gateway.shard.id, guild),
            when {
                isAvailable -> GuildLeaveEvent(this.instance, gateway.shard.id, guild)
                else -> {
                    this.unavailableIds.add(id)
                    GuildUnavailableEvent(instance, gateway.shard.id, guild)
                }
            }
        ).forEach(this.instance.eventBus::post)

        this.instance.entityCache.removeGuild(guild)
        this.nodes[id]?.markUnavailable()
        this.invalidate(id)
        this.checkStatus(gateway)
    }

    internal fun handleGuildChunk(gateway: DiskoGateway, id: Snowflake, last: Boolean, membersArr: JsonArray) {
        this.nodes[id]?.handleChunk(last, membersArr)
        this.checkStatus(gateway)
    }

    internal fun startChunking(gateway: DiskoGateway, id: Snowflake) {
        gateway.send(RequestGuildMembersPacket(id))
    }

    internal fun postReadyForcibly(gateway: DiskoGateway) {
        this.instance.eventBus.post(ReadyEvent(instance, gateway.shard.id))
    }

    private fun invalidate(id: Snowflake) {
        this.nodes.remove(id)
        this.unavailableIds.add(id)
    }

    private fun checkStatus(gateway: DiskoGateway) {
        if (!this.isBotReady && this.nodes.all { (_, node) -> node.isFinished }) {
            markBotReady()
            postReadyForcibly(gateway)
        }
    }

}

private data class Node(
    private val instance: Disko,
    private val manager: GuildManager,
    private val gateway: DiskoGateway,
    private val id: Snowflake,
    private val type: NodeType
) {

    private val logger = LoggerFactory.getLogger("${DiskoConstants.NAME} Guild Node - $id")

    private var status: NodeStatus = NodeStatus.CREATED
    private var expectedMemberCount = 0
    private var processedMembers = mutableMapOf<Snowflake, JsonObject>()
    private var obj = JsonObject()

    val isChunking: Boolean
        get() = this.status == NodeStatus.CHUNKING

    val isFinished: Boolean
        get() = this.status == NodeStatus.FINISHED

    fun markUnavailable() {
        this.status = NodeStatus.UNAVAILABLE
    }

    fun handleCreate(id: Snowflake, obj: JsonObject) {
        this.obj = obj
        this.expectedMemberCount = obj.maybeGetInteger("member_count") ?: 0

        val initialMemberChunk = obj.maybeGetJsonArray("members") ?: JsonArray()

        logger.trace("Handling initial member chunk for guild {} - Current member count: {}, Expected member count: {}", id, initialMemberChunk.size(), this.expectedMemberCount)

        if (initialMemberChunk.size() < expectedMemberCount && !isChunking) {
            this.manager.startChunking(gateway, id)
            this.status = NodeStatus.CHUNKING
        } else if (handleChunk(false, initialMemberChunk) && !isChunking) {
            this.processedMembers.clear()
            this.manager.startChunking(gateway, id)
            this.status = NodeStatus.CHUNKING
        }
    }

    fun handleChunk(last: Boolean, arr: JsonArray): Boolean {
        for (element in arr) {
            if (!element.isJsonObject) continue

            val obj = element.asJsonObject
            val user = obj.maybeGetJsonObject("user") ?: continue
            val id = user.maybeGetSnowflake("id") ?: continue
            this.processedMembers[id] = obj
        }

        if (last || this.processedMembers.size >= this.expectedMemberCount) {
            logger.trace("Finishing guild {} with {} members", id, this.processedMembers.size)
            finish()
            return false
        }

        logger.trace("Waiting for more chunks for guild {} - Current member count: {}, Expected member count: {}", id, this.processedMembers.size, this.expectedMemberCount)
        return true
    }

    private fun finish() {
        logger.trace("Finishing guild {}", id)

        val guild = instance.entityBuilder.guild(obj)
        if (guild == null) {
            logger.error("Failed to parse guild {}", id)
            return
        }

        this.instance.entityCache.addGuild(guild)
        logger.trace("Added guild {} to cache", id)

        val members = this.processedMembers.values.mapNotNull { memberObj -> instance.entityBuilder.member(null, guild, memberObj) }
        for (member in members) {
            this.instance.entityCache.addMember(member)
            this.instance.entityCache.addUser(member.user)
        }

        logger.trace("Added {} members (and users) to guild {}", members.size, id)

        // Channels
        val channels = obj.maybeGetJsonArray("channels") ?: JsonArray()
        for (element in channels) {
            if (!element.isJsonObject) {
                continue
            }

            val channel = instance.entityBuilder.channel(guild, gateway.shard.id, element.asJsonObject) ?: continue
            instance.entityCache.addChannel(channel)
        }

        logger.trace("Added {} channels to guild {}", channels.size(), id)

        // Threads
        val threads = obj.maybeGetJsonArray("threads") ?: JsonArray()
        for (element in threads) {
            if (!element.isJsonObject) {
                continue
            }

            val thread = instance.entityBuilder.channel(guild, gateway.shard.id, element.asJsonObject) ?: continue
            instance.entityCache.addChannel(thread)
        }

        logger.trace("Added {} threads to guild {}", threads.size(), id)

        setOf(
            GuildCreateEvent(instance, gateway.shard.id, guild),
            when (type) {
                NodeType.READY -> GuildSetupEvent(instance, gateway.shard.id, guild)
                NodeType.AVAILABLE -> GuildAvailableEvent(instance, gateway.shard.id, guild)
                NodeType.JOINING -> GuildJoinEvent(instance, gateway.shard.id, guild)
            }
        ).forEach(instance.eventBus::post)

        logger.trace("Finished guild {}", id)
        this.status = NodeStatus.FINISHED
    }

}

private enum class NodeType {

    READY,
    AVAILABLE,
    JOINING

}

private enum class NodeStatus {

    UNAVAILABLE,
    CREATED,
    CHUNKING,
    FINISHED

}
