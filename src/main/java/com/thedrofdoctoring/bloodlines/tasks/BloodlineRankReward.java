package com.thedrofdoctoring.bloodlines.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.ITaskRewardInstance;
import de.teamlapen.vampirism.api.entity.player.task.TaskReward;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.core.ModSounds;
import de.teamlapen.vampirism.particle.GenericParticleOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public record BloodlineRankReward(int targetRank) implements TaskReward, ITaskRewardInstance {

    public static final MapCodec<BloodlineRankReward> CODEC = RecordCodecBuilder.mapCodec(inst -> {
        return inst.group(
                Codec.INT.fieldOf("targetRank").forGetter(BloodlineRankReward::targetRank)
        ).apply(inst, BloodlineRankReward::new);
    });

    @Override
    public void applyReward(IFactionPlayer<?> p) {
        BloodlineManager.getOpt(p.asEntity()).ifPresent(bl -> {
            Player player = p.asEntity();
            if(bl.getRank() == targetRank - 1) {
                int oldRank = bl.getRank();
                bl.setRank(targetRank);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ENTITY_VAMPIRE_SCREAM.get(), SoundSource.PLAYERS, 1, 1);
                ModParticles.spawnParticlesServer(player.level(), new GenericParticleOptions(ResourceLocation.fromNamespaceAndPath("minecraft", "spell_1"), 50, 0x8B0000, 0.2F), player.getX(), player.getY(), player.getZ(), 100, 1, 1, 1, 0);
                bl.onBloodlineChange(bl.getBloodline(), oldRank);
            }
        });
    }

    @Override
    public @NotNull ITaskRewardInstance createInstance(IFactionPlayer<?> player) {
        return this;
    }

    @Override
    public MapCodec<BloodlineRankReward> codec() {
        return BloodlineTasks.BLOODLINE_RANK_REWARD.get();
    }

    @Override
    public Component description() {
        return null;
    }

}
