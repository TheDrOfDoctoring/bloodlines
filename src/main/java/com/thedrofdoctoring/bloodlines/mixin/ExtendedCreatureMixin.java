package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.core.BloodlineAttachments;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import de.teamlapen.vampirism.entity.vampire.VampireBaseEntity;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ExtendedCreature.class)
public class ExtendedCreatureMixin {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @ModifyReturnValue(method = "getSafe", at = @At("RETURN"), remap = false)
    private static @NotNull Optional<ExtendedCreature> modifyGetVampire(Optional<ExtendedCreature> original, Entity mob) {
        if(mob instanceof VampireBaseEntity vp) {
            return Optional.of(vp.getData(BloodlineAttachments.VAMP_EXTENDED_CREATURE.get()));
        }
        return original;
    }

}
