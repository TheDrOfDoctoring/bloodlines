package com.thedrofdoctoring.bloodlines.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.BloodlinesClient;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.entity.BloodlineMobManager;
import de.teamlapen.vampirism.client.renderer.entity.AdvancedVampireRenderer;
import de.teamlapen.vampirism.entity.vampire.AdvancedVampireEntity;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(AdvancedVampireRenderer.class)
public class AdvancedVampireRendererMixin {
    @ModifyReturnValue(method = "getTextureLocation(Lde/teamlapen/vampirism/entity/vampire/AdvancedVampireEntity;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"))
    private ResourceLocation getBloodlineTexture(ResourceLocation original, AdvancedVampireEntity entity) {
        return BloodlinesClient.getInstance().getRenderManager().getBloodlineTexture(original, entity);
    }
}
