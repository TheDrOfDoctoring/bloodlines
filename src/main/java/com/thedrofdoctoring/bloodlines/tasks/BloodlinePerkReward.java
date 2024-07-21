package com.thedrofdoctoring.bloodlines.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.ITaskRewardInstance;
import de.teamlapen.vampirism.api.entity.player.task.TaskReward;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public record BloodlinePerkReward(int perkPoints) implements TaskReward, ITaskRewardInstance {

    public static final MapCodec<BloodlinePerkReward> CODEC = RecordCodecBuilder.mapCodec(inst -> {
        return inst.group(Codec.INT.fieldOf("perkPointAmount").forGetter(i -> i.perkPoints)).apply(inst, BloodlinePerkReward::new);
    });


    @Override
    public void applyReward(IFactionPlayer<?> p) {
        BloodlineManager.getOpt(p.asEntity()).ifPresent(bl -> {
                bl.getSkillHandler().setSkillPoints(bl.getSkillHandler().getSkillPoints() + perkPoints);
                bl.sync(false);
        });
    }

    @Override
    public @NotNull ITaskRewardInstance createInstance(IFactionPlayer<?> player) {
        return this;
    }

    @Override
    public MapCodec<BloodlinePerkReward> codec() {
        return BloodlineTasks.BLOODLINE_PERK_REWARD.get();
    }

    @Override
    public Component description() {
        return Component.translatable("task_reward.bloodlines.bloodline_reward").withStyle(ChatFormatting.DARK_RED);
    }
}