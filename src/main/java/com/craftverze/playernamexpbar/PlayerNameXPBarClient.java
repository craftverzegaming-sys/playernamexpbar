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

            if (client == null || client.player == null || client.world == null) {
                return;
            }

            if (client.textRenderer == null || client.options.hudHidden) {
                return;
            }

            try {
                String playerName = client.player.getName().getString();

                int x = (drawContext.getScaledWindowWidth() - client.textRenderer.getWidth(playerName)) / 2;
                int y = drawContext.getScaledWindowHeight() - 36;

                // 0xFFFFFFFF (white with 100% alpha), shadow = true
                drawContext.drawText(client.textRenderer, Text.literal(playerName), x, y, 0xFFFFFFFF, true);
            } catch (Exception ignored) {
                // Catches frame execution drops safely
            }
        });
    }
}
