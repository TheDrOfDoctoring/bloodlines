package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesDamageTypes;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class GraveboundSoulClaimingAction extends DefaultGraveboundAction implements ILastingAction<IHunterPlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().soulClaimingActive = true;
        return true;
    }



    @Override
    public void onActivatedClient(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().soulClaimingActive = true;
    }

    @Override
    public void onDeactivated(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().soulClaimingActive = false;

    }

    @Override
    public void onReActivated(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().soulClaimingActive = true;
    }
    @Override
    public int getDuration(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.soulClaimingDuration.get() * 20;
    }


    @Override
    public boolean onUpdate(IHunterPlayer iHunterPlayer) {
        return false;
    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.soulClaimingCooldown.get() * 20;
    }

    public static void handleSoulClaiming(LivingDeathEvent event) {

        Entity sourceDamageEntity = event.getSource().getEntity();
        if(sourceDamageEntity instanceof Player player && !event.getSource().is(BloodlinesDamageTypes.DEVOUR_SOUL)) {
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
            IBloodline bloodline = atts.bloodline;
            boolean soulClaimingActive = atts.getGraveboundData().soulClaimingActive || atts.getGraveboundData().passiveSoulClaiming;
            if(bloodline instanceof BloodlineGravebound gravebound && soulClaimingActive && gravebound.canDevour(event.getEntity(), player, true, true)) {
                gravebound.devour(event.getEntity(), player, false);
            }
        }
    }


    @Override
    public boolean showInSelectAction(Player player) {
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);

        return atts.bloodline == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get() && !atts.getGraveboundData().passiveSoulClaiming;
    }

    public boolean showHudCooldown(Player player) {
        return true;
    }
}
