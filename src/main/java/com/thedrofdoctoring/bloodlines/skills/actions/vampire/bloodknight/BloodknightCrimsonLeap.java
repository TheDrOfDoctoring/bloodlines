package com.thedrofdoctoring.bloodlines.skills.actions.vampire.bloodknight;

import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.networking.packets.from_server.ClientboundLeapPacket;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import de.teamlapen.vampirism.particle.GenericParticleOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class BloodknightCrimsonLeap extends BloodknightBloodlineAction implements ILastingAction<IVampirePlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean activate(IVampirePlayer iVampirePlayer, ActivationContext activationContext) {
        int blood = iVampirePlayer.getBloodStats().getBloodLevel();
        int drain = CommonConfig.bloodknightCrimsonLeapBloodUse.get();
        if(blood - drain <= 0) {
            iVampirePlayer.asEntity().displayClientMessage(Component.translatable("skill.bloodlines.bloodknight_not_enough_blood").withStyle(ChatFormatting.DARK_RED), true);
            return false;
        }
        iVampirePlayer.useBlood(drain, true);
        // Client activation for actions is called everytime an action is synced, which is not ideal for this action as movement can only be done on the client side.
        if(iVampirePlayer.asEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, ClientboundLeapPacket.getInstance());
        }
        return true;
    }

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.bloodknightCrimsonLeapCooldown.get();
    }

    @Override
    public int getDuration(IVampirePlayer iVampirePlayer) {
        return CommonConfig.bloodknightCrimsonLeapDuration.get();
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
        if(!iVampirePlayer.asEntity().getCommandSenderWorld().isClientSide) {
            Player player = iVampirePlayer.asEntity();
            ModParticles.spawnParticlesServer(player.getCommandSenderWorld(), new GenericParticleOptions(ResourceLocation.fromNamespaceAndPath("minecraft", "generic_4"), 5, 0x8B0000, 0.1F) , player.getX(), player.getY(), player.getZ(), 3, 0,0, 0, 0);
        }
        return false;
    }

    @Override
    public boolean canBeUsedBy(IVampirePlayer player) {
        return !player.getActionHandler().isActionActive(VampireActions.BAT.get());

    }
    public boolean showHudCooldown(Player player) {
        return true;
    }

    public boolean showHudDuration(Player player) {
        return true;
    }
}

