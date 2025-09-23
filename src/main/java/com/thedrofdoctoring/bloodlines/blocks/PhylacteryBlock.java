package com.thedrofdoctoring.bloodlines.blocks;

import com.mojang.serialization.MapCodec;
import com.thedrofdoctoring.bloodlines.blocks.entities.PhylacteryBlockEntity;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.items.SoulBinderItem;
import de.teamlapen.vampirism.core.ModParticles;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhylacteryBlock extends BaseEntityBlock {

    public static final MapCodec<PhylacteryBlock> CODEC = simpleCodec(PhylacteryBlock::new);

    public PhylacteryBlock(Properties pProperties) {
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
        shape = Shapes.join(shape, Shapes.box(0.25, 0.375, 0.25, 0.75, 0.875 ,0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.40625, 0, 0.40625, 0.59375, 0.375,0.59375), BooleanOp.OR);

        return shape;
    }


    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void playerDestroy(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable BlockEntity pBlockEntity, @NotNull ItemStack pTool) {
        for(int i = 0; i < 5; i++) {
            pLevel.playSound(null, pPos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS);
        }
        ModParticles.spawnParticlesServer(pLevel, ParticleTypes.SOUL, pPos.getX(), pPos.getY(), pPos.getZ(), 50, 0f, 0.5f, 0f, 0.1f);
        if(!pLevel.isClientSide()) {
            syncDestroy((PhylacteryBlockEntity) pBlockEntity);
        }

        super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull BlockHitResult pHitResult) {
        if(pPlayer.getMainHandItem().getItem() instanceof SoulBinderItem) return InteractionResult.FAIL;
        if(pLevel.getBlockEntity(pPos) instanceof PhylacteryBlockEntity phylactery && !pLevel.isClientSide) {
            if(pPlayer.getUUID().equals(phylactery.getOwnerUUID()) && BloodlineHelper.hasBloodline(BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), pPlayer)) {
                BloodlineManager manager = BloodlineManager.get(pPlayer);
                BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(manager);
                if(state == null) return InteractionResult.FAIL;

                int amount = pPlayer.isShiftKeyDown() ? 5 : 1;

                if(phylactery.getStoredSouls() <= 0) {
                    pPlayer.displayClientMessage(Component.translatable("text.bloodlines.phylactery_souls", phylactery.getStoredSouls()), true);
                    return InteractionResult.SUCCESS;
                }

                int used = phylactery.addSouls(-amount);
                int usedAfter = state.addSouls(used);
                phylactery.addSouls(used - usedAfter);
                phylactery.setChanged();
                phylactery.requestModelDataUpdate();
                pPlayer.displayClientMessage(Component.translatable("text.bloodlines.phylactery_souls", phylactery.getStoredSouls()), true);


                state.updateCache(manager.getRank());
                manager.sync(false);
                return InteractionResult.SUCCESS;

            }
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

    @Override
    protected void onRemove(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pMovedByPiston) {
        if(!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof PhylacteryBlockEntity phylactery && !pMovedByPiston) {
            syncDestroy(phylactery);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    private void syncDestroy(PhylacteryBlockEntity phylactery) {
        if(phylactery != null && phylactery.hasOwner()) {
            BloodlineManager manager = BloodlineManager.get(phylactery.getOwner());
            phylactery.getOwner().displayClientMessage(Component.translatable("text.bloodlines.phylactery_destroyed").withStyle(ChatFormatting.DARK_RED), true);
            BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(manager);
            if(state != null) {
                state.removePhylactery();
                manager.sync(false);
                state.updateCache(manager.getRank());
            }
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new PhylacteryBlockEntity(pPos, pState);
    }
}
