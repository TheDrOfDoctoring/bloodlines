package com.thedrofdoctoring.bloodlines.items;

import com.thedrofdoctoring.bloodlines.core.BloodlineComponents;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import com.thedrofdoctoring.bloodlines.items.attachments.ChaliceBlood;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.vampire.IVampire;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.vampire.DrinkBloodContext;
import de.teamlapen.vampirism.fluids.BloodHelper;
import de.teamlapen.vampirism.proxy.ClientProxy;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BottomlessChaliceItem extends Item {

    //This is effectively identical to the BloodBottleItem with a few minor changes to limit to the correct user and increase capacity.

    public static final int AMOUNT = 250;
    private static final int MULTIPLIER = VReference.FOOD_TO_FLUID_BLOOD;
    public static final int CAPACITY = AMOUNT * MULTIPLIER;
    public BottomlessChaliceItem(Properties props) {
        super(props);
    }
    public static @NotNull ItemStack getStackDamage (int damage) {
        ItemStack stack = new ItemStack(BloodlinesItems.CHALICE_ITEM.get());
        stack.set(BloodlineComponents.CHALICE_BLOOD, new ChaliceBlood(damage));
        return stack;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, @NotNull LevelReader world, @NotNull BlockPos pos, Player player) {
        if (world instanceof Level level) {
            return level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null) != null;
        }
        return false;
    }


    @NotNull
    @Override
    public ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving) {
        if (entityLiving instanceof IVampire) {
            ItemStack copy = stack.copy();
            int blood = BloodHelper.getBlood(stack);
            int drink = Math.min(blood, MULTIPLIER);
            ItemStack[] result = new ItemStack[1];
            int amt = BloodHelper.drain(stack, drink, IFluidHandler.FluidAction.EXECUTE, true, containerStack -> result[0] = containerStack);
            ((IVampire) entityLiving).drinkBlood(amt / MULTIPLIER, 0, new DrinkBloodContext(copy));
            return result[0];
        }
        return FluidUtil.getFluidHandler(stack).map(IFluidHandlerItem::getContainer).orElseGet(() -> super.finishUsingItem(stack, worldIn, entityLiving));
    }


    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity p_344979_) {
        return 15;
    }

    @NotNull
    @Override
    public UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltips, flag);
        Player player = ClientProxy.get().getClientPlayer();
        int blood = stack.getOrDefault(BloodlineComponents.CHALICE_BLOOD.get(), ChaliceBlood.EMPTY).blood();
        if (player != null && Helper.isVampire(player)) {
            if (!VampirePlayer.getOpt(player).map(vp -> vp.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_CHALICE_SKILL.get())).orElse(false)) {
                tooltips.add(Component.translatable("text.bloodlines.chalice").withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                tooltips.add(Component.translatable("text.bloodlines.chalice_blood", blood * MULTIPLIER).withStyle(ChatFormatting.DARK_RED));
            }
        }
    }
    public void bloodUpdated(int newAmount, ItemStack stack) {
        float percentage = (float) newAmount / AMOUNT;
        if(newAmount == 0) {
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(0));
        }
        else if(newAmount > 1 && percentage <= 0.25f) {
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(1));
        }
        else if(percentage > 0.25f && percentage <= 0.5f) {
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(2));
        } else if(percentage > 0.5f) {
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(3));
        }
    }


    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity pLivingEntity, @NotNull ItemStack stack, int count) {
        if (pLivingEntity instanceof IVampire) return;
        if (!(pLivingEntity instanceof Player) || !pLivingEntity.isAlive()) {
            pLivingEntity.releaseUsingItem();
            return;
        }
        ItemStack copy = stack.copy();
        int blood = BloodHelper.getBlood(stack);
        VampirePlayer vampire = VampirePlayer.getOpt((Player) pLivingEntity).orElse(null);
        if (vampire == null || vampire.getLevel() == 0 || blood == 0 || !vampire.getBloodStats().needsBlood()) {
            pLivingEntity.releaseUsingItem();
            return;
        }
        if (!vampire.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_CHALICE_SKILL.get())) {
            pLivingEntity.releaseUsingItem();;
            return;
        }

        if (blood > 0 && count == 1) {
            InteractionHand activeHand = pLivingEntity.getUsedItemHand();
            int drink = Math.min(blood, 3 * MULTIPLIER);
            if (BloodHelper.drain(stack, drink, IFluidHandler.FluidAction.EXECUTE, true, containerStack -> pLivingEntity.setItemInHand(activeHand, containerStack)) > 0) {
                vampire.drinkBlood(Math.round(((float) drink) / VReference.FOOD_TO_FLUID_BLOOD), 0.45F, false, new DrinkBloodContext(copy));
            }

            blood = BloodHelper.getBlood(stack);
            if (blood > 0) {
                pLivingEntity.startUsingItem(pLivingEntity.getUsedItemHand());
            }
        }
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, @NotNull Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        return VampirePlayer.getOpt(playerIn).map(vampire -> {
            if (vampire.getLevel() == 0) return new InteractionResultHolder<>(InteractionResult.PASS, stack);
            if (!vampire.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_CHALICE_SKILL.get())) return new InteractionResultHolder<>(InteractionResult.PASS, stack);
            if (vampire.getBloodStats().needsBlood() && stack.getCount() == 1 && BloodHelper.getBlood(stack) != 0) {
                playerIn.startUsingItem(handIn);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            } else if (BloodHelper.getBlood(stack) == 0) {
                playerIn.displayClientMessage(Component.translatable("text.bloodlines.chalice_out_of_blood"), true);
            }
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }).orElse(new InteractionResultHolder<>(InteractionResult.PASS, stack));
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return false;
    }
}
