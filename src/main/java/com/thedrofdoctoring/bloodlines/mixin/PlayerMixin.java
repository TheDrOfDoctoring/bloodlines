package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineZealot;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {
    @ModifyReturnValue(method = "getBlockSpeedFactor", at = @At("RETURN"))
    private float zealotStoneSpeed(float originalSpeed) {
        Player player = (Player) (Object) this;
        if(Helper.isVampire(player) && bloodlines$onStone(player) && BloodlineManager.get(player).getBloodline() instanceof BloodlineZealot) {
            float speedMultiplier = ((IVampSpecialAttributes)VampirePlayer.get(player).getSpecialAttributes()).bloodlines$getStoneRunSpeed();
            return originalSpeed * speedMultiplier;
        }
        return originalSpeed;
    }
    @Unique
    private boolean bloodlines$onStone(Player player) {
        return player.level().getBlockState(player.getOnPos()).is(BloodlinesTagsProviders.BloodlinesBlockTagProvider.ZEALOT_STONE);
    }
}