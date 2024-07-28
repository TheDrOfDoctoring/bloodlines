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
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodlineFrost implements IBloodline {
    public static final ResourceLocation ECTOTHERM = Bloodlines.rl("ectotherm");
    private final Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, Player player, boolean cleanup) {
        int realRank = rank - 1;
        attributes.clear();
        ISkillHandler<IVampirePlayer> skillHandler =  this.getSkillHandler(player);
        double speedMul = skillHandler.isSkillEnabled(BloodlineSkills.ECTOTHERM_HYDRODYNAMIC_FORM.get()) ? CommonConfig.ectothermHydrodynamicFormSpeedMultiplier.get() : 1;
        attributes.put(NeoForgeMod.SWIM_SPEED, new AttributeModifier(Bloodlines.rl("ectotherm_swim_speed"), CommonConfig.ectothermSwimSpeedMultipliers.get().get(realRank) * speedMul, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        applyConditionalModifier(BloodlineSkills.ECTOTHERM_TENTACLES.get(), Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(Bloodlines.rl("ectotherm_tentacles"), CommonConfig.ectothermTentacleInteractionDistance.get(), AttributeModifier.Operation.ADD_VALUE), skillHandler, cleanup);
        applyConditionalModifier(BloodlineSkills.ECTOTHERM_MINING_SPEED_UNDERWATER.get(), Attributes.SUBMERGED_MINING_SPEED, new AttributeModifier(Bloodlines.rl("ectotherm_underwater_mining_speed"), CommonConfig.ectothermUnderwaterMiningSpeedMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), skillHandler, cleanup);
        return attributes;
    }

    private void applyConditionalModifier(ISkill<?> skill, Holder<Attribute> attribute, AttributeModifier modifier, ISkillHandler<?> skillHandler, boolean cleanup) {
        if (skillHandler.isSkillEnabled(skill) || cleanup) {
            attributes.put(attribute, modifier);
        }
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return ECTOTHERM;
    }

    @Override
    public IPlayableFaction<?> getFaction() {
        return VReference.VAMPIRE_FACTION;
    }

    @Override
    public @Nullable ISkillHandler<IVampirePlayer> getSkillHandler(net.minecraft.world.entity.player.Player player) {
        return VampirePlayer.get(player).getSkillHandler();
    }

    @Override
    public BloodlineSkillType getSkillType() {
        return BloodlineSkillType.ECTOTHERM;
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
