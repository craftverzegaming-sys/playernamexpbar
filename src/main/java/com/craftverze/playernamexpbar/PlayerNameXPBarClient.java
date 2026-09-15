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

            try {
                TextRenderer textRenderer = client.textRenderer;
                String playerName = client.player.getName().getString();
                int textWidth = textRenderer.getWidth(playerName);

                int screenWidth = drawContext.getScaledWindowWidth();
                int screenHeight = drawContext.getScaledWindowHeight();

                int x = (screenWidth - textWidth) / 2;
                int y = screenHeight - 36;

                // 1. Wipe out the vanilla XP number behind it
                // We draw a compact filled rect centered right over where vanilla draws the level
                int clearWidth = Math.max(textWidth, 20);
                int clearX1 = (screenWidth - clearWidth) / 2 - 2;
                int clearX2 = (screenWidth + clearWidth) / 2 + 2;
                
                // Draw a 0-alpha clear mask / black strip to scrub the number
                drawContext.fill(clearX1, y - 1, clearX2, y + 9, 0xFF000000);

                int xpGreenColor = 0x80FF20;
                int outlineColor = 0x000000;

                // 2. Render 4-way black outline
                drawContext.drawText(textRenderer, playerName, x - 1, y, outlineColor, false);
                drawContext.drawText(textRenderer, playerName, x + 1, y, outlineColor, false);
                drawContext.drawText(textRenderer, playerName, x, y - 1, outlineColor, false);
                drawContext.drawText(textRenderer, playerName, x, y + 1, outlineColor, false);

                // 3. Render Name in Green
                drawContext.drawText(textRenderer, playerName, x, y, xpGreenColor, false);

            } catch (Throwable ignored) {
            }
        });
    }
}
