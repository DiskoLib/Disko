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

include(
    "common",   // WIP         [1]
    "core",     // Unstarted
    "cdn",      // Finished    [3]
    "webhook",  // WIP         [4]
    "gateway",  // Finished    [2]
    "voice",    // Unstarted
    "commands"  // Unstarted
)
