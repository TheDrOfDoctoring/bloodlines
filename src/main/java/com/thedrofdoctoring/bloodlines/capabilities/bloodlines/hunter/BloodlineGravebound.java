package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Map;

public class BloodlineGravebound extends HunterBloodline{
    public static final ResourceLocation GRAVEBOUND = Bloodlines.rl("gravebound");

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, LivingEntity entity, boolean cleanup) {
        return Map.of();
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return GRAVEBOUND;
    }

    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.GRAVEBOUND;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return HunterBloodlinesConfig.graveboundDefaults;
    }

    @Override
    public String getName() {
        return "Gravebound";
    }
}
