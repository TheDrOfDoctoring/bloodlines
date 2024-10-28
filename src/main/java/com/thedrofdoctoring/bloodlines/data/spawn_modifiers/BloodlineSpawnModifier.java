package com.thedrofdoctoring.bloodlines.data.spawn_modifiers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public record BloodlineSpawnModifier(IBloodline bloodline, List<EntityType<?>> targetEntities, List<Pair<HolderSet<Biome>, Integer>> weightPairs) {

    public static final MapCodec<BloodlineSpawnModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                IBloodline.CODEC.fieldOf("bloodline").forGetter(BloodlineSpawnModifier::bloodline),
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().fieldOf("entity").forGetter(BloodlineSpawnModifier::targetEntities),
                Codec.pair(
                        Biome.LIST_CODEC.fieldOf("biome").codec(),
                        Codec.INT.fieldOf("weight").codec()
                ).listOf().fieldOf("biome_weight").forGetter(BloodlineSpawnModifier::weightPairs)
    ).apply(instance, BloodlineSpawnModifier::new);
    });

}
