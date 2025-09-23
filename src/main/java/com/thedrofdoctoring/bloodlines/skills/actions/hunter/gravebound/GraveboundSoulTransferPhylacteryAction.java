package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.blocks.entities.PhylacteryBlockEntity;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlineState;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class GraveboundSoulTransferPhylacteryAction extends DefaultGraveboundAction{

    private static final int SOUL_TRANSFER_AMOUNT = 5;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {

        Player player = iHunterPlayer.asEntity();
        if(!BloodlinesPlayerAttributes.get(player).getGraveboundData().hasPhylactery) {
            return false;
        }
        Optional<BloodlineState> stateOpt = BloodlineManager.get(player).getBloodlineState();
        if(stateOpt.isPresent() && stateOpt.get() instanceof BloodlineGravebound.State state) {
            Optional<PhylacteryBlockEntity> phylacteryOpt = state.tryGetPhylactery();
            if(phylacteryOpt.isPresent()) {
                PhylacteryBlockEntity phylactery = phylacteryOpt.get();
                BloodlineManager manager = BloodlineManager.get(player);
                if(phylactery.getStoredSouls() <= 0) {
                    player.displayClientMessage(Component.translatable("text.bloodlines.phylactery_not_enough_souls"), true);
                    return false;
                }

                int used = phylactery.addSouls(-SOUL_TRANSFER_AMOUNT);
                int usedAfter = state.addSouls(used);
                phylactery.addSouls(SOUL_TRANSFER_AMOUNT - usedAfter);
                state.updateCache(manager.getRank());
                phylactery.setChanged();
                manager.sync(false);
            }
        }

        return true;
    }

    @Override
    public boolean canBeUsedBy(IHunterPlayer player) {
        return BloodlinesPlayerAttributes.get(player.asEntity()).getGraveboundData().hasPhylactery;
    }

    public boolean showHudCooldown(Player player) {
        return true;
    }


    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.phylacterySoulTransferActionCooldown.get() * 20;
    }
}
