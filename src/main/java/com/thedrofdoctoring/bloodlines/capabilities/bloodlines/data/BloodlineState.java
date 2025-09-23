package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BloodlineState  {

    protected Player player;

    protected BloodlineState(Player player) {
        this.player = player;
    }

    public abstract void deserializeUpdateNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt);

    public abstract CompoundTag serializeUpdateNBT(HolderLookup.@NotNull Provider provider, CompoundTag origNBT);

    public abstract void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt);

    public abstract CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag origNBT);

    public abstract void clear(Level level);

    public abstract void updateCache(int rank);
}
