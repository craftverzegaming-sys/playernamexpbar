package com.craftverze.playernamexpbar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

public class PlayerNameXPBarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client == null || client.player == null || client.world == null) return;
            if (client.textRenderer == null || client.options.hudHidden) return;

            TextRenderer textRenderer = client.textRenderer;
            String playerName = client.player.getName().getString();
            int textWidth = textRenderer.getWidth(playerName);

            int screenWidth = drawContext.getScaledWindowWidth();
            int screenHeight = drawContext.getScaledWindowHeight();

            int x = (screenWidth - textWidth) / 2;
            int y = screenHeight - 36; // Position directly above XP bar

            // Push matrix to layer text ON TOP of all vanilla HUD elements
            drawContext.getMatrices().push();
            drawContext.getMatrices().translate(0, 0, 500); // 500 Z-offset brings it above the HUD

            int xpGreenColor = 0x80FF20;
            int outlineColor = 0x000000;

            // 1. Black Outline
            drawContext.drawText(textRenderer, playerName, x - 1, y, outlineColor, false);
            drawContext.drawText(textRenderer, playerName, x + 1, y, outlineColor, false);
            drawContext.drawText(textRenderer, playerName, x, y - 1, outlineColor, false);
            drawContext.drawText(textRenderer, playerName, x, y + 1, outlineColor, false);

            // 2. Green Player Name
            drawContext.drawText(textRenderer, playerName, x, y, xpGreenColor, false);

            drawContext.getMatrices().pop();
        });
    }
}
