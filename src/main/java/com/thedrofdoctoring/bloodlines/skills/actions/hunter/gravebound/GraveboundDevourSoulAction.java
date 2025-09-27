package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class GraveboundDevourSoulAction extends DefaultGraveboundAction {


    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        Player player = iHunterPlayer.asEntity();
        if(activationContext.targetEntity().isPresent()) {
            Entity entity = activationContext.targetEntity().get();
            BloodlineManager manager = BloodlineManager.get(iHunterPlayer.asEntity());
            if(entity instanceof LivingEntity living && manager.getBloodline() instanceof BloodlineGravebound gravebound) {
                if(gravebound.canDevour(living, player)) {
                    gravebound.devour(living, player);
                }
                return true;
            }
        }
        return false;
    }



    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        int blRank = BloodlineManager.get(iHunterPlayer.asEntity()).getRank() - 1;
        return HunterBloodlinesConfig.devourSoulCooldownsSeconds.get().get(blRank) * 20;
    }

    public boolean showHudCooldown(Player player) {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
