package com.craftverze.playernamexpbar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class PlayerNameXPBarClient implements ClientModInitializer {

    public static boolean renderingXPName = false;

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
                int y = screenHeight - 36; // Directly over the XP bar

                int xpGreenColor = 0x80FF20;
                int outlineColor = 0x000000;

                renderingXPName = true;

                // 4-way outline
                drawContext.drawText(client.textRenderer, playerName, x - 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x + 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y - 1, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y + 1, outlineColor, false);

                // Green text
                drawContext.drawText(client.textRenderer, playerName, x, y, xpGreenColor, false);

                renderingXPName = false;

            } catch (Throwable ignored) {
                renderingXPName = false;
            }
        });
    }
}
