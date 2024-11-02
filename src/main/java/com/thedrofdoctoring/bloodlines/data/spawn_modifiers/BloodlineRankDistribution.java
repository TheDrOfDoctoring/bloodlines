package com.thedrofdoctoring.bloodlines.data.spawn_modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record BloodlineRankDistribution(List<Float> rankDistributions) {
    public static final MapCodec<BloodlineRankDistribution> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                Codec.FLOAT.listOf(5, 5).comapFlatMap(f -> {
                        double sum = Math.floor(f.stream().mapToDouble(p -> p).sum() * 100) / 100;
                        if(sum != 1.0) return DataResult.error(() -> "Sum of values in Bloodline Rank Distribution must equal 1. Given sum is " + sum);
                        return DataResult.success(f);
                }, p -> p).fieldOf("rank_distributions").forGetter(BloodlineRankDistribution::rankDistributions)
        ).apply(instance, BloodlineRankDistribution::new);
    });
}
