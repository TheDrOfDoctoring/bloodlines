package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.BatVampireAction;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BatVampireAction.class)
public class BatVampireActionMixin {

    @Inject(method = "setModifier", at = @At(value = "INVOKE", target = "Lde/teamlapen/vampirism/entity/player/vampire/actions/BatVampireAction;setFlightSpeed(Lnet/minecraft/world/entity/player/Player;F)V", shift = At.Shift.AFTER, ordinal = 0), remap = false)
    private void setNobleBatSpeedMultiplier(Player player, boolean enabled, CallbackInfo ci) {
        if(VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_BAT_FLIGHT_SPEED.get())) {
            int rank = BloodlineHelper.getBloodlineRank(player);
            player.getAbilities().setFlyingSpeed(player.getAbilities().getFlyingSpeed() * CommonConfig.nobleBatSpeedMultiplier.get().get(rank - 1).floatValue());
        }
    }
}