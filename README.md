# Minecraft AI Agent Mod (26.2)

This is a Fabric mod for Minecraft 26.2 that adds an `/agent` command. The command translates plain English prompts into executable in-game commands using the Gemini API.

## Project Overview
The mod solves the issue of complex command syntax. Instead of memorizing NBT tags, exact IDs, and relative coordinates, players can simply type their intent into chat.

## Functional Details
- **Async HTTP Requests:** Uses `CompletableFuture` to handle all API calls on background threads, preventing the game's main thread from freezing while waiting for a response.
- **Command Parsing:** Uses a specific system prompt to force the AI to return only the raw command string. This prevents conversational text ("Sure! Here is your command:...") from breaking execution.
- **Player Context:** Executes commands using the player's existing `ServerCommandSource`. This ensures that relative coordinates (`~ ~ ~`) and player-specific permissions are handled correctly.
- **Chat Integration:** Registered via Fabric's standard Command API, utilizing the modern `Commands.literal` and `sendSuccess` methods for Minecraft 26.2.

## Tech Stack
- **Language:** Java (JDK 25)
- **Platform:** Minecraft 26.2 (Fabric)
- **AI Model:** Gemini 3.6 Flash
- **Libraries:** Gson, Java HttpClient, Fabric API

## Setup and Installation
1. Obtain an API key from Google AI Studio.
2. Set the key as an environment variable named `GEMINI_API_KEY`.
3. Build the project using `./gradlew build`.
4. Place the generated `.jar` file from `build/libs/` into your Minecraft `mods` folder.

## Usage
In-game, type:
`/agent <prompt>`

Examples:
- `/agent make it night`
- `/agent summon a creeper`
- `/agent give me a diamond sword with sharpness 5`

## Security
The API key is read entirely from the environment variable `GEMINI_API_KEY`. It is not hardcoded and is excluded from the repository.
