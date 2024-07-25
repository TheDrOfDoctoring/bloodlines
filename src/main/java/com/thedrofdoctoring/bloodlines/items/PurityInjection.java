package com.thedrofdoctoring.bloodlines.items;

import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PurityInjection extends Item {
    public PurityInjection(Properties pProperties) {
        super(pProperties);
    }
    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, @NotNull Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if(BloodlineManager.get(playerIn).getBloodline() != null) {
            BloodlineManager bl = BloodlineManager.get(playerIn);
            int oldRank = bl.getRank();
            IBloodline oldBl = bl.getBloodline();
            bl.setBloodline(null);
            bl.setRank(0);
            bl.onBloodlineChange(oldBl, oldRank);
            playerIn.displayClientMessage(Component.translatable("text.bloodlines.left_bloodline").withStyle(ChatFormatting.DARK_RED), true);
            playerIn.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 2));
            playerIn.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 3));
            stack.shrink(1);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }
}
