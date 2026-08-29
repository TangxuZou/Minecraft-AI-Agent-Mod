package com.tz.aiagent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class GeminiService {

    private static final String API_KEY = System.getenv("GEMINI_API_KEY");

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=" + API_KEY;

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String SYSTEM_PROMPT = "You are a Minecraft Command Generator. Output ONLY a valid raw Minecraft command without slashes, explanation, or markdown formatting.";

    public static CompletableFuture<String> getCommand(String userPrompt) {
        // Build System Instruction
        JsonObject systemPart = new JsonObject();
        systemPart.addProperty("text", SYSTEM_PROMPT);
        JsonArray systemParts = new JsonArray();
        systemParts.add(systemPart);
        JsonObject systemInstruction = new JsonObject();
        systemInstruction.add("parts", systemParts);

        // Build User Prompt
        JsonObject userPart = new JsonObject();
        userPart.addProperty("text", userPrompt);
        JsonArray userParts = new JsonArray();
        userParts.add(userPart);
        JsonObject userContent = new JsonObject();
        userContent.add("parts", userParts);

        // Build Final Payload
        JsonArray contents = new JsonArray();
        contents.add(userContent);

        JsonObject payload = new JsonObject();
        payload.add("system_instruction", systemInstruction);
        payload.add("contents", contents);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        System.err.println("Gemini API Error " + response.statusCode() + ": " + response.body());
                        return "say Error: Check console.";
                    }
                    try {
                        return JsonParser.parseString(response.body()).getAsJsonObject()
                                .getAsJsonArray("candidates")
                                .get(0).getAsJsonObject()
                                .getAsJsonObject("content")
                                .getAsJsonArray("parts")
                                .get(0).getAsJsonObject()
                                .get("text").getAsString();
                    } catch (Exception e) {
                        return "say The AI returned an empty or invalid response.";
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return "say Failed to connect to the AI service.";
                });
    }
}