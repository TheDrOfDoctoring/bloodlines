package com.thedrofdoctoring.bloodlines.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SpecialIceBlock extends HalfTransparentBlock {
    public static final MapCodec<SpecialIceBlock> CODEC = simpleCodec(SpecialIceBlock::new);
    public static final IntegerProperty MELTAGE = IntegerProperty.create("meltage", 0, 2);
    public static final BooleanProperty WAS_WATER = BooleanProperty.create("was_water");


    @Override
    public @NotNull MapCodec<? extends SpecialIceBlock> codec() {
        return CODEC;
    }

    public SpecialIceBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    public static BlockState meltsInto() {
        return Blocks.WATER.defaultBlockState();
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(MELTAGE);
        pBuilder.add(WAS_WATER);
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
        if (pState.getValue(WAS_WATER)) {
            pLevel.setBlockAndUpdate(pPos, meltsInto());
        }
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(pState.getValue(MELTAGE) < 2) {
            int melt = pState.getValue(MELTAGE);
            pLevel.setBlock(pPos, pState.setValue(MELTAGE, melt + 1), 2);
        } else {
            this.melt(pState, pLevel, pPos);
        }
    }

    protected void melt(BlockState pState, Level pLevel, BlockPos pPos) {
        if (pState.getValue(WAS_WATER)) {
            pLevel.setBlockAndUpdate(pPos, meltsInto());
            pLevel.neighborChanged(pPos, meltsInto().getBlock(), pPos);
        } else {
            pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
            pLevel.neighborChanged(pPos, Blocks.AIR, pPos);
        }
    }
}
