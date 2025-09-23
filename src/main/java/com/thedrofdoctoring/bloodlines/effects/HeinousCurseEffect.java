package com.thedrofdoctoring.bloodlines.effects;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class HeinousCurseEffect extends MobEffect {
    public HeinousCurseEffect() {
        super(MobEffectCategory.HARMFUL, 0xF00000);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.hurt(pLivingEntity.damageSources().magic(), 3f);
        if(!pLivingEntity.hasEffect(MobEffects.BLINDNESS)) {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 2));
        }
        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 15, 1));
        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 15, 1));

        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration % 10 == 0;
    }
    public static void handleHeinousEnding(Player player) {
        BloodlineManager man = BloodlineManager.get(player);
        if(man.getBloodline() == null && CommonConfig.bloodknightUniqueUnlock.get()) {
            BloodlineHelper.joinBloodlineGeneric(player, BloodlineRegistry.BLOODLINE_BLOODKNIGHT.get(), Component.translatable("text.bloodlines.bloodknight_join").withStyle(ChatFormatting.DARK_RED));
        }

    }
    
}
