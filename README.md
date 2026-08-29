# Minecraft AI Agent Mod (26.2)

This is a Fabric mod for Minecraft 26.2 that adds an `/agent` command. The command translates plain English prompts into executable in-game commands using the Gemini API.

## Project Overview
The mod solves the issue of complex command syntax. Instead of memorizing NBT tags, exact item IDs, and relative coordinates, players can simply type their intent into chat. 

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

---

## Setup & Installation (For Judges & Players)

### Prerequisites
1. A free API key from [Google AI Studio](https://aistudio.google.com/).
2. A Minecraft 26.2 instance with the Fabric Loader installed.

### Steps

**Step 1: Install the Mod**
- Download the `.jar` file from the Releases page.
- Navigate to your Minecraft `mods` folder (usually `C:\Users\[YourName]\AppData\Roaming\.minecraft\mods`).
- Place the `.jar` file inside this folder.

**Step 2: Set the Environment Variable**

*You must do this so the mod can find your API key. You only need to do this once.*

**Method A: PowerShell (Fastest for tech-savvy users)**
1. Press the **Windows Key** on your keyboard.
2. Type `PowerShell`.
3. Right-click on **Windows PowerShell** and select **Run as administrator**.
4. Copy and paste this exact command into the window (replace `YOUR_API_KEY_HERE` with your actual key):

setx GEMINI_API_KEY "YOUR_API_KEY_HERE"

5. Press **Enter**. You should see a message saying "SUCCESS: Specified value was saved."
6. **CRITICAL:** Close the PowerShell window. **Restart your computer** (or at minimum, completely close and reopen the Minecraft Launcher). Usually, a full computer restart is the most reliable way to make the variable stick.

**Method B: Windows Settings (Safest for beginners - RECOMMENDED)**
1. Press the **Windows Key** and type `Edit the system environment variables`.
2. Click on the result to open the **System Properties** window.
3. Click the **Environment Variables...** button on the bottom right.
4. In the top section (under "User variables for [YourName]"), click **New...**.
5. **Variable name:** `GEMINI_API_KEY`
6. **Variable value:** Paste your API key here (e.g., `AQ.Ab8RN6...`).
7. Click **OK**, then **OK**, then **OK** to close all windows.
8. **CRITICAL:** Restart your computer (or at minimum, completely close and reopen the Minecraft Launcher). Usually, a full computer restart is the most reliable way to make the variable stick.

**Step 3: Restart the Game**
- **CRITICAL:** Completely close the Minecraft Launcher.
- If you used PowerShell or the GUI method, it is highly recommended to restart your computer entirely before launching the game again. This ensures the new environment variable is fully loaded into the system.
- Open the Minecraft Launcher and launch the game.

**Step 4: Play**
- Once the game is open and you are in a world, type `/agent <prompt>`.
- Example: `/agent make it night`

---

## Security Architecture: Why I Used Environment Variables

For this project, the Gemini API key is **not hardcoded** in the source code or the JAR file. Instead, it is read from a system environment variable (`GEMINI_API_KEY`) at runtime.

**Why this is safer than hosting a proxy server:**
- **Prevents key theft:** A `.jar` file is a compressed archive that can be easily decompiled. If the key were hardcoded, anyone could extract it from the mod, use up your free API quota, and potentially rack up charges on your account. 
- **User Control:** By using `System.getenv("GEMINI_API_KEY")`, the source code provides a secure template. Users maintain control over their own credentials, ensuring they are never shared with other players or strangers.
- **Cost and Maintenance:** Hosting a dedicated server (to act as a proxy for the API) introduces significant ongoing costs, requires 24/7 uptime, and adds complexity for the user. For a standalone Minecraft mod, it is cleaner and more transparent to let the user provide their own key.
- **Transparency:** Judges and users can inspect the source code and see exactly that the key is used only for the Gemini API request and is not logged or sent anywhere else.
- **Best practice:** This approach follows industry-standard security practices for client-side applications, ensuring the mod is safe for public distribution.

---

## Usage
In-game, type:
`/agent <prompt>`

**Examples:**
- `/agent make it night`
- `/agent summon a creeper`
- `/agent give me a diamond sword`
- `/agent time set day`
- `/protect me`

## Troubleshooting
- **"Error: Check console"**: This means the environment variable was not set correctly, or the game was not restarted after setting it. **Restarting your PC is the #1 fix.** 
- **"API key not valid"**: Ensure you copied the key exactly and did not include extra spaces. 

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.
