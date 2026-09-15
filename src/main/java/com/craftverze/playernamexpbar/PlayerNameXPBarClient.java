package com.craftverze.playernamexpbar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class PlayerNameXPBarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client == null || client.player == null || client.world == null) return;
            if (client.textRenderer == null || client.options.hudHidden) return;

            try {
                // 1. Temporarily erase XP level text so vanilla doesn't render numbers
                int realExperienceLevel = client.player.experienceLevel;
                client.player.experienceLevel = 0; 

                String playerName = client.player.getName().getString();
                int textWidth = client.textRenderer.getWidth(playerName);
                
                // Centered horizontally, positioned right in the gap above the XP bar
                int x = (drawContext.getScaledWindowWidth() - textWidth) / 2;
                int y = drawContext.getScaledWindowHeight() - 36; 

                int xpGreenColor = 0x80FF20;
                int outlineColor = 0x000000;

                // 2. Draw 4-directional black outline (matching vanilla XP text)
                drawContext.drawText(client.textRenderer, playerName, x - 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x + 1, y, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y - 1, outlineColor, false);
                drawContext.drawText(client.textRenderer, playerName, x, y + 1, outlineColor, false);

                // 3. Draw main player name in XP Green
                drawContext.drawText(client.textRenderer, playerName, x, y, xpGreenColor, false);

                // Restore true player experience level state for gameplay math
                client.player.experienceLevel = realExperienceLevel;

            } catch (Throwable ignored) {
                // Prevent HUD rendering exceptions
            }
        });
    }
}
