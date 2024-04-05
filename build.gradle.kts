import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    java
    kotlin("jvm") version("1.9.10")
    val dgt = "1.24.0"
    id("dev.deftu.gradle.tools") version(dgt)
    id("dev.deftu.gradle.tools.dokka") version(dgt)
    id("dev.deftu.gradle.tools.blossom") version(dgt)
    id("dev.deftu.gradle.tools.maven-publishing") version(dgt)
}

val projectDisplayName = project.findProperty("project.displayName") as? String
    ?: throw IllegalStateException("project.displayName is not set")

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    // Networking (OkHttp)
    implementation("com.squareup.okhttp3:okhttp:${libs.versions.okhttp.get()}")

    // Data management (Gson, EnhancedEventBus)
    implementation("com.google.code.gson:gson:${libs.versions.gson.get()}")
    implementation("xyz.deftu:enhancedeventbus:${libs.versions.enhancedeventbus.get()}")

    // Logging (Slf4j)
    implementation("org.slf4j:slf4j-api:${libs.versions.slf4j.get()}")

    //// Testing

    // Logging (Log4j)
    testImplementation("org.apache.logging.log4j:log4j-api:${libs.versions.log4j.get()}")
    testImplementation("org.apache.logging.log4j:log4j-core:${libs.versions.log4j.get()}")
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:${libs.versions.log4j.get()}")
}

kotlin {
    explicitApi()
}

// Documentation

tasks.withType<DokkaTask> {
    dokkaSourceSets {
        named("main") {
            moduleName.set(projectDisplayName)
            moduleVersion.set(projectData.version)
        }
    }

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customStyleSheets = listOf(rootProject.file("dokka.css"))
        footerMessage = "(c) 2024 Deftu and the Disko contributors"
        separateInheritedMembers = true
        mergeImplicitExpectActualDeclarations = true
    }
}
