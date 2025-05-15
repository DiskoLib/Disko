import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.deftu.dev/releases/")
        maven("https://maven.deftu.dev/snapshots/")
        maven("https://jitpack.io/")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.architectury.dev/")
        maven("https://repo.essential.gg/repository/maven-public/")
    }
}

rootProject.name = extra["project.name"]?.toString() ?: throw MissingPropertyException("The project name was not configured!")

":common".let { basePath ->
    include(basePath)
    setOf(
        "ws",
        "rest",
    ).map { "$basePath:$it" }.forEach(::include)
}

":modules".let { basePath ->
    include(basePath)
    setOf(
        "core",
        "cdn",
        "webhook",
        "voice",
        "commands",
    ).map { "$basePath:$it" }.forEach(::include)

    "$basePath:gateway".let { gatewayPath ->
        include(gatewayPath)
        setOf(
            "client",
            "sharding",
        ).map { "$gatewayPath:$it" }.forEach(::include)

        "$gatewayPath:packets".let { packetsPath ->
            include(packetsPath)
            setOf(
                "protocol",
                "auto-mod",
                "guild",
                "channel",
                "thread",
                "message"
            ).map { "$packetsPath:$it" }.forEach(::include)
        }
    }
}
