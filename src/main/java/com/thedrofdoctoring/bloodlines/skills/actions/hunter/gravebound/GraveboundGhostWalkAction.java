package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.world.entity.player.Player;

public class GraveboundGhostWalkAction extends GraveboundSoulAction implements ILastingAction<IHunterPlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getConsumedSouls(IHunterPlayer player) {
        return HunterBloodlinesConfig.ghostWalkSoulCost.get();
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        if(!super.activate(iHunterPlayer, activationContext)) {
            return false;
        }
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().ghostWalk = true;
        consumeSouls(iHunterPlayer);
        return true;
    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.ghostWalkCooldown.get() * 20;
    }

    @Override
    public int getDuration(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.ghostWalkDuration.get() * 20;
    }

    @Override
    public void onActivatedClient(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().ghostWalk = true;

    }

    @Override
    public void onDeactivated(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().ghostWalk = false;

    }

    @Override
    public void onReActivated(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().ghostWalk = true;

    }

    @Override
    public boolean canBeUsedBy(IHunterPlayer player) {
        return BloodlineHelper.hasBloodline(BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), player.asEntity());
    }

    @Override
    public boolean onUpdate(IHunterPlayer iHunterPlayer) {
        return false;
    }

    @Override
    public boolean showHudDuration(Player player) {
        return true;
    }
}
