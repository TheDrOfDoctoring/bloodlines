package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodlineNoble extends VampireBloodline {
    public static final ResourceLocation NOBLE = Bloodlines.rl("noble");
    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, LivingEntity entity, boolean cleanup) {
        int realRank = rank - 1;
        Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();
        attributes.put(Attributes.ATTACK_SPEED, new AttributeModifier(Bloodlines.rl("noble_attack_speed_multiplier"), CommonConfig.nobleAttackSpeedIncrease.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attributes.put(Attributes.MAX_HEALTH, new AttributeModifier(Bloodlines.rl("noble_health_modifier"), CommonConfig.nobleMaxHealthChange.get().get(realRank), AttributeModifier.Operation.ADD_VALUE));
        attributes.put(ModAttributes.BLOOD_EXHAUSTION, new AttributeModifier(Bloodlines.rl("noble_exhaustion_decrease"), CommonConfig.nobleBloodThirstChange.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(ModAttributes.NEONATAL_DURATION, new AttributeModifier(Bloodlines.rl("noble_neonatal_modifier"), CommonConfig.nobleNeonatalMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        if(entity instanceof Player player) {
            ISkillHandler<IVampirePlayer> skillHandler =  this.getSkillHandler(player);
            applyConditionalModifier(attributes, BloodlineSkills.NOBLE_FASTER_RESURRECT.get(), ModAttributes.DBNO_DURATION, new AttributeModifier(Bloodlines.rl("noble_resurrection_modifier"), CommonConfig.nobleFasterResurrectionMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), skillHandler, cleanup);
            applyConditionalModifier(attributes, BloodlineSkills.NOBLE_FASTER_MOVEMENT_SPEED.get(), Attributes.MOVEMENT_SPEED, new AttributeModifier(Bloodlines.rl("noble_speed_increase"), CommonConfig.nobleSpeedMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), skillHandler, cleanup);
        } else {
            attributes.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(Bloodlines.rl("noble_mob_speed_increase"), CommonConfig.nobleMobSpeedIncrease.get().get(realRank), AttributeModifier.Operation.ADD_VALUE));
        }
        return attributes;
    }


    @Override
    public ResourceLocation getBloodlineId() {
        return NOBLE;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return CommonConfig.nobleDefaults;
    }

    @Override
    public String getName() {
        return "Noble";
    }


    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.NOBLE;
    }

}
