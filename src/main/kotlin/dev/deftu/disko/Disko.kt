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

package dev.deftu.disko

import dev.deftu.disko.cache.ChannelCache
import dev.deftu.disko.cache.GuildCache
import dev.deftu.disko.cache.MemberCache
import dev.deftu.disko.cache.UserCache
import dev.deftu.disko.entities.EntityConstructor
import dev.deftu.disko.entities.DefaultEntityConstructor
import dev.deftu.disko.entities.SelfUser
import dev.deftu.disko.entities.channel.VoiceRegions
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.DefaultDiskoGateway
import dev.deftu.disko.shards.ShardManager
import dev.deftu.disko.gateway.intents.IntentManager
import dev.deftu.disko.presence.PresenceManager
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import xyz.deftu.enhancedeventbus.EventBus
import xyz.deftu.enhancedeventbus.bus
import xyz.deftu.enhancedeventbus.invokers.LMFInvoker

public class Disko(
    block: DiskoBuilder.() -> Unit = {}
) {
    public companion object {
        private val logger = LoggerFactory.getLogger(DiskoConstants.NAME)
    }

    public val eventBus: EventBus = bus {
        invoker = LMFInvoker()
        threadSafety = true
    }

    private var opened: Boolean = false
    internal lateinit var token: String
    public val gatewayMetadata: GatewayMetadata = GatewayMetadata(this)
    public val discordBaseUrl: String
        get() = "https://discord.com/api/v${gatewayMetadata.getApiVersion().value}"
    public lateinit var httpClient: OkHttpClient
        private set

        private set
    public var selfUser: SelfUser? = null
        internal set

    public val userCache: UserCache = UserCache()
    public val guildCache: GuildCache = GuildCache()
    public val memberCache: MemberCache = MemberCache()
    public val channelCache: ChannelCache = ChannelCache()

    internal var gatewayBuilder: (Disko, Int) -> DiskoGateway = { instance, shardId -> DefaultDiskoGateway(instance, shardId) }
    internal val shardManager = ShardManager(this)
    public val voiceRegions: VoiceRegions = VoiceRegions(this)
    public val presenceManager: PresenceManager = PresenceManager(this)
    public val intentManager: IntentManager = IntentManager(this)

    public constructor() : this({})

    init {
        DiskoBuilder()
            .apply(block)
            .applyTo(this)
    }

    public fun login(token: String) {
        this.opened = true
        requireHttpClient()
        this.token = token
        shardManager.login()
    }

    public fun setApiVersion(version: ApiVersion) {
        gatewayMetadata.setApiVersion(version)
    }

    public fun setHttpClient(httpClient: OkHttpClient) {
        if (opened) throw IllegalStateException("Cannot set HTTP client after login!")
        this.httpClient = httpClient
    }

    public fun setEntityConstructor(constructor: EntityConstructor) {
        if (opened) throw IllegalStateException("Cannot set entity constructor after login!")
        this.entityConstructor = constructor
    }

    public fun setGatewayBuilder(builder: (Disko, Int) -> DiskoGateway) {
        if (opened) throw IllegalStateException("Cannot set gateway builder after login!")
        this.gatewayBuilder = builder
    }

    private fun requireHttpClient() {
        if (!::httpClient.isInitialized) {
            logger.info("HTTP client doesn't exist yet, creating one...")
            httpClient = OkHttpClient.Builder()
                .addInterceptor {
                    it.proceed(
                        it.request()
                            .newBuilder()
                            .addHeader("User-Agent", "${DiskoConstants.NAME}/${DiskoConstants.VERSION}")
                            .build()
                    )
                }.build()
        }
    }
}
