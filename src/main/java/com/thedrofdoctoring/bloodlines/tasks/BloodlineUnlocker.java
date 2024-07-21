package com.thedrofdoctoring.bloodlines.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.TaskUnlocker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BloodlineUnlocker implements TaskUnlocker {
    public static final MapCodec<BloodlineUnlocker> CODEC = RecordCodecBuilder.mapCodec(inst -> {
        return inst.group(
                Codec.INT.fieldOf("bloodLineLevel").forGetter(i -> i.bloodlineLevel),
                Codec.STRING.fieldOf("bloodlineId").forGetter(i -> i.id),
                Codec.BOOL.fieldOf("matchExactly").forGetter(i -> i.matchExactly)
        ).apply(inst, BloodlineUnlocker::new);
    });

    private final int bloodlineLevel;
    private final String id;
    private final boolean matchExactly;
    public BloodlineUnlocker(int rank, ResourceLocation bloodlineId, boolean matchExactly) {
        this.bloodlineLevel = rank;
        this.id = bloodlineId.toString();
        this.matchExactly = matchExactly;
    }
    public BloodlineUnlocker(int rank, String bloodlineId, boolean matchExactly) {
        this.bloodlineLevel = rank;
        this.id = bloodlineId;
        this.matchExactly = matchExactly;
    }

    @Override
    public Component getDescription() {
        return Component.translatable("text.bloodlines.bloodline_unlocker", id, bloodlineLevel);
    }

    @Override
    public boolean isUnlocked(IFactionPlayer<?> iFactionPlayer) {
        BloodlineManager bl = BloodlineManager.get(iFactionPlayer.asEntity());
        boolean correctRank = this.matchExactly ? bl.getRank() == bloodlineLevel : bl.getRank() >= bloodlineLevel;
        return (bl.getBloodlineId().toString().equals(id) && correctRank);
    }

    @Override
    public MapCodec<? extends TaskUnlocker> codec() {
        return BloodlineTasks.BLOODLINE_UNLOCKER.get();
    }
}
