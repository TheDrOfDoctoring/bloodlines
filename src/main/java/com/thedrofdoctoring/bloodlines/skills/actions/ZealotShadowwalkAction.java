package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.capabilities.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.config.VampirismConfig;
import de.teamlapen.vampirism.core.ModEntities;
import de.teamlapen.vampirism.core.ModRefinements;
import de.teamlapen.vampirism.core.ModSounds;
import de.teamlapen.vampirism.entity.AreaParticleCloudEntity;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class ZealotShadowwalkAction extends DefaultVampireAction {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean activate(@NotNull IVampirePlayer vampire, ActivationContext context) {
        Player player = vampire.asEntity();

        if(!BloodlineHelper.lightMatches(CommonConfig.zealotShadowwalkLightLevel.get(), player.getOnPos().above(), player.getCommandSenderWorld(), true)) {
            player.displayClientMessage(Component.translatable("text.bloodlines.current_light").withStyle(ChatFormatting.DARK_PURPLE), true);
            return false;
        }
        int dist = VampirismConfig.BALANCE.vaTeleportMaxDistance.get();
        if (vampire.getSkillHandler().isRefinementEquipped(ModRefinements.TELEPORT_DISTANCE.get())) {
            dist *= VampirismConfig.BALANCE.vrTeleportDistanceMod.get();
        }
        HitResult target = UtilLib.getPlayerLookingSpot(player, dist);
        double ox = player.getX();
        double oy = player.getY();
        double oz = player.getZ();
        if (target.getType() == HitResult.Type.MISS) {
            player.playSound(SoundEvents.NOTE_BLOCK_BASS.value(), 1, 1);
            return false;
        }
        BlockPos pos = null;
        if (target.getType() == HitResult.Type.BLOCK) {
            if (player.getCommandSenderWorld().getBlockState(((BlockHitResult) target).getBlockPos()).blocksMotion()) {
                pos = ((BlockHitResult) target).getBlockPos().above();
            }
        } else {//TODO better solution / remove
            if (player.getCommandSenderWorld().getBlockState(((EntityHitResult) target).getEntity().blockPosition()).blocksMotion()) {
                pos = ((EntityHitResult) target).getEntity().blockPosition();
            }
        }

        if (pos != null) {
            if(!BloodlineHelper.lightMatches(CommonConfig.zealotShadowwalkLightLevel.get(), pos.above(), player.getCommandSenderWorld(), true)) {
                player.displayClientMessage(Component.translatable("text.bloodlines.target_light").withStyle(ChatFormatting.DARK_PURPLE), true);
                return false;
            }
            player.setPos(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
            if (player.getCommandSenderWorld().containsAnyLiquid(player.getBoundingBox()) || !player.getCommandSenderWorld().isUnobstructed(player)) { //isEntityColliding
                pos = null;
            }
        }


        if (pos == null) {
            player.setPos(ox, oy, oz);
            player.playSound(SoundEvents.NOTE_BLOCK_BASEDRUM.value(), 1, 1);
            return false;
        }
        if (player instanceof ServerPlayer playerMp) {
            playerMp.disconnect();
            playerMp.teleportTo(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
        }
        AreaParticleCloudEntity particleCloud = new AreaParticleCloudEntity(ModEntities.PARTICLE_CLOUD.get(), player.getCommandSenderWorld());
        particleCloud.setPos(ox, oy, oz);
        particleCloud.setRadius(0.7F);
        particleCloud.setHeight(player.getBbHeight());
        particleCloud.setDuration(5);
        particleCloud.setSpawnRate(15);
        player.getCommandSenderWorld().addFreshEntity(particleCloud);
        player.getCommandSenderWorld().playSound(null, ox,oy,oz, ModSounds.TELEPORT_AWAY.get(), SoundSource.PLAYERS, 1f, 1f);
        player.getCommandSenderWorld().playSound(null, player.getX(), player.getY(), player.getZ(),ModSounds.TELEPORT_HERE.get(), SoundSource.PLAYERS, 1f, 1f);
        return true;
    }

    @Override
    public boolean canBeUsedBy(IVampirePlayer player) {
        return !player.getActionHandler().isActionActive(VampireActions.BAT.get());
    }

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        int rank = BloodlineManager.get(iVampirePlayer.asEntity()).getRank() - 1;
        return CommonConfig.zealotShadowwalkCooldowns.get().get(rank).intValue() * 20;
    }
}
