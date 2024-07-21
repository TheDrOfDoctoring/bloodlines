package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.blocks.SpecialIceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BloodlinesBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Bloodlines.MODID);
    public static final DeferredBlock<SpecialIceBlock> ICE_BLOCK = BLOCKS.register("special_ice_block", () -> new SpecialIceBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.ICE)
            .friction(0.98F)
            .randomTicks()
            .strength(0.5F)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .isRedstoneConductor((p, t, x) -> false)
    ));
}
