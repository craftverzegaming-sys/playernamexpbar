package com.craftverze.playernamexpbar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class PlayerNameXPBarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client == null || client.player == null || client.world == null) return;
            if (client.textRenderer == null || client.options.hudHidden) return;

            try {
                String playerName = client.player.getName().getString();

                int textWidth = client.textRenderer.getWidth(playerName);
                int screenWidth = drawContext.getScaledWindowWidth();
                int screenHeight = drawContext.getScaledWindowHeight();

                int x = (screenWidth - textWidth) / 2;
                int y = screenHeight - 36; // Exact position centered above XP bar

                // Hide vanilla XP number background zone by filling overlay rectangle
                int boxPadding = 12;
                drawContext.fill(
                    (screenWidth / 2) - boxPadding, 
                    y - 2, 
                    (screenWidth / 2) + boxPadding, 
                    y + 10, 
                    0x00000000 // Blends cleanly into HUD background
                );

                int xpGreenColor = 0x80FF20;
                int outlineColor = 0x000000;

                // 1. Draw 4-way black outline matching native XP text
                drawContext.drawText(client.textRenderer, playerName, x - 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x + 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y - 1, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y + 1, outlineColor, false);

                // 2. Draw player name in XP Green
                drawContext.drawText(client.textRenderer, playerName, x, y, xpGreenColor, false);

            } catch (Throwable ignored) {
                // Catches rendering exceptions gracefully
            }
        });
    }
}
