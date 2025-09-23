package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class GraveboundForceEndMistFormAction extends DefaultGraveboundAction{
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        Player player = iHunterPlayer.asEntity();
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
        if(!atts.getGraveboundData().mistForm) {
            return false;
        }

        DamageSource killingDamageSource = atts.getGraveboundData().lastDamageSource;
        atts.getGraveboundData().mistForm = false;
        if(killingDamageSource != null) {
            player.hurt(killingDamageSource, 1000f);
        } else {
            player.kill();
        }
        return true;
    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return 20;
    }

    @Override
    public boolean canBeUsedBy(IHunterPlayer player) {
        return BloodlinesPlayerAttributes.get(player.asEntity()).getGraveboundData().mistForm;
    }

    @Override
    public boolean showInSelectAction(Player player) {
        return BloodlinesPlayerAttributes.get(player).getGraveboundData().mistForm;
    }

    @Override
    public boolean showHudCooldown(Player player) {
        return BloodlinesPlayerAttributes.get(player).getGraveboundData().mistForm;
    }
}
