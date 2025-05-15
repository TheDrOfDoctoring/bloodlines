package com.thedrofdoctoring.bloodlines.effects;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BloodFrenzyEffect extends MobEffect {

    public BloodFrenzyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }

    @Override
    public void onEffectAdded(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectAdded(pLivingEntity, pAmplifier);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, Bloodlines.rl("blood_frenzy_damage"), CommonConfig.bloodknightBloodFrenzyDamageBonus.get(), AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.MAX_HEALTH, Bloodlines.rl("blood_frenzy_max_health"), CommonConfig.bloodknightBloodFrenzyMaxHealthBonus.get(), AttributeModifier.Operation.ADD_VALUE);
        if(pAmplifier > 0) {
            this.addAttributeModifier(Attributes.MOVEMENT_SPEED, Bloodlines.rl("blood_frenzy_speed"), CommonConfig.bloodknightBloodFrenzy2SpeedBonus.get(), AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            this.addAttributeModifier(Attributes.JUMP_STRENGTH, Bloodlines.rl("blood_frenzy_jump_height"), CommonConfig.bloodknightBloodFrenzy2JumpHeightBonus.get(), AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        }
        if(pLivingEntity instanceof Player player && Helper.isVampire(player)) {
            VampirePlayer vp = VampirePlayer.get(player);
            if(vp.getSkillHandler().isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FRENZIED_STRIKES.get())) {
                this.addAttributeModifier(Attributes.ATTACK_SPEED, Bloodlines.rl("blood_frenzy_attack_speed"), CommonConfig.bloodknightBloodFrenzyAttackSpeedMult.get(), AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

            }
        }

    }


    @Override
    protected @NotNull String getOrCreateDescriptionId() {
        return "action.bloodlines.blood_frenzy";
    }
}
