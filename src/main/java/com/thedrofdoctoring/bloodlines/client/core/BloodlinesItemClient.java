package com.thedrofdoctoring.bloodlines.client.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.BloodlinesComponents;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import com.thedrofdoctoring.bloodlines.items.BottomlessChaliceItem;
import com.thedrofdoctoring.bloodlines.items.attachments.ChaliceBlood;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.items.BloodBottleFluidHandler;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BloodlinesItemClient {
    public static void registerItemModelProperties() {
        ItemProperties.register(BloodlinesItems.CHALICE_ITEM.get(), Bloodlines.rl("chalice_blood"), (stack, world, entity, tint) -> {
            return stack.getOrDefault(BloodlinesComponents.CHALICE_BLOOD, ChaliceBlood.EMPTY).blood() / (float) BottomlessChaliceItem.AMOUNT;
        });
    }

    public static void handleChaliceHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltips, @NotNull TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        int blood = stack.getOrDefault(BloodlinesComponents.CHALICE_BLOOD.get(), ChaliceBlood.EMPTY).blood();
        if (player != null && Helper.isVampire(player)) {
            if (!VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_CHALICE_SKILL.get())) {
                tooltips.add(Component.translatable("text.bloodlines.chalice").withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                tooltips.add(Component.translatable("text.bloodlines.chalice_blood", blood * BloodBottleFluidHandler.MULTIPLIER).withStyle(ChatFormatting.DARK_RED));
            }
        }
    }
}
