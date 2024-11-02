package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodlineFrost extends VampireBloodline {
    public static final ResourceLocation ECTOTHERM = Bloodlines.rl("ectotherm");

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, LivingEntity entity, boolean cleanup) {
        int realRank = rank - 1;
        Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();
        double speedMul = 1;
        if(entity instanceof Player player) {
            ISkillHandler<IVampirePlayer> skillHandler =  this.getSkillHandler(player);
            applyConditionalModifier(attributes, BloodlineSkills.ECTOTHERM_MINING_SPEED_UNDERWATER.get(), Attributes.SUBMERGED_MINING_SPEED, new AttributeModifier(Bloodlines.rl("ectotherm_underwater_mining_speed"), CommonConfig.ectothermUnderwaterMiningSpeedMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), skillHandler, cleanup);
            applyConditionalModifier(attributes, BloodlineSkills.ECTOTHERM_TENTACLES.get(), Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(Bloodlines.rl("ectotherm_tentacles"), CommonConfig.ectothermTentacleInteractionDistance.get(), AttributeModifier.Operation.ADD_VALUE), skillHandler, cleanup);
            speedMul = skillHandler.isSkillEnabled(BloodlineSkills.ECTOTHERM_HYDRODYNAMIC_FORM.get()) ? CommonConfig.ectothermHydrodynamicFormSpeedMultiplier.get() : 1;
        } else {
            attributes.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(Bloodlines.rl("ectotherm_knockback_resistance"), CommonConfig.ectothermMobAdditionalKnockbackResistance.get().get(realRank), AttributeModifier.Operation.ADD_VALUE));
        }
        attributes.put(NeoForgeMod.SWIM_SPEED, new AttributeModifier(Bloodlines.rl("ectotherm_swim_speed"), CommonConfig.ectothermSwimSpeedMultipliers.get().get(realRank) * speedMul, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        return attributes;
    }

    private void applyConditionalModifier(Map<Holder<Attribute>, AttributeModifier> attributes, ISkill<?> skill, Holder<Attribute> attribute, AttributeModifier modifier, ISkillHandler<?> skillHandler, boolean cleanup) {
        if (skillHandler.isSkillEnabled(skill) || cleanup) {
            attributes.put(attribute, modifier);
        }
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

}
