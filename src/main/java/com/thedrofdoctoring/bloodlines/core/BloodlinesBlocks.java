package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.blocks.SpecialIceBlock;
import com.thedrofdoctoring.bloodlines.blocks.ZealotAltarBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

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
    public static final DeferredBlock<ZealotAltarBlock> ZEALOT_ALTAR = registerWithItem("zealot_altar", () -> new ZealotAltarBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(7)
            .noOcclusion()
    ));
    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Supplier<T> supplier, Item.@NotNull Properties properties) {
        DeferredBlock<T> block = BLOCKS.register(name, supplier);
        BloodlinesItems.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Supplier<T> supplier) {
        return registerWithItem(name, supplier, new Item.Properties());
    }
}
