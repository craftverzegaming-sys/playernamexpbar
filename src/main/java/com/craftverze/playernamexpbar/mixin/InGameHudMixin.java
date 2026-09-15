package com.craftverze.playernamexpbar.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    // Redirects all drawText calls inside InGameHud matching XP number drawing
    @Redirect(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"
        )
    )
    private int cancelXpText(DrawContext context, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow) {
        // Check if the text being rendered is numeric (the XP level number)
        if (text != null && text.matches("\\d+")) {
            return 0; // Cancel rendering standard XP numbers
        }
        return context.drawText(textRenderer, text, x, y, color, shadow);
    }
}
