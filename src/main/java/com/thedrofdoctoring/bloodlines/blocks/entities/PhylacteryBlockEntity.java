package com.thedrofdoctoring.bloodlines.blocks.entities;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlockEntities;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PhylacteryBlockEntity extends BlockEntity {
    public PhylacteryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BloodlinesBlockEntities.PHYLACTERY.get(), pPos, pBlockState);
    }

    private UUID ownerUUID;
    private Player owner;
    private int storedSouls;
    private int maxStoredSouls;


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
    public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookupProvider) {
        CompoundTag tag = pkt.getTag();
        if (this.hasLevel()) {
            this.loadCustomOnly(tag, lookupProvider);
        }
    }
    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider lookupProvider) {
        super.saveAdditional(nbt, lookupProvider);
        if (ownerUUID != null) {
            nbt.putUUID("player_uuid", this.ownerUUID);
            nbt.putInt("stored_souls", this.storedSouls);
            nbt.putInt("max_stored_souls", this.maxStoredSouls);
        }
    }
    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider lookupProvider) {
        super.loadAdditional(nbt, lookupProvider);
        if (owner == null && nbt.hasUUID("player_uuid")) {
            this.ownerUUID = nbt.getUUID("player_uuid");
            this.storedSouls = nbt.getInt("stored_souls");
            this.maxStoredSouls = nbt.getInt("max_stored_souls");

            if(this.level != null) {
                this.owner = this.level.getPlayerByUUID(ownerUUID);
            }
        }

    }
    public Player getOwner() {
        if(this.level != null && this.owner == null && this.ownerUUID != null) {
            this.owner = this.level.getPlayerByUUID(ownerUUID);
        }
        return this.owner;
    }

    public int getStoredSouls() {
        return storedSouls;
    }
    public void determineMaxSouls(int totalSoulsDevoured) {
        List<? extends Integer> tiers = HunterBloodlinesConfig.phylacteryMaxStorageTierRequirements.get();
        int i = 0;
        boolean foundTier = false;

        if(totalSoulsDevoured >= tiers.getLast()) {
            foundTier = true;
            i = tiers.size() - 1;
        }

        while(i < tiers.size() && !foundTier) {
            int tierRequirement = tiers.get(i);
            if(totalSoulsDevoured < tierRequirement) {
                foundTier = true;
                i-=1;
            }
            if(!foundTier) {
                i++;
            }
        }
        if(i >= tiers.size()) {
            maxStoredSouls = 5;
            Bloodlines.LOGGER.warn("Unable to obtain Phylactery tier");
        }
        this.maxStoredSouls = HunterBloodlinesConfig.phylacteryMaxStorageTiers.get().get(i);
    }

    public int addSouls(int additional) {
        if(additional > 0) {
            int used = additional;
            if(additional + storedSouls > maxStoredSouls) {
                used = maxStoredSouls - storedSouls;
            }
            this.storedSouls = Math.min(this.maxStoredSouls, storedSouls + additional);
            return used;
        }
        else if (additional < 0) {
            int used = -additional;
            if(additional + storedSouls < 0) {
                used = storedSouls;
            }
            this.storedSouls = Math.max(this.storedSouls - used, 0);
            return used;
        }
        return 0;
    }

    public void setOwner(@Nullable Player player) {
        this.owner = player;
        if(player == null) {
            this.ownerUUID = null;
            this.storedSouls = 0;
            this.maxStoredSouls = 0;
        } else {
            this.ownerUUID =  player.getUUID();
            BloodlineGravebound.State state= BloodlineGravebound.getGraveboundState(player);
            if(state != null) {
                determineMaxSouls(state.getTotalSoulsDevoured());
            }
        }
        this.setChanged();
    }


    public int getMaxStoredSouls() {
        return maxStoredSouls;
    }

    public boolean hasOwner() {
        return this.ownerUUID != null;
    }
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }
    public static Optional<PhylacteryBlockEntity> searchForNearbyPhylactery(Level level, BlockPos origin, boolean allowOwned, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        for(int i = minX; i <= maxX; i++ ) {
            for(int j = minY; j <= maxY; j++ ) {
                for(int k = minZ; k <= maxZ; k++ ) {
                    BlockPos pos = new BlockPos(origin.getX() + i, origin.getY() + j, origin.getZ() + k);
                    BlockState state = level.getBlockState(pos);
                    if(state.is(BloodlinesBlocks.PHYLACTERY)) {
                        BlockEntity be = level.getBlockEntity(pos);
                        if(be instanceof PhylacteryBlockEntity phylactery) {
                            if(allowOwned) {
                                return Optional.of(phylactery);
                            } else if(phylactery.getOwner() == null) {
                                return Optional.of(phylactery);
                            }
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}
