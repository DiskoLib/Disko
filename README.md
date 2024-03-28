# Disko
A Discord API wrapper built for Kotlin developers. Building towards a beautiful, clean and fully featured wrapping of Discord's REST and Gateway APIs, allowing you to create Discord Bots and Applications with ease in Kotlin.

## Table of Contents
1. [Setup](#setup)
2. [Basic Usage](#basic-usage)
3. [Contributing](#contributing)
4. [A Note on Sharding](#a-note-on-sharding)
5. [A Note on Self-Bots](#a-note-on-self-bots)
6. [Related Projects](#related-projects)

## Setup
To use Disko in your project, add the following to your relevant build file:

<details>
    <summary>Gradle</summary>

```kotlin
repositories {
    maven { url = "https://maven.deftu.dev/releases" }
}

dependencies {
    implementation("dev.deftu:disko:<VERSION>")
}
```

</details>

<details>
    <summary>Kotlin DSL</summary>

```kotlin
repositories {
    maven("https://maven.deftu.dev/releases")
}

dependencies {
    implementation("dev.deftu:disko:<VERSION>")
}
```

</details>

<details>
    <summary>Maven</summary>

```xml
<repositories>
    <repository>
        <id>deftu-repo</id>
        <url>https://maven.deftu.dev/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>dev.deftu</groupId>
        <artifactId>disko</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

</details>

## Basic Usage
```kotlin
import dev.deftu.disko.Disko

fun main() {
    val disko = Disko {
        intents {
            +GatewayIntent.MESSAGE_CONTENT
        }
        
        presence {
            status = OnlineStatus.DND
            +playing("with Disko")
        }
    }
    
    disko.login("YOUR_BOT_TOKEN")
    
    disko.eventBus.on<MessageCreateEvent> {
        if (message.content == "!ping") {
            message.reply {
                content = "Pong!"
            }
        }
    }
}
```

## Contributing
If you would like to contribute to Disko, please base your branch on `main` (or, a specific feature branch) and open a pull request into the same branch, providing as many relevant details as possible in the description. If you are unsure about something, feel free to ask in the pull request or in the [Discord server][discord].

## A Note on Sharding
Disko will automatically manage sharding for you, so you don't have to worry about it. Manually sharding is not supported as of yet, but will be in the future. If you would like to contribute towards how manual sharding will function, please connect with us in our [Discord server][discord] so we can discuss further.

## A Note on Self-Bots
Self-bots are not supported by Disko, and will not be supported in the future. Discord has expressed their prohibition of self-bots in their [Terms of Service](https://discord.com/terms), and we will not be supporting any actions that go against their terms. You will need to create a bot account from the [Discord Developer Portal](https://discord.com/developers/applications) to use Disko.

## Related Projects
- [Kord](https://github.com/kordlib/kord)
- [JDA](https://github.com/discord-jda/JDA)
- [Javacord](https://github.com/Javacord/Javacord)
- [Discord4J](https://github.com/Discord4J/Discord4J)

[discord]: https://discord.gg/rYHWPbh7CS
