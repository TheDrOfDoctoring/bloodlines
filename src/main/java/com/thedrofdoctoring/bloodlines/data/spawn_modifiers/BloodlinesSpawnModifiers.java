package com.thedrofdoctoring.bloodlines.data.spawn_modifiers;

import com.mojang.datafixers.util.Pair;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.data.BloodlinesData;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import de.teamlapen.vampirism.core.ModEntities;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class BloodlinesSpawnModifiers {

    public static final ResourceKey<BloodlineSpawnModifier> BLOODLINE_NOBLE_SPAWN_MODIFIER = ResourceKey.create(
            BloodlinesData.BLOODLINE_SPAWN_MODIFIERS,
            Bloodlines.rl("noble_spawn_modifier")
    );
    public static final ResourceKey<BloodlineSpawnModifier> BLOODLINE_ECTOTHERM_SPAWN_MODIFIER = ResourceKey.create(
            BloodlinesData.BLOODLINE_SPAWN_MODIFIERS,
            Bloodlines.rl("ectotherm_spawn_modifier")
    );
    public static final ResourceKey<BloodlineSpawnModifier> BLOODLINE_ZEALOT_SPAWN_MODIFIER = ResourceKey.create(
            BloodlinesData.BLOODLINE_SPAWN_MODIFIERS,
            Bloodlines.rl("zealot_spawn_modifier")
    );
    public static final ResourceKey<BloodlineSpawnModifier> BLOODLINE_BLOODKNIGHT_SPAWN_MODIFIERS = ResourceKey.create(
            BloodlinesData.BLOODLINE_SPAWN_MODIFIERS,
            Bloodlines.rl("bloodknight_spawn_modifier")
    );


    public static void createSpawnModifiers(BootstrapContext<BloodlineSpawnModifier> context) {
        HolderGetter<Biome> biomeLookup = context.lookup(Registries.BIOME);

        context.register(
                BLOODLINE_NOBLE_SPAWN_MODIFIER,
                new BloodlineSpawnModifier(
                        BloodlineRegistry.BLOODLINE_NOBLE.get(),
                        List.of(ModEntities.VAMPIRE.get(), ModEntities.ADVANCED_VAMPIRE.get()),
                        List.of(
                                Pair.of(biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD), 45),
                                Pair.of(biomeLookup.getOrThrow(BloodlinesTagsProviders.BloodlinesBiomeTagProvider.NOBLE_BIOMES), 100)
                        ),
                        Pair.of(60, -25)
                )

        );
        context.register(
                BLOODLINE_BLOODKNIGHT_SPAWN_MODIFIERS,
                new BloodlineSpawnModifier(
                        BloodlineRegistry.BLOODLINE_BLOODKNIGHT.get(),
                        List.of(ModEntities.VAMPIRE.get(), ModEntities.ADVANCED_VAMPIRE.get()),
                        List.of(
                                Pair.of(biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD), 15),
                                Pair.of(biomeLookup.getOrThrow(Tags.Biomes.IS_HOT_OVERWORLD), 20)
                        ),
                        Pair.of(60, -25)
                )

        );
        context.register(
                BLOODLINE_ECTOTHERM_SPAWN_MODIFIER,
                new BloodlineSpawnModifier(
                        BloodlineRegistry.BLOODLINE_ECTOTHERM.get(),
                        List.of(ModEntities.VAMPIRE.get(), ModEntities.ADVANCED_VAMPIRE.get()),
                        List.of(
                                Pair.of(biomeLookup.getOrThrow(Tags.Biomes.IS_HOT), -100),
                                Pair.of(biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD), 25),
                                Pair.of(biomeLookup.getOrThrow(BiomeTags.IS_OCEAN), 50),
                                Pair.of(biomeLookup.getOrThrow(Tags.Biomes.IS_COLD), 50)
                        ),
                        Pair.of(60, -50)
                )

        );
        // In Vampirism, vampires cannot typically spawn in "underground biomes", so the tags here are effectively pointless, only the below depth weight bonus matters.
        context.register(
                BLOODLINE_ZEALOT_SPAWN_MODIFIER,
                new BloodlineSpawnModifier(
                        BloodlineRegistry.BLOODLINE_ZEALOT.get(),
                        List.of(ModEntities.VAMPIRE.get(), ModEntities.ADVANCED_VAMPIRE.get()),
                        List.of(
                                Pair.of(biomeLookup.getOrThrow(Tags.Biomes.IS_UNDERGROUND), 100),
                                Pair.of(biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD), -50),
                                Pair.of(biomeLookup.getOrThrow(BiomeTags.HAS_ANCIENT_CITY), 100)
                        ),
                        Pair.of(40, 500)
                )

        );
    }
}
