package com.thedrofdoctoring.bloodlines.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineSkillHandler;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.TaskUnlocker;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
@SuppressWarnings("unused")
public record MaxPerkUnlocker(int minPerkPoints, int maxPerkPoints) implements TaskUnlocker {


    public static final MapCodec<MaxPerkUnlocker> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("maxPerkPoints").forGetter(i -> i.minPerkPoints),
            Codec.INT.fieldOf("minPerkPoints").forGetter(i -> i.maxPerkPoints)
    ).apply(inst, MaxPerkUnlocker::new));

    @Override
    public Component getDescription() {
        return Component.translatable("text.bloodlines.max_perk_points", minPerkPoints, maxPerkPoints);
    }

    @Override
    public boolean isUnlocked(IFactionPlayer<?> iFactionPlayer) {
        Player entity = iFactionPlayer.asEntity();
        BloodlineSkillHandler skillHandler = BloodlineManager.get(entity).getSkillHandler();
        if(skillHandler != null) {
            int taskSkillPoints = skillHandler.getTaskSkillPoints();
            return taskSkillPoints >= minPerkPoints && taskSkillPoints < maxPerkPoints;
        }
        return true;
    }

    @Override
    public MapCodec<? extends TaskUnlocker> codec() {
        return CODEC;
    }
}
