package dev.deftu.disko.events

import dev.deftu.disko.Disko
import dev.deftu.disko.entities.SelfUser

public data class ReadyEvent(
    override val instance: Disko,
    override val shardId: Int,
    val selfUser: SelfUser
) : Event
