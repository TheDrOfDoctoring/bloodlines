package com.thedrofdoctoring.bloodlines.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.BloodlinesClient;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.entity.BloodlineMobManager;
import de.teamlapen.vampirism.client.renderer.entity.BasicVampireRenderer;
import de.teamlapen.vampirism.entity.vampire.BasicVampireEntity;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(BasicVampireRenderer.class)
public class BasicVampireRendererMixin {

    @ModifyReturnValue(method = "getTextureLocation(Lde/teamlapen/vampirism/entity/vampire/BasicVampireEntity;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"))
    private ResourceLocation getBloodlineTexture(ResourceLocation original, BasicVampireEntity entity) {
        return BloodlinesClient.getInstance().getRenderManager().getBloodlineTexture(original, entity);
    }

}
