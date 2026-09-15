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
                // Temporarily hide vanilla XP level number (Minecraft hides level numbers when level is 0)
                int realXpLevel = client.player.experienceLevel;
                client.player.experienceLevel = 0;

                String playerName = client.player.getName().getString();
                int textWidth = client.textRenderer.getWidth(playerName);

                int screenWidth = drawContext.getScaledWindowWidth();
                int screenHeight = drawContext.getScaledWindowHeight();

                int x = (screenWidth - textWidth) / 2;
                int y = screenHeight - 36; // Positioned right above the XP bar

                int xpGreenColor = 0x80FF20;
                int outlineColor = 0x000000;

                // 1. Render 4-way black outline
                drawContext.drawText(client.textRenderer, playerName, x - 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x + 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y - 1, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y + 1, outlineColor, false);

                // 2. Render Player Name in XP Green
                drawContext.drawText(client.textRenderer, playerName, x, y, xpGreenColor, false);

                // Restore real XP level for game mechanics
                client.player.experienceLevel = realXpLevel;

            } catch (Throwable ignored) {
            }
        });
    }
}
