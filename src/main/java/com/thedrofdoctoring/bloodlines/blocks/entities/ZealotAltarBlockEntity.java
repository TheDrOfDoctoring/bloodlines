package com.thedrofdoctoring.bloodlines.blocks.entities;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlockEntities;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.core.ModSounds;
import de.teamlapen.vampirism.particle.FlyingBloodParticleOptions;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Modified from Vampirism's AltarInfusionBlockEntity
 */

public class ZealotAltarBlockEntity extends BlockEntity {

    public ZealotAltarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BloodlinesBlockEntities.ZEALOT_ALTAR.get(), pPos, pBlockState);
    }

    private static final int TOTAL_DURATION = 700;

    private int currentTick;
    private UUID currentPlayerUUID;
    private Player currentPlayer;

    @NotNull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider lookupProvider) {
        return this.saveWithoutMetadata(lookupProvider);
    }
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider lookupProvider) {
        super.saveAdditional(nbt, lookupProvider);
        nbt.putInt("tick", currentTick);
        if (currentPlayer != null) {
            nbt.putUUID("player_uuid", currentPlayer.getUUID());
        }
    }
    @Override
    public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookupProvider) {
        CompoundTag tag = pkt.getTag();
        if (this.hasLevel()) {
            this.loadCustomOnly(tag, lookupProvider);
        }
    }

    public void startRitual(Player player) {
        if (level == null) return;
        this.currentPlayer = player;
        currentTick = TOTAL_DURATION;
        this.setChanged();

        if(!level.isClientSide) {
            BlockState state = this.level.getBlockState(getBlockPos());
            level.sendBlockUpdated(getBlockPos(), state, state, 3);
        }
    }
    private void progressRitual() {

        if (currentPlayer == null || !currentPlayer.isAlive()) {
            currentTick = 1;
        } else {
            if (currentPlayer.getDeltaMovement().y >= 0) {
                currentPlayer.setDeltaMovement(0D, 0D, 0D);
            } else {
                currentPlayer.setDeltaMovement(0D, currentPlayer.getDeltaMovement().y, 0D);
                currentPlayer.setDeltaMovement(currentPlayer.getDeltaMovement().multiply(1D, 0.5D, 1D));
            }
        }
        if(this.level == null) return;


        if (currentTick > 600) {
            if (currentTick % 15 == 0) {
                BlockPos pos = getBlockPos();
                ModParticles.spawnParticlesClient(level, new FlyingBloodParticleOptions(60, false, pos.getX(), pos.getY() + 2, pos.getZ() + 0.5), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0, 5, 0.1, RandomSource.create());
            }
        }
        if (currentTick <= 650 && currentTick % 30 == 0) {

            int xOffset = level.getRandom().nextInt(-6, 10);
            int zOffset = level.getRandom().nextInt(-6, 10);
            level.playSound(null, currentPlayer.getX() + xOffset, currentPlayer.getY(), currentPlayer.getZ() + zOffset, SoundEvents.ENDERMITE_STEP, SoundSource.BLOCKS);

        }
        if (currentTick <= 650 && currentTick % 20 == 0 && currentTick >= 400) {
            int xOffset = level.getRandom().nextInt(-10, 15);
            int zOffset = level.getRandom().nextInt(-10, 15);
            level.playSound(null, currentPlayer.getX() + xOffset, currentPlayer.getY(), currentPlayer.getZ() + zOffset, SoundEvents.BASALT_BREAK, SoundSource.BLOCKS);
        }
        if (currentTick <= 400 && currentTick % 10 == 0) {
            int xOffset = level.getRandom().nextInt(-8, 8);
            int zOffset = level.getRandom().nextInt(-8, 8);
            level.playSound(null, currentPlayer.getX() + xOffset, currentPlayer.getY(), currentPlayer.getZ() + zOffset, SoundEvents.BASALT_BREAK, SoundSource.BLOCKS);
        }

        if (currentTick <= 650) {
            currentPlayer.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 30, 2));
        }
        if (currentTick <= 400 && currentTick % 100 == 0) {
            level.playSound(null, currentPlayer.getX(), currentPlayer.getY(), currentPlayer.getZ(), SoundEvents.AMBIENT_CAVE.value(), SoundSource.BLOCKS);
        }


        if (currentTick <= 600) {
            currentPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 2));

        }

        if (currentTick == 50) {
            if(!level.isClientSide) {
                BloodlineHelper.joinBloodlineGeneric(currentPlayer, BloodlineRegistry.BLOODLINE_ZEALOT.get(), Component.translatable("text.bloodlines.zealot_join").withStyle(ChatFormatting.DARK_RED));
            }

            currentPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 2));
            currentPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 2));

        }
        if (currentTick < 50) {
            currentPlayer = null;
            currentPlayerUUID = null;
            currentTick = 0;
            this.setChanged();
        }

    }

    public static void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ZealotAltarBlockEntity altar) {
        if (altar.currentPlayerUUID != null) {
            if (!altar.setupRitualState(altar.currentPlayerUUID)) return;
            altar.currentPlayerUUID = null;
            altar.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);

        }
        if (altar.currentTick == TOTAL_DURATION && !level.isClientSide) {
            altar.setChanged();
        }
        if (altar.currentTick > 0) {
            altar.currentTick--;
            altar.progressRitual();
        }
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider lookupProvider) {
        super.loadAdditional(nbt, lookupProvider);
        int tick = nbt.getInt("tick");
        if (tick > 0 && currentPlayer == null && nbt.hasUUID("player_uuid")) {
            UUID uuid = nbt.getUUID("player_uuid");
            if (!setupRitualState(uuid)) {
                this.currentPlayerUUID = uuid;
            }
            this.currentTick = tick;
        }

    }

    private boolean setupRitualState(UUID currentPlayerUUID) {
        if(this.level == null) return false;
        if(this.level.players().isEmpty()) return false;

        this.currentPlayer = this.level.getPlayerByUUID(currentPlayerUUID);

        if(this.currentPlayer == null) {
            currentTick = 0;
        }

        return true;
    }

    public @NotNull Result canActivate(@NotNull Player player) {
        if (!CommonConfig.zealotUniqueUnlock.get()) {
            return Result.DISABLED;
        }
        if (currentTick > 0) {
            return Result.RUNNING;
        }
        if(!Helper.isVampire(player)) {
            return Result.NOT_VAMPIRE;
        }
        if(BloodlineHelper.getBloodlineRank(player) > 0) {
            return Result.HAS_BLOODLINE;
        }


        this.currentPlayer = null;
        return Result.OK;

    }

    public enum Result {
        OK, HAS_BLOODLINE, NOT_VAMPIRE, RUNNING, DISABLED
    }

}
