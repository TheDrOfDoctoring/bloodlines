package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import net.minecraft.world.effect.PoisonMobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PoisonMobEffect.class)
public class PoisonMobEffectMixin {

    @Inject(method = "applyEffectTick", at = @At("HEAD"), remap = false, cancellable = true)
    public void applyEffectTick(LivingEntity entity, int yeah, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof Player player) {
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
            if(atts.getGraveboundData().poisonImmunity && !atts.getGraveboundData().poisonHealing) {
                cir.setReturnValue(true);
                cir.cancel();
            }
            if(atts.getGraveboundData().poisonHealing) {
                player.heal(0.5f);
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }

}
