package com.thedrofdoctoring.bloodlines.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import de.teamlapen.vampirism.client.gui.overlay.SunOverlay;
import de.teamlapen.vampirism.client.gui.overlay.TextureOverlay;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SunOverlay.class)
public abstract class SunOverlayMixin extends TextureOverlay {


    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lde/teamlapen/vampirism/client/gui/overlay/SunOverlay;renderTextureOverlay(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;F)V"
            ),
            remap = false
    )
    @SuppressWarnings("ConstantConditions")
    private void lessHarshSun(SunOverlay instance, GuiGraphics guiGraphics, ResourceLocation resourceLocation, float v, Operation<Void> original) {
        IVampSpecialAttributes specialAttributes = (IVampSpecialAttributes) VampirismPlayerAttributes.get(mc.player).getVampSpecial();
        if (specialAttributes.bloodlines$getDayWalker()) {
            this.renderTextureOverlay(guiGraphics, SunOverlay.SUN_TEXTURE, 0.25f);
        } else {
            original.call(instance, guiGraphics, resourceLocation, v);
        }
    }

}
