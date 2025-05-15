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

import dev.deftu.disko.api.entity.EntityBuilder
import dev.deftu.disko.api.entity.user.SelfUser
import dev.deftu.disko.impl.entity.EntityCache
import dev.deftu.disko.api.exceptions.MultipleLoginException
import dev.deftu.disko.gateway.*
import dev.deftu.disko.gateway.presence.PresenceUpdateBuilder
import dev.deftu.disko.api.utils.ApiVersion
import dev.deftu.disko.impl.entity.EntityBuilderImpl
import dev.deftu.disko.impl.gateway.CoreGatewayExtension
import dev.deftu.disko.impl.gateway.GuildManager
import dev.deftu.disko.impl.utils.VoiceRegionPopulator
import dev.deftu.eventbus.EventBus
import dev.deftu.eventbus.bus
import dev.deftu.eventbus.invokers.LMFInvoker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

/**
 * The main class for Disko, which is used to interact with the Discord API.
 *
 * This is essentially a wrapper around Disko's gateway, CDN and webhook modules which individually implement their respective functionalities for the Discord API.
 *
 * The core module, in which this class is located, is responsible for implementing all non-critical functionalities of the Discord API, such as caching, event handling, etc.
 *
 * @param coroutineContext The coroutine context to use for Disko's operations.
 * @param token The bot token to use for authentication with the Discord API.
 * @param intents The intents to use for the bot's connection to the Discord API.
 *
 * @see CoroutineContext
 * @see GatewayIntent
 * @see CoroutineScope
 * @see EventBus
 * @see DiskoGateway
 * @see Sharder
 *
 * @since 0.1.0
 * @author Deftu
 */
public class Disko(
    override val coroutineContext: CoroutineContext,
    internal val token: String,
    intents: Set<GatewayIntent> = emptySet(),
) : CoroutineScope {

    public companion object {

        internal val logger = LoggerFactory.getLogger(DiskoConstants.NAME)

        /**
         * An empty request body, which is used for requests that do not require a body.
         *
         * This is useful for requests such as DELETE requests, where a body is not required.
         *
         * @see RequestBody
         *
         * @since 0.1.0
         * @author Deftu
         */
        public val EMPTY_REQUEST_BODY: RequestBody = ByteArray(0).toRequestBody()

    }

    internal val guildManager = GuildManager(this)

    /**
     * Disko's event bus, where all user-facing events are dispatched, such as message events, guild events, etc.
     * The user may also dispatch their own events via this bus for their own purposes safely.
     *
     * @see EventBus
     * @see LMFInvoker
     * @see bus
     *
     * @since 0.1.0
     * @author Deftu
     */
    public val eventBus: EventBus = bus {
        invoker = LMFInvoker()
        threadSafety = true
    }

    /**
     * An [OkHttpClient] instance which is used for all HTTP and WebSocket requests made by Disko.
     *
     * This instance is created with the optimal settings for Disko, as outlined by [DiskoGateway.createOptimalHttpClient].
     *
     * @see DiskoGateway.createOptimalHttpClient
     * @see OkHttpClient
     *
     * @since 0.1.0
     * @author Deftu
     */
    public val httpClient: OkHttpClient = DiskoGateway.createOptimalHttpClient()

    /**
     * The sharder instance used by Disko to manage all shards.
     *
     * This instance is created with the provided coroutine context, bot token, HTTP client and intents.
     *
     * @see Sharder
     *
     * @since 0.1.0
     * @author Deftu
     */
    public val sharder: Sharder = Sharder(coroutineContext, token, httpClient, Sharder.usingDefaultGatewayBuilder(intents))

    /**
     * The entity builder used by Disko to create entities such as users, guilds, channels, etc.
     *
     * @see EntityBuilder
     *
     * @since 0.1.0
     * @author Deftu
     */
    public val entityBuilder: EntityBuilder = EntityBuilderImpl(this)

    /**
     * The entity cache used by Disko to store entities such as users, guilds, channels, etc.
     *
     * This cache is used to store entities that are received from the Discord API, such as through events, and is used to retrieve entities quickly.
     *
     * @see EntityCache
     *
     * @since 0.1.0
     * @author Deftu
     */
    public val entityCache: EntityCache = EntityCache()

    /**
     * The self user of the bot, which is received from the READY event.
     *
     * This is used to store information about the bot, such as it's ID, username, discriminator, etc.
     *
     * @see SelfUser
     *
     * @since 0.1.0
     * @author Deftu
     */
    public lateinit var selfUser: SelfUser
        internal set

    /**
     * Creates a new Disko instance with the provided bot token and intents.
     *
     * Uses the default coroutine context, which is [Dispatchers.Default] with a [SupervisorJob].
     *
     * @param token The bot token to use for authentication with the Discord API.
     * @param intents The intents to use for the bot's connection to the Discord API.
     *
     * @since 0.1.0
     * @author Deftu
     */
    public constructor(
        token: String,
        intents: Set<GatewayIntent> = emptySet()
    ) : this(
        coroutineContext = Dispatchers.Default + SupervisorJob(),
        token = token,
        intents
    )

    /**
     * Sets up the core modules' events, packets, etc. and then logs into the Discord API.
     *
     * Disko's core module automatically uses the sharder implementation provided by the gateway module, meaning there is no need for you to manually manage shards.
     *
     * @see Sharder
     * @see ApiVersion.latest
     *
     * @since 0.1.0
     * @author Deftu
     */
    public fun login() {
        if (sharder.isStarted) {
            throw MultipleLoginException("Cannot login to Discord multiple times using the same Disko instance.")
        }

        // Populate our cache with initial voice regions
        logger.info("Populating voice regions...")
        VoiceRegionPopulator.populate(httpClient, entityBuilder, entityCache)

        // Populate our shards
        val shardCount = sharder.populateShards(token)
        logger.info("Set up $shardCount shard(s).")

        // Register packets
        logger.info("Registering core packets.")
        CoreGatewayExtension.registerOn(this, sharder)

        // Allow the sharder to log in.
        logger.info("Logging into Discord with API ${ApiVersion.latest.formatted}...")
        sharder.loginManually()
    }

    /**
     * Sets the presence of the bot across all shards.
     *
     * @see PresenceUpdateBuilder
     *
     * @since 0.1.0
     * @author Deftu
     */
    public fun setPresence(builder: PresenceUpdateBuilder.() -> Unit) {
        sharder.forEach { _, gateway ->
            gateway.setPresence(builder)
        }
    }

}
