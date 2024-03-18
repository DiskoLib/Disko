package dev.deftu.disko

import dev.deftu.disko.entities.EntityConstructor
import dev.deftu.disko.entities.DefaultEntityConstructor
import dev.deftu.disko.entities.SelfUser
import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.DiskoGatewayImpl
import dev.deftu.disko.shards.ShardManager
import dev.deftu.disko.gateway.intents.IntentManager
import dev.deftu.disko.presence.PresenceManager
import dev.deftu.disko.presence.PresenceUpdateBuilder
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import xyz.deftu.enhancedeventbus.EventBus
import xyz.deftu.enhancedeventbus.bus
import xyz.deftu.enhancedeventbus.invokers.LMFInvoker

public class Disko(
    private val block: DiskoBuilder.() -> Unit = {}
) {
    public companion object {
        private val logger = LoggerFactory.getLogger(DiskoConstants.NAME)
    }

    public class DiskoBuilder {
        public var httpClient: OkHttpClient? = null
        public var gatewayBuilder: ((Disko, Int) -> DiskoGateway)? = null

        private var intentsBlock: IntentManager.() -> Unit = {}
        private var presenceBlock: PresenceUpdateBuilder.() -> Unit = {}

        public fun intents(block: IntentManager.() -> Unit) {
            intentsBlock = block
        }

        public fun presence(block: PresenceUpdateBuilder.() -> Unit) {
            presenceBlock = block
        }

        public fun build(): Disko =
            Disko().also { applyTo(it) }

        internal fun applyTo(instance: Disko) {
            if (httpClient != null) instance.setHttpClient(httpClient!!)
            if (gatewayBuilder != null) instance.setGatewayBuilder(gatewayBuilder!!)
            instance.intentManager.apply(intentsBlock)
            instance.presenceManager.update(presenceBlock)
        }
    }

    public val eventBus: EventBus = bus {
        invoker = LMFInvoker()
        threadSafety = true
    }

    private var opened: Boolean = false
    internal lateinit var token: String
    public val gatewayMetadata: GatewayMetadata = GatewayMetadata(this)
    public lateinit var httpClient: OkHttpClient
        private set

    public var entityConstructor: EntityConstructor = DefaultEntityConstructor
        private set
    public var selfUser: SelfUser? = null
        internal set
    internal val shardManager = ShardManager(this)
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
