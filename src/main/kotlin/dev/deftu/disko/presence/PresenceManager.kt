package dev.deftu.disko.presence

import com.google.gson.JsonElement
import dev.deftu.disko.Disko
import dev.deftu.disko.gateway.packets.PresenceUpdatePacket
import dev.deftu.disko.utils.DataObject
import dev.deftu.disko.utils.add
import dev.deftu.disko.utils.buildJsonArray
import dev.deftu.disko.utils.buildJsonObject

public class PresenceManager(
    private val instance: Disko
) : DataObject {
    private val activities = mutableListOf<Activity>()
    private var status = OnlineStatus.ONLINE
    private var since: Long? = null

    public fun playing(name: String): Activity {
        val activity = Activity(name, ActivityType.PLAYING)
        activities.add(activity)
        updatePresence()
        return activity
    }

    public fun streaming(name: String, url: String): Activity {
        val activity = Activity(name, ActivityType.STREAMING, url)
        activities.add(activity)
        updatePresence()
        return activity
    }

    public fun listening(name: String): Activity {
        val activity = Activity(name, ActivityType.LISTENING)
        activities.add(activity)
        updatePresence()
        return activity
    }

    public fun watching(name: String): Activity {
        val activity = Activity(name, ActivityType.WATCHING)
        activities.add(activity)
        updatePresence()
        return activity
    }

    public fun competing(name: String): Activity {
        val activity = Activity(name, ActivityType.COMPETING)
        activities.add(activity)
        updatePresence()
        return activity
    }

    public fun addActivity(activity: Activity) {
        activities.add(activity)
        updatePresence()
    }

    public fun removeActivity(activity: Activity) {
        activities.remove(activity)
        updatePresence()
    }

    public fun setStatus(status: OnlineStatus) {
        this.status = status
        updatePresence()
    }

    public fun setSince(since: Long?) {
        this.since = since
        updatePresence()
    }

    public fun update(builder: PresenceUpdateBuilder) {
        activities.addAll(builder.activities)
        builder.status?.let { setStatus(it) }
        builder.since?.let { setSince(it) }
    }

    public fun update(callback: PresenceUpdateBuilder.() -> Unit) {
        val builder = PresenceUpdateBuilder()
        builder.apply(callback)
        update(builder)
    }

    private fun updatePresence() {
        if (instance.shardManager.isConnected()) {
            instance.shardManager.forEachShard { shard ->
                shard.send(PresenceUpdatePacket(
                    activities,
                    status,
                    since
                ))
            }
        }
    }

    override fun toJson(): JsonElement = buildJsonObject {
        add("activities", buildJsonArray {
            activities.forEach { add(it.toJson()) }
        })

        add("status", status.status)
        addProperty("since", since)
    }
}

public class PresenceUpdateBuilder {
    internal val activities = mutableListOf<Activity>()
    public var status: OnlineStatus? = null
    public var since: Long? = null

    public operator fun Activity.unaryPlus() {
        activities.add(this)
    }

    public operator fun Activity.unaryMinus() {
        activities.remove(this)
    }

    public fun addPlaying(name: String) {
        activities.add(Activity(name, ActivityType.PLAYING))
    }

    public fun playing(name: String): Activity =
        Activity(name, ActivityType.PLAYING)

    public fun addStreaming(name: String, url: String) {
        activities.add(Activity(name, ActivityType.STREAMING, url))
    }

    public fun streaming(name: String, url: String): Activity =
        Activity(name, ActivityType.STREAMING, url)

    public fun addListening(name: String) {
        activities.add(Activity(name, ActivityType.LISTENING))
    }

    public fun listening(name: String): Activity =
        Activity(name, ActivityType.LISTENING)

    public fun addWatching(name: String) {
        activities.add(Activity(name, ActivityType.WATCHING))
    }

    public fun watching(name: String): Activity =
        Activity(name, ActivityType.WATCHING)

    public fun addCompeting(name: String) {
        activities.add(Activity(name, ActivityType.COMPETING))
    }

    public fun competing(name: String): Activity =
        Activity(name, ActivityType.COMPETING)
}
