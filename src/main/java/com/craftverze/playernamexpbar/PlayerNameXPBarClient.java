package com.craftverze.playernamexpbar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.lang.reflect.Method;

public class PlayerNameXPBarClient implements ClientModInitializer {

    private Method drawTextMethod = null;
    private boolean reflectionAttempted = false;

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

                // Dynamically locate the drawText method at runtime regardless of mapping variations
                if (!reflectionAttempted) {
                    reflectionAttempted = true;
                    for (Method m : drawContext.getClass().getMethods()) {
                        Class<?>[] params = m.getParameterTypes();
                        if (params.length == 5 && 
                            params[0].isAssignableFrom(client.textRenderer.getClass()) &&
                            params[1].isAssignableFrom(Text.class) &&
                            params[2] == int.class &&
                            params[3] == int.class &&
                            params[4] == int.class) {
                            drawTextMethod = m;
                            break;
                        }
                    }
                }

                if (drawTextMethod != null) {
                    drawTextMethod.invoke(drawContext, client.textRenderer, Text.literal(playerName), x, y, 0xFFFFFFFF);
                } else {
                    // Direct fallback call
                    drawContext.drawText(client.textRenderer, Text.literal(playerName), x, y, 0xFFFFFFFF, true);
                }
            } catch (Throwable ignored) {
                // Catches all runtime invocation errors to guarantee no game crashes
            }
        });
    }
}
