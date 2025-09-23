package com.thedrofdoctoring.bloodlines.world.structures;

import com.mojang.serialization.MapCodec;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ZealotShrineStructure extends Structure {

    public static final MapCodec<ZealotShrineStructure> CODEC = simpleCodec(ZealotShrineStructure::new);



    protected ZealotShrineStructure(StructureSettings pSettings) {
        super(pSettings);
    }
    @Override
    public @NotNull Optional<Structure.GenerationStub> findGenerationPoint(Structure.@NotNull GenerationContext context) {

        ChunkPos chunkPos = context.chunkPos();
        int midX, midZ;
        midX = chunkPos.getMiddleBlockX();
        midZ = chunkPos.getMiddleBlockZ();
        NoiseColumn blockColumn = context.chunkGenerator().getBaseColumn(midX, midZ, context.heightAccessor(), context.randomState());
        int y;
        for(y = -60; y < 10; y++) {
            if(blockColumn.getBlock(y + 1).is(BlockTags.AIR)) {
                break;
            }
        }

        if(y >= 10) return Optional.empty();
        BlockPos blockPos = new BlockPos(midX, y, midZ);

        return Optional.of(
                new GenerationStub(
                        blockPos, (b) ->
                        b.addPiece(
                                new ZealotShrinePiece(2, context.structureTemplateManager(), "zealot_shrine", makeSettings(), blockPos)
                        )
                )
        );
    }
    private static @NotNull StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setRotation(Rotation.CLOCKWISE_180).setMirror(Mirror.NONE);
    }
    @Override
    public @NotNull StructureType<?> type() {
        return BloodlineStructures.ZEALOT_SHRINE_STRUCTURE.get();
    }

    public static class ZealotShrinePiece extends TemplateStructurePiece {

        public ZealotShrinePiece(int pGenDepth, StructureTemplateManager pStructureTemplateManager, String pTemplateName, StructurePlaceSettings pPlaceSettings, BlockPos pTemplatePosition) {
            super(BloodlineStructures.ZEALOT_SHRINE_PIECE.get(), pGenDepth, pStructureTemplateManager, Bloodlines.rl("zealot_shrine"), pTemplateName, pPlaceSettings, pTemplatePosition);
        }

        public ZealotShrinePiece(StructureTemplateManager pStructureTemplateManager, CompoundTag pTag) {
            super(BloodlineStructures.ZEALOT_SHRINE_PIECE.get(), pTag, pStructureTemplateManager, (p -> (makeSettings())));
        }



        @Override
        protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext pContext, @NotNull CompoundTag pTag) {

        }

        @Override
        protected void handleDataMarker(@NotNull String pName, @NotNull BlockPos pPos, @NotNull ServerLevelAccessor pLevel, @NotNull RandomSource pRandom, @NotNull BoundingBox pBox) {

        }


    }
}
