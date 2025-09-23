package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public abstract class GraveboundSoulAction extends DefaultGraveboundAction {

    public abstract int getConsumedSouls(IHunterPlayer player);

    public void consumeSouls(IHunterPlayer player) {
        if(player.asEntity() instanceof ServerPlayer serverPlayer) {
            if(serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE) return;
        }
        BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(player.asEntity());
        if(state == null) {
            Bloodlines.LOGGER.warn("Unable to retrieve Gravebound state when consuming souls for player {}", player.asEntity().getDisplayName());
            return;
        }
        state.addSouls(-getConsumedSouls(player));
        BloodlineManager.get(player.asEntity()).sync(false);
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(iHunterPlayer.asEntity());
        if(state == null) return false;
        int consumed = getConsumedSouls(iHunterPlayer);
        if(state.getSouls() - consumed <= 0) {
            iHunterPlayer.asEntity().displayClientMessage(Component.translatable("skill.bloodlines.gravebound_not_enough_souls").withStyle(ChatFormatting.BLUE), true);
            return false;
        }
        return true;
    }
}
