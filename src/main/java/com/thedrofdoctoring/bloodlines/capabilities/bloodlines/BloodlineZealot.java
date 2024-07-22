package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.ISpecialAttributes;
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
import de.teamlapen.vampirism.util.Helper;
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

public class BloodlineZealot implements IBloodline {
    private final Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();
    public static final ResourceLocation ZEALOT = Bloodlines.rl("zealot");
    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, Player player) {
        int realRank = rank - 1;
        updateSpeed(player, realRank);
        ISkillHandler<IVampirePlayer> skillHandler =  this.getSkillHandler(player);
        attributes.put(ModAttributes.SUNDAMAGE, new AttributeModifier(Bloodlines.rl("zealot_sun_damage"), CommonConfig.zealotSunDamageMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(ModAttributes.BLOOD_EXHAUSTION, new AttributeModifier(Bloodlines.rl("zealot_exhaustion_decrease"), CommonConfig.zealotBloodExhaustionChange.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        applyConditionalModifier(BloodlineSkills.ZEALOT_SWIFT_SNEAK.get(), Attributes.SNEAKING_SPEED, new AttributeModifier(Bloodlines.rl("zealot_serpent_speed"), CommonConfig.zealotSerpentSpeedMultipliers.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_BASE), skillHandler);
        return attributes;
    }
    private void applyConditionalModifier(ISkill<?> skill, Holder<Attribute> attribute, AttributeModifier modifier, ISkillHandler<?> skillHandler) {
        if(skillHandler.isSkillEnabled(skill)) {
            attributes.put(attribute, modifier);
        }
    }

    @Override
    public void onBloodlineChange(Player player, int rank) {
        if(rank > 0) {
            IBloodline.super.onBloodlineChange(player, rank);
            updateSpeed(player, rank - 1);
        } else if(Helper.isVampire(player)){
            ISpecialAttributes specialAttributes = (ISpecialAttributes) (VampirePlayer.get(player)).getSpecialAttributes();
            specialAttributes.bloodlines$setStoneRunSpeed(1f);
        }
    }
    private void updateSpeed(Player player, int rank) {
        float speed = CommonConfig.zealotStoneSpeedMultiplier.get().get(rank).floatValue();
        ISpecialAttributes specialAttributes = (ISpecialAttributes) (VampirePlayer.get(player)).getSpecialAttributes();
        specialAttributes.bloodlines$setStoneRunSpeed(speed);
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return ZEALOT;
    }
    @Override
    public IPlayableFaction<?> getFaction() {
        return VReference.VAMPIRE_FACTION;
    }

    @Override
    public @Nullable ISkillHandler<IVampirePlayer> getSkillHandler(Player player) {
        return VampirePlayer.get(player).getSkillHandler();
    }
    @Override
    public BloodlineSkillType getSkillType() {
        return BloodlineSkillType.ZEALOT;
    }

    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.ZEALOT;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return CommonConfig.zealotDefaults;
    }

}
