package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkillType;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodlineNoble implements IBloodline {
    private final Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();
    public static final ResourceLocation NOBLE = Bloodlines.rl("noble");
    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, Player player, boolean cleanup) {
        int realRank = rank - 1;
        attributes.clear();
        attributes.put(Attributes.ATTACK_SPEED, new AttributeModifier(Bloodlines.rl("noble_attack_speed_multiplier"), CommonConfig.nobleAttackSpeedIncrease.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attributes.put(Attributes.MAX_HEALTH, new AttributeModifier(Bloodlines.rl("noble_health_modifier"), CommonConfig.nobleMaxHealthChange.get().get(realRank), AttributeModifier.Operation.ADD_VALUE));
        attributes.put(ModAttributes.BLOOD_EXHAUSTION, new AttributeModifier(Bloodlines.rl("noble_exhaustion_decrease"), CommonConfig.nobleBloodThirstChange.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(ModAttributes.NEONATAL_DURATION, new AttributeModifier(Bloodlines.rl("noble_neonatal_modifier"), CommonConfig.nobleNeonatalMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        ISkillHandler<IVampirePlayer> skillHandler =  this.getSkillHandler(player);
        applyConditionalModifier(BloodlineSkills.NOBLE_FASTER_RESURRECT.get(), ModAttributes.DBNO_DURATION, new AttributeModifier(Bloodlines.rl("noble_resurrection_modifier"), CommonConfig.nobleFasterResurrectionMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), skillHandler, cleanup);
        applyConditionalModifier(BloodlineSkills.NOBLE_FASTER_MOVEMENT_SPEED.get(), Attributes.MOVEMENT_SPEED, new AttributeModifier(Bloodlines.rl("noble_speed_increase"), CommonConfig.nobleSpeedMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), skillHandler, cleanup);
        return attributes;
    }
    private void applyConditionalModifier(ISkill<?> skill, Holder<Attribute> attribute, AttributeModifier modifier, ISkillHandler<?> skillHandler, boolean cleanup) {
        if(skillHandler.isSkillEnabled(skill) || cleanup) {
            attributes.put(attribute, modifier);
        }
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return NOBLE;
    }
    @Override
    public IPlayableFaction<?> getFaction() {
        return VReference.VAMPIRE_FACTION;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return CommonConfig.nobleDefaults;
    }

    @Override
    public BloodlineSkillType getSkillType() {
        return BloodlineSkillType.NOBLE;
    }

    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.NOBLE;
    }

    @Override
    public @Nullable ISkillHandler<IVampirePlayer> getSkillHandler(Player player) {
        return VampirePlayer.get(player).getSkillHandler();
    }
}
