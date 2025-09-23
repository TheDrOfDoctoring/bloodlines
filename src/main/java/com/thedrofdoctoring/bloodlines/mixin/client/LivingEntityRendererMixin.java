package com.thedrofdoctoring.bloodlines.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> extends EntityRenderer<T> {


    protected LivingEntityRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }


    @ModifyReturnValue(
            method = "getRenderType",
            at = @At("RETURN")
    )
    private RenderType setGhostWalkRenderType(RenderType original, T livingEntity) {
        ResourceLocation resourcelocation = this.getTextureLocation(livingEntity);
        if(livingEntity instanceof Player player && BloodlinesPlayerAttributes.get(player).getGraveboundData().ghostWalk) {
            return RenderType.entityTranslucent(resourcelocation);
        }
        return original;

    }





}
