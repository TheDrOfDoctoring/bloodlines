package com.thedrofdoctoring.bloodlines.items;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import de.teamlapen.vampirism.entity.vampire.VampireBaronEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class LordslayerInjectionItem extends Item {
    public LordslayerInjectionItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack pStack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker) {

        if(pAttacker instanceof Player player && pTarget instanceof VampireBaronEntity && CommonConfig.nobleUniqueUnlock.get()) {
            float healthPercent = pTarget.getHealth() / pTarget.getMaxHealth();
            int lordRank = FactionPlayerHandler.get(player).getLordLevel();
            IBloodline bloodline = BloodlineManager.get(player).getBloodline();
            if(lordRank >= CommonConfig.nobleLordSlayerMinLordRank.get() && bloodline == null && pTarget.hasEffect(MobEffects.WEAKNESS) && healthPercent <= CommonConfig.nobleLordslayerBaronHealth.get().floatValue()) {
                pTarget.kill();
                pStack.shrink(1);
                BloodlineHelper.joinBloodlineGeneric(player, BloodlineRegistry.BLOODLINE_NOBLE.get(), Component.translatable("text.bloodlines.noble_join").withStyle(ChatFormatting.DARK_RED));
                return false;
            }

        } else {
            pAttacker.sendSystemMessage(Component.translatable("text.bloodlines.join_method_disabled", BloodlineRegistry.BLOODLINE_NOBLE.get().getName()));
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
