package com.thedrofdoctoring.bloodlines.items;

import com.thedrofdoctoring.bloodlines.capabilities.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.vampirism.core.ModSounds;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class BloodlineFang extends Item {

    ResourceLocation bloodlineId;

    public BloodlineFang(Properties props, ResourceLocation bloodlineId) {
        super(props);
        this.bloodlineId = bloodlineId;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        IBloodline bloodline = BloodlineHelper.getBloodlineById(bloodlineId);
        if(bloodline != null && Helper.isVampire(player)) {
            BloodlineManager.getOpt(player).ifPresent(bl -> {
                if(bl.getBloodline() != null) {
                    player.displayClientMessage(Component.translatable("text.bloodlines.bloodline_active"), true);
                } else {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ENTITY_VAMPIRE_SCREAM.get(), SoundSource.PLAYERS, 1, 1);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1, 1);
                    bl.setRank(1);
                    bl.setBloodline(bloodline);
                    bl.onBloodlineChange(null, 0);
                    String bloodlineName = bloodlineId.getPath().substring(0, 1).toUpperCase() + bloodlineId.getPath().substring(1).toLowerCase();
                    player.displayClientMessage(Component.translatable("text.bloodlines.new_bloodline", bloodlineName).withStyle(ChatFormatting.DARK_RED), true);
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 3));
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 90, 2));
                    player.getItemInHand(hand).shrink(1);
                }
            });
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }
}

