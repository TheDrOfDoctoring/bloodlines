package com.thedrofdoctoring.bloodlines.skills;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public record BloodlineSubPredicate(IBloodline bloodline) implements EntitySubPredicate {


    @Override
    public @NotNull MapCodec<? extends EntitySubPredicate> codec() {
        return CODEC;
    }
    public static final MapCodec<BloodlineSubPredicate> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    IBloodline.CODEC.fieldOf("bloodline").forGetter(p -> p.bloodline)
            ).apply(inst, BloodlineSubPredicate::new)
    );

    @Override
    public boolean matches(@NotNull Entity pEntity, @NotNull ServerLevel pLevel, @Nullable Vec3 p_218830_) {
        if (pEntity instanceof Player player) {
            return BloodlineManager.getOpt(player).map(handler -> bloodline == null || handler.getBloodline() == bloodline).orElse(false);
        }
        return false;
    }

}
