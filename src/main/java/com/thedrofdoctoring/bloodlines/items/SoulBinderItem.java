package com.thedrofdoctoring.bloodlines.items;

import com.thedrofdoctoring.bloodlines.blocks.entities.PhylacteryBlockEntity;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SoulBinderItem extends Item {

    private boolean forceBind;

    public SoulBinderItem(boolean creative) {
        super(new Properties().stacksTo(1));
        if(creative) forceBind = true;
    }


    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        BlockState target = level.getBlockState(pos);


        if(!level.isClientSide && target.is(BloodlinesBlocks.PHYLACTERY) && pContext.getPlayer() != null) {
            BlockEntity be = level.getBlockEntity(pos);
            Player user = pContext.getPlayer();
            BloodlineManager manager = BloodlineManager.get(user);
            IBloodline bloodline = manager.getBloodline();
            if(be instanceof PhylacteryBlockEntity phylactery && bloodline == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get() && manager.getBloodlineState().isPresent()) {
                Player owner = phylactery.getOwner();
                BloodlineGravebound.State state = (BloodlineGravebound.State) manager.getBloodlineState().get();

                // We only bind with the regular soul binder if the phylactery doesn't have an owner.
                if(!phylactery.hasOwner() && !state.hasPhylactery()) {
                    phylactery.setOwner(user);
                    state.setPhylactery(pos, level.dimension().location());
                    user.displayClientMessage(Component.translatable("text.bloodlines.phylactery_bound", pos.toShortString()), true);
                    manager.sync(false);
                    state.updateCache(manager.getRank());
                    return InteractionResult.SUCCESS;

                // If owner matches user, then we add souls to phylactery
                } else if(phylactery.getOwnerUUID().equals(user.getUUID())) {

                    int amount = user.isShiftKeyDown() ? 5 : 1;
                    if(state.getSouls() <= 0) return InteractionResult.SUCCESS;

                    int used = state.addSouls(-amount);
                    int usedAfter = phylactery.addSouls(used);
                    state.addSouls(used - usedAfter);

                    int remaining = phylactery.getStoredSouls();
                    state.updateCache(manager.getRank());
                    phylactery.setChanged();
                    if(remaining == phylactery.getMaxStoredSouls()) {
                        user.displayClientMessage(Component.translatable("text.bloodlines.phylactery_max", remaining), true);
                    } else {
                        user.displayClientMessage(Component.translatable("text.bloodlines.phylactery_souls", remaining), true);
                    }
                    manager.sync(false);
                    user.getCooldowns().addCooldown(this, 10);

                    return InteractionResult.SUCCESS;

                // Creative Soul Binder ignores other conditions, forces the phylactery to be bound even if it already has an owner.
                } else if(forceBind) {
                    boolean shouldRebind = !user.isShiftKeyDown();

                    // Unbinding phylactery from the original owner
                    if(phylactery.hasOwner()) {
                        BloodlineManager ownerBloodline = BloodlineManager.get(owner);
                        if(ownerBloodline.getBloodlineState().isPresent() && ownerBloodline.getBloodline() == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get()) {
                            BloodlineGravebound.State ownerState = (BloodlineGravebound.State) ownerBloodline.getBloodlineState().get();
                            ownerState.removePhylactery();
                        }
                    }
                    if(shouldRebind) {
                        // Unbinding old phylactery from the new owner
                        if(state.getPhylacteryPos() != null) {
                            BlockEntity old = level.getBlockEntity(state.getPhylacteryPos());
                            if(old instanceof PhylacteryBlockEntity oldPhylactery) {
                                oldPhylactery.setOwner(null);
                            }
                        }
                        phylactery.setOwner(user);
                        state.setPhylactery(pos, level.dimension().location());
                        user.displayClientMessage(Component.translatable("text.bloodlines.phylactery_bound", pos.toShortString()), true);
                    }

                    return InteractionResult.SUCCESS;
                }
                manager.sync(false);
                state.updateCache(manager.getRank());
                phylactery.setChanged();
                phylactery.requestModelDataUpdate();
            }

        }
        return super.useOn(pContext);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(BloodlineHelper.hasBloodline(BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), pPlayer)) {
            BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(pPlayer);
            if(state == null) return super.use(pLevel, pPlayer, pUsedHand);
            boolean hasPhylactery = state.getPhylacteryPos() != null;

            if (hasPhylactery && state.getPhylacteryDimension() != null) {
                ResourceLocation currentDimension = pLevel.dimension().location();
                ResourceLocation phylacteryDimension = state.getPhylacteryDimension();

                if (phylacteryDimension.equals(currentDimension)) {
                    pPlayer.displayClientMessage(Component.translatable("text.bloodlines.phylactery_location", state.getPhylacteryPos().toShortString()), true);
                } else {
                    pPlayer.displayClientMessage(Component.translatable("text.bloodlines.phylactery_wrong_dimension"), true);
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
