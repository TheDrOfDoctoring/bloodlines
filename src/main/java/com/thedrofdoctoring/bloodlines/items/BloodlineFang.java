package com.thedrofdoctoring.bloodlines.items;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BloodlineFang extends Item {

    private final ResourceLocation bloodlineId;

    public BloodlineFang(Properties props, ResourceLocation bloodlineId) {
        super(props);
        this.bloodlineId = bloodlineId;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        IBloodline bloodline = BloodlineHelper.getBloodlineById(bloodlineId);
        if(bloodline != null) {
            BloodlineManager bl = BloodlineManager.get(player);
            IPlayableFaction<?> faction = FactionPlayerHandler.get(player).getCurrentFaction();
            if(faction == null) {
                player.displayClientMessage(Component.translatable("text.bloodlines.need_faction"), true);
                return new InteractionResultHolder<>(InteractionResult.FAIL, player.getItemInHand(hand));
            }
            if(bl.getBloodline() != null) {
                player.displayClientMessage(Component.translatable("text.bloodlines.bloodline_active"), true);
                return new InteractionResultHolder<>(InteractionResult.FAIL, player.getItemInHand(hand));

            } else if(bloodline.getFaction() != faction) {
                player.displayClientMessage(Component.translatable("text.bloodlines.wrong_faction", faction.getNamePlural()), true);
                return new InteractionResultHolder<>(InteractionResult.FAIL, player.getItemInHand(hand));
            } else {
                BloodlineHelper.joinBloodlineGeneric(player, bloodline, Component.translatable("text.bloodlines.new_bloodline", bloodline.getName()).withStyle(ChatFormatting.DARK_RED));
                player.getItemInHand(hand).shrink(1);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }
}

