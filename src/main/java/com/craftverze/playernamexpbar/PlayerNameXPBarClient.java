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
                int x = (drawContext.getScaledWindowWidth() - textWidth) / 2;
                
                // Centered between top of XP bar and bottom of health/armor icons
                int y = drawContext.getScaledWindowHeight() - 36;

                int xpGreenColor = 0x80FF20;
                int outlineColor = 0x000000;

                // 1. Draw 4-directional black outline (matching vanilla XP text outline)
                drawContext.drawText(client.textRenderer, playerName, x - 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x + 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y - 1, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y + 1, outlineColor, false);

                // 2. Draw main text in XP Green
                drawContext.drawText(client.textRenderer, playerName, x, y, xpGreenColor, false);

            } catch (Throwable ignored) {
                // Safeguard against frame drops
            }
        });
    }
}
