package com.thedrofdoctoring.bloodlines.world.structures;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BloodlineStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Bloodlines.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, Bloodlines.MODID);

    public static final DeferredHolder<StructureType<?>, StructureType<ZealotShrineStructure>> ZEALOT_SHRINE_STRUCTURE = STRUCTURE_TYPES.register("zealot_shrine", () -> () -> ZealotShrineStructure.CODEC);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> ZEALOT_SHRINE_PIECE = STRUCTURE_PIECES.register("zealot_shrine", () -> (StructurePieceType.StructureTemplateType) ZealotShrineStructure.ZealotShrinePiece::new);


    public static final ResourceKey<Structure> ZEALOT_SHRINE = ResourceKey.create(Registries.STRUCTURE, Bloodlines.rl("zealot_shrine"));
    public static final ResourceKey<StructureSet> ZEALOT_SHRINE_SET = createStructureSetKey("zealot_shrine");

    private static ResourceKey<StructureSet> createStructureSetKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, Bloodlines.rl(name));
    }

    public static void createStructureSets(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structureLookup = context.lookup(Registries.STRUCTURE);
        HolderGetter<StructureSet> structureSetLookup = context.lookup(Registries.STRUCTURE_SET);
        context.register(ZEALOT_SHRINE_SET, new StructureSet(structureLookup.getOrThrow(ZEALOT_SHRINE), new RandomSpreadStructurePlacement(16, 8, RandomSpreadType.LINEAR, 1937195837)));
    }

    public static void createStructures(BootstrapContext<Structure> context) {
        HolderGetter<Biome> lookup = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> lookup1 = context.lookup(Registries.TEMPLATE_POOL);
        context.register(ZEALOT_SHRINE, new ZealotShrineStructure(
                new Structure.StructureSettings.Builder(
                        lookup.getOrThrow(BiomeTags.HAS_ANCIENT_CITY))
                        .generationStep(GenerationStep.Decoration.UNDERGROUND_DECORATION)
                        .terrainAdapation(TerrainAdjustment.BEARD_BOX).build()
        ));


    }
}
