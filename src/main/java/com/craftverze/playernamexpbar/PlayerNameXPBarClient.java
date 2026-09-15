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

            if (client.player != null && client.textRenderer != null && !client.options.hudHidden) {
                String playerName = client.player.getName().getString();

                int x = (drawContext.getScaledWindowWidth() - client.textRenderer.getWidth(playerName)) / 2;
                int y = drawContext.getScaledWindowHeight() - 36;

                drawContext.drawTextWithShadow(client.textRenderer, Text.literal(playerName), x, y, 0xFFFFFF);
            }
        });
    }
}
