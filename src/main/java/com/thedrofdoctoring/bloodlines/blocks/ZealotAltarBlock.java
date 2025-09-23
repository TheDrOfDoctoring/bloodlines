package com.thedrofdoctoring.bloodlines.blocks;

import com.mojang.serialization.MapCodec;
import com.thedrofdoctoring.bloodlines.blocks.entities.ZealotAltarBlockEntity;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlockEntities;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import de.teamlapen.vampirism.blocks.AltarInfusionBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class  ZealotAltarBlock extends BaseEntityBlock {

    public static final MapCodec<AltarInfusionBlock> CODEC = simpleCodec(AltarInfusionBlock::new);

    public ZealotAltarBlock(Properties pProperties) {
        super(pProperties);
    }
    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.25, 0.75, 0.75, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.75, 0, 1, 0.875, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.875, 0, 0.125, 1.25, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0.875, 0, 1, 1.25, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0.875, 0.875, 1, 1.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.875, 0.875, 0.125, 1.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.75, 0.25, 0.75, 1, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 1.25, 0.375, 0.625, 1.5, 0.625), BooleanOp.OR);

        return shape;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        ZealotAltarBlockEntity altarBE = (ZealotAltarBlockEntity) pLevel.getBlockEntity(pPos);
        if (pLevel.isClientSide || altarBE == null) return ItemInteractionResult.SUCCESS;


        if(pStack.is(BloodlinesItems.ZEALOT_RITUAL_CATALYST.get())) {
            ZealotAltarBlockEntity.Result result = altarBE.canActivate(pPlayer);

            switch (result) {
                case DISABLED -> {
                    pPlayer.displayClientMessage(Component.translatable("text.bloodlines.zealot_altar.disabled").withStyle(ChatFormatting.DARK_RED), true);
                    return ItemInteractionResult.SUCCESS;
                }
                case RUNNING -> {
                    pPlayer.displayClientMessage(Component.translatable("text.bloodlines.zealot_altar.already_running").withStyle(ChatFormatting.DARK_RED), true);
                    return ItemInteractionResult.SUCCESS;
                }
                case HAS_BLOODLINE -> {
                    pPlayer.displayClientMessage(Component.translatable("text.bloodlines.zealot_altar.has_bloodline").withStyle(ChatFormatting.DARK_RED), true);
                    return ItemInteractionResult.SUCCESS;
                }
                case NOT_VAMPIRE -> {
                    pPlayer.displayClientMessage(Component.translatable("text.bloodlines.zealot_altar.not_vampire").withStyle(ChatFormatting.DARK_RED), true);
                    return ItemInteractionResult.SUCCESS;
                }
                case OK -> {
                    pStack.shrink(1);
                    altarBE.startRitual(pPlayer);
                    return ItemInteractionResult.SUCCESS;
                }
            }


        } else {
            pPlayer.displayClientMessage(Component.translatable("text.bloodlines.zealot_altar.incorrect_item").withStyle(ChatFormatting.DARK_RED), true);
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {

        return createTickerHelper(pBlockEntityType, BloodlinesBlockEntities.ZEALOT_ALTAR.get(), ZealotAltarBlockEntity::tick);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new ZealotAltarBlockEntity(pPos, pState);
    }
}
