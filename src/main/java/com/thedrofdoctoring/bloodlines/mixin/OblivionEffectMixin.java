package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import de.teamlapen.vampirism.effects.OblivionEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OblivionEffect.class)
public class OblivionEffectMixin {

    @Inject(method = "applyEffectTick", at = @At("RETURN"), remap = false)
    private void applyEffect(LivingEntity entityLivingBaseIn, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValue() && entityLivingBaseIn instanceof Player player) {
            BloodlineManager manager = BloodlineManager.get(player);
            if(manager.getBloodline() != null) {
                manager.getBloodline().onBloodlineChange(player, manager.getRank());
            }
        }

    }
}
