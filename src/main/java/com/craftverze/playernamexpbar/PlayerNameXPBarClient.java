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
                int y = screenHeight - 36; // Level text coordinate above XP bar

                // 1. Draw a small dark box over the old level numbers so they are covered completely
                int coverWidth = Math.max(textWidth, 24);
                int coverX1 = (screenWidth - coverWidth) / 2 - 2;
                int coverX2 = (screenWidth + coverWidth) / 2 + 2;
                drawContext.fill(coverX1, y - 2, coverX2, y + 10, 0xFF000000);

                int xpGreenColor = 0x80FF20;
                int outlineColor = 0x000000;

                // 2. Render 4-way black outline
                drawContext.drawText(client.textRenderer, playerName, x - 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x + 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y - 1, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y + 1, outlineColor, false);

                // 3. Render name in XP Green
                drawContext.drawText(client.textRenderer, playerName, x, y, xpGreenColor, false);

            } catch (Throwable ignored) {
            }
        });
    }
}
