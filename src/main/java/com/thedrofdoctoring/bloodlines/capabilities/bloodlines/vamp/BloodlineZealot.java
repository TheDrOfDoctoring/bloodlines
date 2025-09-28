package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineEventReceiver;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.particle.GenericParticleOptions;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodlineZealot extends VampireBloodline implements IBloodlineEventReceiver {
    public static final ResourceLocation ZEALOT = Bloodlines.rl("zealot");

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, LivingEntity entity, boolean cleanup) {

        int realRank = rank - 1;
        Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();
        attributes.put(ModAttributes.SUNDAMAGE, new AttributeModifier(Bloodlines.rl("zealot_sun_damage"), CommonConfig.zealotSunDamageMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(ModAttributes.BLOOD_EXHAUSTION, new AttributeModifier(Bloodlines.rl("zealot_exhaustion_decrease"), CommonConfig.zealotBloodExhaustionChange.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        if(entity instanceof Player player) {
            ISkillHandler<IVampirePlayer> skillHandler =  this.getSkillHandler(player);
            updateSpeed(realRank, BloodlinesPlayerAttributes.get(player).getZealotAtts(), skillHandler);
            applyConditionalModifier(attributes, BloodlineSkills.ZEALOT_SWIFT_SNEAK.get(), Attributes.SNEAKING_SPEED, new AttributeModifier(Bloodlines.rl("zealot_serpent_speed"), CommonConfig.zealotSerpentSpeedMultipliers.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_BASE), skillHandler, cleanup);
        } else {
            attributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Bloodlines.rl("zealot_mob_damage_increase"), CommonConfig.zealotMobDamageIncrease.get().get(realRank), AttributeModifier.Operation.ADD_VALUE));
        }

        return attributes;
    }

    @Override
    public void onReceiveDamage(LivingIncomingDamageEvent event, LivingEntity bloodlineMember, int blRank) {

        if(bloodlineMember instanceof Player player) {

            SpecialAttributes atts = BloodlinesPlayerAttributes.get(player).getZealotAtts();
            int brightness = player.getCommandSenderWorld().getRawBrightness(player.getOnPos().above(), 0);
            if (atts.hasShadowArmour && brightness <= CommonConfig.zealotShadowArmourLightLevel.get()) {
                event.setAmount(event.getAmount() * CommonConfig.zealotShadowArmourDamageMultiplier.get().get(blRank).floatValue());
                ModParticles.spawnParticlesServer(player.getCommandSenderWorld(), new GenericParticleOptions(ResourceLocation.fromNamespaceAndPath("minecraft", "generic_7"), 40, 0x000000, 0.2F), player.getX(), player.getY() + 0.5f, player.getZ(), 10, 0, 0.2f, 0, 0);
            }
            if (blRank + 1 >= CommonConfig.zealotBrightAreaDamageMultiplierRank.get() && brightness >= CommonConfig.zealotBrightAreaDamageMultiplierLightLevel.get()) {
                event.setAmount(Math.min(event.getAmount() * CommonConfig.zealotBrightAreaDamageMultiplier.get().floatValue(), Float.MAX_VALUE));
            }
            if (atts.hasHexProtection && (event.getSource().is(DamageTypes.MAGIC) || event.getSource().is(DamageTypes.INDIRECT_MAGIC))) {
                event.setAmount(event.getAmount() * CommonConfig.zealotHexProtectionMultiplier.get().get(blRank).floatValue());
            }
        }
    }

    @Override
    public void onBloodlineChange(LivingEntity entity, int rank) {
        super.onBloodlineChange(entity, rank);
        if(entity instanceof Player player) {
            SpecialAttributes atts = BloodlinesPlayerAttributes.get(player).getZealotAtts();
            if(Helper.isVampire(player)){
                atts.stoneSpeed = 1f;
            }
            updateSpeed(player, Math.max(rank - 1, 0), atts);

        }
    }

    @Override
    public void onCrit(CriticalHitEvent event) {
        Player player = event.getEntity();
        VampirePlayer vp = VampirePlayer.get(player);
        if(event.getTarget() instanceof LivingEntity living && !event.getTarget().getType().is(EntityTypeTags.UNDEAD) && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_POISONED_STRIKE.get())) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, CommonConfig.zealotPoisonedStrikeDuration.get(), 0));
        }
    }
    private void updateSpeed(Player player, int rank, SpecialAttributes attributes) {
        updateSpeed(rank, attributes, getSkillHandler(player));
    }
    private void updateSpeed(int rank, SpecialAttributes attributes, ISkillHandler<IVampirePlayer> skillHandler) {
        if(skillHandler != null && skillHandler.isSkillEnabled(BloodlineSkills.ZEALOT_STONE_SPEED.get())) {
            attributes.stoneSpeed = CommonConfig.zealotStoneSpeedMultiplier.get().get(rank).floatValue();
        } else {
            attributes.stoneSpeed = 1f;
        }
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return ZEALOT;
    }

    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.ZEALOT;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return CommonConfig.zealotDefaults;
    }

    @Override
    public String getName() {
        return "Zealot";
    }

    @Override
    public void onBloodDrink(BloodDrinkEvent.PlayerDrinkBloodEvent event, int blRank, VampirePlayer bloodlinePlayer) {}

    public static class SpecialAttributes {
        public boolean hasShadowArmour;
        public boolean hasHexProtection;
        public boolean hasTunneler;

        public float stoneSpeed;
    }
}
