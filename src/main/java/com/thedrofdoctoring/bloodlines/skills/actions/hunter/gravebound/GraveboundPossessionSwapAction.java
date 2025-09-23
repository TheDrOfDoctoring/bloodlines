package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GraveboundPossessionSwapAction extends GraveboundSoulAction {
    @Override
    public int getConsumedSouls(IHunterPlayer player) {
        return HunterBloodlinesConfig.possessionSwapSoulCost.get();
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        if(!super.activate(iHunterPlayer, activationContext)) {
            return false;
        }
        LivingEntity target = BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().possessedEntity;
        if(target == null) return false;
        Vec3 targetPos = target.position();
        Vec3 playerPos = iHunterPlayer.asEntity().position();
        playerPos = new Vec3(playerPos.x, playerPos.y, playerPos.z);
        iHunterPlayer.asEntity().teleportTo(targetPos.x, targetPos.y, targetPos.z);
        target.absMoveTo(playerPos.x, playerPos.y, playerPos.z);

        consumeSouls(iHunterPlayer);
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean showInSelectAction(Player player) {
        return BloodlinesPlayerAttributes.get(player).getGraveboundData().possessedEntity != null;
    }

    @Override
    public boolean canBeUsedBy(IHunterPlayer player) {
        return BloodlinesPlayerAttributes.get(player.asEntity()).getGraveboundData().possessedEntity != null;
    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.possessionSwapCooldown.get() * 20;
    }
}
