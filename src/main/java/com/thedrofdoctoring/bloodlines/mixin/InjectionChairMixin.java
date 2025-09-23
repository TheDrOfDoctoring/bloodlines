package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import de.teamlapen.vampirism.blocks.MedChairBlock;
import de.teamlapen.vampirism.core.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MedChairBlock.class)
public class InjectionChairMixin {

    @Inject(method = "handleInjections", at = @At("HEAD"), cancellable = true)
    public void handlePurityInjection(Player playerIn, Level world, ItemStack stack, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if(stack.is(BloodlinesItems.PURITY_INJECTION.get())) {
            if(playerIn != null && playerIn.level().getBlockState(pos).is(ModBlocks.MED_CHAIR.get()) && BloodlineManager.get(playerIn).getBloodline() != null) {
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
                cir.setReturnValue(true);
            }
            cir.setReturnValue(false);
        }

    }
}
