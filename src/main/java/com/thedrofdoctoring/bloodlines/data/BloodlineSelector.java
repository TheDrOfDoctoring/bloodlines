package com.thedrofdoctoring.bloodlines.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;

import java.util.List;

public record BloodlineSelector(boolean targetNoBloodline, boolean playerOnly, List<IBloodline> targetBloodlines) {

    public static final MapCodec<BloodlineSelector> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("target_without_bloodlines").forGetter(BloodlineSelector::targetNoBloodline),
            Codec.BOOL.fieldOf("player_only").forGetter(BloodlineSelector::playerOnly),
            IBloodline.CODEC.listOf().fieldOf("target_bloodlines").forGetter(BloodlineSelector::targetBloodlines)
    ).apply(instance, BloodlineSelector::new));

    public BloodlineSelector(boolean targetNoBloodline, List<IBloodline> targetBloodlines) {
        this(targetNoBloodline, false, targetBloodlines);
    }

}
