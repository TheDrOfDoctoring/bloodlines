package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.mixin.BloodStatsInvoker;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IBloodStats;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.entity.player.vampire.BloodStats;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
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
import org.w3c.dom.Attr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodlineBloodknight extends VampireBloodline{
    public static final ResourceLocation BLOOD_KNIGHT = Bloodlines.rl("bloodknight");

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, LivingEntity entity, boolean cleanup) {
        int realRank = rank - 1;
        Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();
        attributes.put(ModAttributes.BLOOD_EXHAUSTION, new AttributeModifier(Bloodlines.rl("bloodknight_exhaustion_decrease"), CommonConfig.bloodknightBloodThirstChange.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Bloodlines.rl("bloodknight_damage_increase"), CommonConfig.bloodknightDamageIncrease.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        if(entity instanceof Player player) {
        } else {

        }

        return attributes;
    }


    @Override
    public ResourceLocation getBloodlineId() {
        return BLOOD_KNIGHT;
    }

    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.BLOOD_KNIGHT;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return CommonConfig.bloodknightDefaults;
    }

    @Override
    public String getName() {
        return "Bloodknight";
    }

}
