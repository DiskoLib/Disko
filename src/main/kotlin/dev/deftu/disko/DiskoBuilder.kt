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
