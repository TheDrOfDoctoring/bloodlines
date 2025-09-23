package com.thedrofdoctoring.bloodlines.mixin.client;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.client.core.render.TransparencyBufferSourceWrapper;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @ModifyVariable(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), argsOnly = true)
    private MultiBufferSource replaceBufferSource(MultiBufferSource source, AbstractClientPlayer player) {
        if(BloodlinesPlayerAttributes.get(player).getGraveboundData().ghostWalk) {
            return new TransparencyBufferSourceWrapper(source, 0.2f);
        }
        return source;
    }
}
