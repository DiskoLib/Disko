package dev.deftu.disko.events

import dev.deftu.disko.Disko

public interface Event {
    public val instance: Disko
    public val shardId: Int
}
