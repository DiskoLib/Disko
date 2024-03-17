package dev.deftu.disko.gateway.intents

import dev.deftu.disko.Disko

public class IntentManager(
    private val instance: Disko
) {
    private val intents: MutableList<GatewayIntent> = mutableListOf()

    public fun add(intent: GatewayIntent): Unit = ensureNotConnected {
        intents.add(intent)
    }

    public fun add(vararg intents: GatewayIntent): Unit = ensureNotConnected {
        this.intents.addAll(intents)
    }

    public fun add(intents: Collection<GatewayIntent>): Unit = ensureNotConnected {
        this.intents.addAll(intents)
    }

    public operator fun GatewayIntent.unaryPlus(): Unit = add(this)

    public operator fun Collection<GatewayIntent>.unaryPlus(): Unit = add(this)

    public operator fun Array<GatewayIntent>.unaryPlus(): Unit = add(*this)

    public fun remove(intent: GatewayIntent): Unit = ensureNotConnected {
        intents.remove(intent)
    }

    public fun remove(vararg intents: GatewayIntent): Unit = ensureNotConnected {
        this.intents.removeAll(intents)
    }

    public fun remove(intents: Collection<GatewayIntent>): Unit = ensureNotConnected {
        this.intents.removeAll(intents)
    }

    public operator fun GatewayIntent.unaryMinus(): Unit = remove(this)

    public operator fun Collection<GatewayIntent>.unaryMinus(): Unit = remove(this)

    public operator fun Array<GatewayIntent>.unaryMinus(): Unit = remove(*this)

    public fun clear(): Unit = ensureNotConnected {
        intents.clear()
    }

    public fun get(): List<GatewayIntent> = intents.toList()

    private fun ensureNotConnected(block: () -> Unit) {
        if (!instance.shardManager.isConnected()) {
            block()
        } else throw IllegalStateException("Cannot modify intents while connected to the gateway")
    }
}
