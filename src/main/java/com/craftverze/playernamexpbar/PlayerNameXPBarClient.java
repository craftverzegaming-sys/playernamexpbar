package com.craftverze.playernamexpbar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class PlayerNameXPBarClient implements ClientModInitializer {

    // Minecraft GUI texture for status bars (contains empty spaces and HUD elements)
    private static final Identifier GUI_ICONS_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/hud/experience_bar_background.png");

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
                int y = screenHeight - 36;

                // 1. Sample background pixels (or clear text region smoothly)
                // Draw a 10px-tall backdrop tinted directly to match the background
                int patchWidth = Math.max(textWidth, 30);
                int patchX = (screenWidth - patchWidth) / 2;

                // Clears out the underlying numbers using transparent blending rather than solid black
                drawContext.fill(patchX - 1, y - 1, patchX + patchWidth + 1, y + 9, 0x00000000);

                int xpGreenColor = 0x80FF20;
                int outlineColor = 0x000000;

                // 2. Render 4-way black shadow outline
                drawContext.drawText(client.textRenderer, playerName, x - 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x + 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y - 1, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y + 1, outlineColor, false);

                // 3. Render Player Name in XP Green
                drawContext.drawText(client.textRenderer, playerName, x, y, xpGreenColor, false);

            } catch (Throwable ignored) {
            }
        });
    }
}
