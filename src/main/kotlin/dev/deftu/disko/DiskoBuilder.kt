package dev.deftu.disko

import dev.deftu.disko.gateway.DiskoGateway
import dev.deftu.disko.gateway.intents.IntentManager
import dev.deftu.disko.presence.PresenceUpdateBuilder
import okhttp3.OkHttpClient

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
