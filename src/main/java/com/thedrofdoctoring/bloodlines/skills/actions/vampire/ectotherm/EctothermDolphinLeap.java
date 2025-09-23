package com.thedrofdoctoring.bloodlines.skills.actions.vampire.ectotherm;

import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.networking.packets.from_server.ClientboundLeapPacket;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class EctothermDolphinLeap extends EctothermBloodlineAction implements ILastingAction<IVampirePlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean activate(IVampirePlayer iVampirePlayer, ActivationContext activationContext) {
        if(!iVampirePlayer.asEntity().isInWater()) {
            iVampirePlayer.asEntity().displayClientMessage(Component.translatable("skill.bloodlines.ectotherm.not_in_water"), true);
            return false;
        }
        // Client activation for actions is called everytime an action is synced, which is not ideal for this action as movement can only be done on the client side.
        if(iVampirePlayer.asEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, ClientboundLeapPacket.getInstance());
        }
        return true;
    }

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.ectothermDolphinLeapCooldown.get() * 20;
    }

    @Override
    public int getDuration(IVampirePlayer iVampirePlayer) {
        return CommonConfig.ectothermDolphinLeapDuration.get() * 20;
    }

    @Override
    public void onActivatedClient(IVampirePlayer iVampirePlayer) {

    }


    @Override
    public void onDeactivated(IVampirePlayer iVampirePlayer) {

    }

    @Override
    public void onReActivated(IVampirePlayer iVampirePlayer) {

    }

    @Override
    public boolean onUpdate(IVampirePlayer iVampirePlayer) {
        iVampirePlayer.asEntity().fallDistance = 0;
        if (!iVampirePlayer.isRemote() && iVampirePlayer.asEntity().tickCount % 50 == 0) {
            this.applyEffect(iVampirePlayer);
        }

        return false;
    }
    protected void applyEffect(IVampirePlayer vampire) {
        this.addEffectInstance(vampire, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 52, 1));
    }
    public boolean showHudCooldown(Player player) {
        return true;
    }

    public boolean showHudDuration(Player player) {
        return true;
    }
}
