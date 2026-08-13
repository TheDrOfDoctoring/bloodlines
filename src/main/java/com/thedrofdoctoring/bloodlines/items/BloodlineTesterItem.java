package com.thedrofdoctoring.bloodlines.items;

import com.thedrofdoctoring.bloodlines.advancements.BloodlinesAdvancementTriggers;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;


public class BloodlineTesterItem  extends Item {
    public BloodlineTesterItem(Properties props) {
        super(props);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity source) {
        Optional<IBloodlineManager> optManager = BloodlineHelper.getBloodlineData(target);
        if(source instanceof Player player && optManager.isPresent()) {
            IBloodlineManager blManager = optManager.get();
            String bloodlineName = blManager.getBloodline().getName();
            player.sendSystemMessage(Component.translatable("text.bloodlines.creature_bloodline", bloodlineName, blManager.getRank()));
            if(source instanceof ServerPlayer serverPlayer) {
                BloodlineManager sourceManager = BloodlineManager.get(serverPlayer);
                if(!blManager.getBloodline().equals(sourceManager.getBloodline())) {
                    BloodlinesAdvancementTriggers.CHECK_BLOODLINE.get().trigger(serverPlayer);
                }
            }
        }
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull TooltipContext pContext, List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("text.bloodlines.blood_tester_usage").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

}
