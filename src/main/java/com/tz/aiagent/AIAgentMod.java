package com.tz.aiagent;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIAgentMod implements ModInitializer {
	public static final String MOD_ID = "ai-agent";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("AI Agent Mod Initialized!");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(Commands.literal("agent")
					.then(Commands.argument("prompt", StringArgumentType.greedyString())
							.executes(context -> {
								// Extracts the text string after /agent
								String userPrompt = StringArgumentType.getString(context, "prompt");

								// Sends feedback to the player
								context.getSource().sendSuccess(() -> Component.literal("AI Agent Processing: " + userPrompt), false);

								GeminiService.getCommand(userPrompt)
										.thenAccept(command -> {
											context.getSource().getServer().execute(() -> {
												String cleanCommand = command.startsWith("/") ? command.substring(1) : command;

												context.getSource().getServer().getCommands().performPrefixedCommand(
														context.getSource(), cleanCommand);
											});
										});

								return 1;
							})
					)
			);
		});
	}
}