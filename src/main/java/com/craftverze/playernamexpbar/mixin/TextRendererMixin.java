package com.craftverze.playernamexpbar.mixin;

import com.craftverze.playernamexpbar.PlayerNameXPBarClient;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextRenderer.class)
public class TextRendererMixin {

    @Inject(method = "draw(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;IIZ)I", at = @At("HEAD"), cancellable = true)
    private void hideVanillaXpNumber(String text, float x, float y, int color, boolean shadow, org.joml.Matrix4f matrix, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, net.minecraft.client.font.TextRenderer.TextLayerType layerType, int backgroundColor, int light, boolean rightToLeft, CallbackInfoReturnable<Integer> cir) {
        if (!PlayerNameXPBarClient.renderingXPName && text != null && text.matches("\\d+")) {
            cir.setReturnValue(0);
        }
    }
}
