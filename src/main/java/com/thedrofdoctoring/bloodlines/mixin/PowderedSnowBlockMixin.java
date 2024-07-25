package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderSnowBlock.class)
public class PowderedSnowBlockMixin {

    @ModifyReturnValue(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"))
    private static boolean canWalkOnPowderedSnow(boolean orig, Entity entity) {
        if(entity instanceof Player player && Helper.isVampire(player)) {
            return VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_SNOW_WALKER.get());
        }
        return orig;
    }
}
