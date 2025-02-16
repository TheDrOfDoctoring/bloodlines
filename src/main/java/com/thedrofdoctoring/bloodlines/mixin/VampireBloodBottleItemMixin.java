package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineBloodknight;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.vampire.DrinkBloodContext;
import de.teamlapen.vampirism.items.VampireBloodBottleItem;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(VampireBloodBottleItem.class)
public class VampireBloodBottleItemMixin extends Item {

    public VampireBloodBottleItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack, @NotNull LivingEntity p_344979_) {
        return 25;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @NotNull
    @Override
    public ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving) {
        if (entityLiving instanceof Player player) {
            VampirePlayer vp = VampirePlayer.get(player);
            if(vp.getSkillHandler().isSkillEnabled(BloodlineSkills.BLOODKNIGHT_STILL_BLOOD)) {
                VampirePlayer.get(player).drinkBlood(CommonConfig.bloodknightVampireBloodBottleNutrition.get(), CommonConfig.bloodknightVampireBloodBottleSaturation.get().floatValue(), new DrinkBloodContext(stack));
                stack.shrink(1);
                player.addItem(Items.GLASS_BOTTLE.getDefaultInstance());    
            }
        }
        return stack;
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player player, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = player.getItemInHand(pUsedHand);
        if(!Helper.isVampire(player)) return new InteractionResultHolder<>(InteractionResult.PASS, stack);

        VampirePlayer vp = VampirePlayer.get(player);
        if(!vp.getSkillHandler().isSkillEnabled(BloodlineSkills.BLOODKNIGHT_STILL_BLOOD)) {
            player.displayClientMessage(Component.translatable("text.bloodlines.vampire_blood_bottle"), true);
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }
        player.startUsingItem(pUsedHand);
        return new InteractionResultHolder<>(InteractionResult.sidedSuccess(pLevel.isClientSide), stack);
    }
}