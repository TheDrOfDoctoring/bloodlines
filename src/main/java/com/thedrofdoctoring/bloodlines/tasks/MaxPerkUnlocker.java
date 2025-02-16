package com.thedrofdoctoring.bloodlines.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineSkillHandler;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineManager;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.TaskUnlocker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record MaxPerkUnlocker(int maxPerkPoints, ResourceLocation taskName) implements TaskUnlocker {


    public static final MapCodec<MaxPerkUnlocker> CODEC = RecordCodecBuilder.mapCodec(inst -> {
        return inst.group(
                Codec.INT.fieldOf("maxPerkPoints").forGetter(i -> i.maxPerkPoints),
                ResourceLocation.CODEC.fieldOf("taskName").forGetter(t -> t.taskName)
        ).apply(inst, MaxPerkUnlocker::new);
    });

    @Override
    public Component getDescription() {
        return Component.translatable("text.bloodlines.max_perk_points", maxPerkPoints);
    }

    @Override
    public boolean isUnlocked(IFactionPlayer<?> iFactionPlayer) {
        Player entity = iFactionPlayer.asEntity();
        BloodlineSkillHandler skillHandler = BloodlineManager.get(entity).getSkillHandler();
        if(skillHandler != null) {
            int gainedPoints = skillHandler.getPointsFromSource(taskName);
            return gainedPoints <= maxPerkPoints;
        }
        return true;
    }

    @Override
    public MapCodec<? extends TaskUnlocker> codec() {
        return CODEC;
    }
}
