package com.thedrofdoctoring.bloodlines.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CapeLayer.class)
public class CapeLayerMixin {

    @ModifyExpressionValue(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entitySolid(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;")
    )
    private RenderType returnTranslucentType(RenderType original, @Local(argsOnly = true) AbstractClientPlayer player) {
        if(BloodlinesPlayerAttributes.get(player).getGraveboundData().ghostWalk) {
            ResourceLocation capeTexture = player.getSkin().capeTexture();
            if(capeTexture == null) return original;
            return RenderType.entityTranslucent(capeTexture);
        }
        return original;
    }

}
