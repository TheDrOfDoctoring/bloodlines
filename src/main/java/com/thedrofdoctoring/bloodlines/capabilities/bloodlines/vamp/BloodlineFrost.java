package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineEventReceiver;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModDamageTypes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BloodlineFrost extends VampireBloodline implements IBloodlineEventReceiver {
    public static final ResourceLocation ECTOTHERM = Bloodlines.rl("ectotherm");

    private static final ResourceLocation reducedMovementSpeed = Bloodlines.rl("ectotherm_biome_reduced_movement_speed");
    private static final ResourceLocation increasedMovementSpeed = Bloodlines.rl("ectotherm_biome_increased_movement_speed");
    private static final ResourceLocation reducedMaxHealth = Bloodlines.rl("ectotherm_biome_reduced_max_haelth");

    @SuppressWarnings("ConstantConditions")
    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, LivingEntity entity, boolean cleanup) {
        int realRank = rank - 1;
        Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();
        double speedMul = 1;
        if(entity instanceof Player player) {
            if(cleanup) {
                BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), reducedMovementSpeed);
                BloodlineManager.removeModifier(player.getAttribute(Attributes.MAX_HEALTH), reducedMaxHealth);
                BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), increasedMovementSpeed);
            }
            ISkillHandler<IVampirePlayer> skillHandler =  this.getSkillHandler(player);
            if(skillHandler != null) {
                speedMul = skillHandler.isSkillEnabled(BloodlineSkills.ECTOTHERM_HYDRODYNAMIC_FORM.get()) ? CommonConfig.ectothermHydrodynamicFormSpeedMultiplier.get() : 1;
            }
            applyConditionalModifier(attributes, BloodlineSkills.ECTOTHERM_MINING_SPEED_UNDERWATER.get(), Attributes.SUBMERGED_MINING_SPEED, new AttributeModifier(Bloodlines.rl("ectotherm_underwater_mining_speed"), CommonConfig.ectothermUnderwaterMiningSpeedMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), skillHandler, cleanup);
            applyConditionalModifier(attributes, BloodlineSkills.ECTOTHERM_TENTACLES.get(), Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(Bloodlines.rl("ectotherm_tentacles"), CommonConfig.ectothermTentacleInteractionDistance.get(), AttributeModifier.Operation.ADD_VALUE), skillHandler, cleanup);
        } else {
            attributes.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(Bloodlines.rl("ectotherm_knockback_resistance"), CommonConfig.ectothermMobAdditionalKnockbackResistance.get().get(realRank), AttributeModifier.Operation.ADD_VALUE));
        }

        attributes.put(NeoForgeMod.SWIM_SPEED, new AttributeModifier(Bloodlines.rl("ectotherm_swim_speed"), CommonConfig.ectothermSwimSpeedMultipliers.get().get(realRank) * speedMul, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        return attributes;
    }


    @Override
    public ResourceLocation getBloodlineId() {
        return ECTOTHERM;
    }

    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.ECTOTHERM;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return CommonConfig.ectothermDefaults;
    }

    @Override
    public String getName() {
        return "Ectotherm";
    }
    @SuppressWarnings("ConstantConditions")
    @Override
    public void tick(Player player) {
        if(player.tickCount % 5 == 0) {
            player.setTicksFrozen(0);
        }
        if(player.tickCount % 50 == 0) {

            int rank = BloodlineManager.get(player).getRank() - 1;
            int modifierRank = rank + 1;
            Holder<Biome> biome = player.level().getBiome(player.getOnPos());
            if (biome.is(Tags.Biomes.IS_HOT) && !biome.is(Tags.Biomes.IS_OCEAN)) {
                if (modifierRank >= CommonConfig.ectothermHotBiomeReducedMaxHealthRank.get() && !hasAttributeAlready(player, Attributes.MAX_HEALTH, reducedMaxHealth)) {
                    player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(reducedMaxHealth, CommonConfig.ectothermHotBiomeReducedMaxHealthAmount.get().get(rank), AttributeModifier.Operation.ADD_VALUE));
                }
                if (modifierRank >= CommonConfig.ectothermHotBiomeReducedMovementSpeedRank.get() && !hasAttributeAlready(player, Attributes.MOVEMENT_SPEED, reducedMovementSpeed)) {
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(reducedMovementSpeed, CommonConfig.ectothermHotBiomeReducedMovementSpeedMultiplier.get().get(rank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                }
            } else {
                BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), reducedMovementSpeed);
                BloodlineManager.removeModifier(player.getAttribute(Attributes.MAX_HEALTH), reducedMaxHealth);
            }
            if (biome.is(Tags.Biomes.IS_COLD)) {
                if (modifierRank >= CommonConfig.ectothermColdBiomeIncreasedMovementSpeedRank.get() && !hasAttributeAlready(player, Attributes.MOVEMENT_SPEED, increasedMovementSpeed)) {
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(increasedMovementSpeed, CommonConfig.ectothermColdBiomeSpeedMultiplier.get().get(rank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                }
            } else {
                BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), increasedMovementSpeed);
            }
        }
    }

    @Override
    public void onReceiveDamage(LivingIncomingDamageEvent event, LivingEntity bloodlineMember, int blRank) {

        if(event.getSource().is(ModDamageTypes.VAMPIRE_IN_FIRE) || event.getSource().is(ModDamageTypes.VAMPIRE_ON_FIRE)) {
            event.setAmount(Math.min(Float.MAX_VALUE, event.getAmount() * CommonConfig.ectothermFireDamageMultipliers.get().get(blRank).floatValue()));
        }

        if(event.getSource().is(ModDamageTypes.HOLY_WATER) && bloodlineMember instanceof Player player) {
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
            boolean diffusion = atts.getEctothermAtts().hasHolyWaterDiffusion;
            if(diffusion) {
                event.setAmount(event.getAmount() * CommonConfig.ectothermHolyWaterDiffusion.get().get(blRank).floatValue());
            }
        }
    }

    @Override
    public void onCrit(CriticalHitEvent event) {
        Player player = event.getEntity();
        if(event.getTarget() instanceof LivingEntity living) {
            VampirePlayer vp = VampirePlayer.get(player);
            if(vp.getActionHandler().isActionActive(BloodlineActions.ECTOTHERM_FROST_LORD_ACTION.get())) {

                if(vp.getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_FROZEN_ATTACK.get())) {
                    living.setTicksFrozen(CommonConfig.ectothermFrozenAttackFreezingAmount.get());
                }
                if(vp.getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_SLOWNESS_ATTACK.get())) {
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, CommonConfig.ectothermSlowingAttackSlownessDuration.get(), 0));
                }
            }
            if(!event.getTarget().getType().is(EntityTypeTags.UNDEAD) && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_POISONED_STRIKE.get())) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, CommonConfig.zealotPoisonedStrikeDuration.get(), 0));
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean hasAttributeAlready(Player player, Holder<Attribute> att, ResourceLocation rl) {
        return player.getAttribute(att).hasModifier(rl);
    }

    @Override
    public void onBloodDrink(BloodDrinkEvent.PlayerDrinkBloodEvent event, int rank, VampirePlayer bloodlinePlayer) {

    }
    public static class SpecialAttributes {
        public boolean hasHolyWaterDiffusion;
        public boolean icePhasing;
        public boolean frostControl;
        public boolean snowWalker;
    }
}
