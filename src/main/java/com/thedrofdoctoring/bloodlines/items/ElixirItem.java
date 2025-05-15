package com.thedrofdoctoring.bloodlines.items;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ElixirItem extends Item {

    private final Holder<MobEffect> effect;
    private final Supplier<Integer> effectDuration;

    public ElixirItem(Properties pProperties, Holder<MobEffect> effect, Supplier<Integer> effectDuration) {
        super(pProperties.stacksTo(1));
        this.effect = effect;
        this.effectDuration = effectDuration;
    }

    public int getUseDuration(@NotNull ItemStack pStack, @NotNull LivingEntity p_344979_) {
        return 60;
    }
    @NotNull
    @Override
    public UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        pStack.shrink(1);
        pLivingEntity.addEffect(new MobEffectInstance(effect, effectDuration.get()));
        return new ItemStack(Items.GLASS_BOTTLE);
    }
    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, @NotNull Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
