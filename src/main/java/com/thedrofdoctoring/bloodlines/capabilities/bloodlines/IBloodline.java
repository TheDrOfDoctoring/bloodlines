package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkillType;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.util.RegUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface IBloodline {

    Codec<IBloodline> CODEC = RecordCodecBuilder.create(ins -> ins.group(ResourceLocation.CODEC.fieldOf("id").forGetter(IBloodline::getBloodlineId)).apply(ins, BloodlineHelper::getBloodlineById));
        /**
         *
         * @param rank - Bloodline Rank for default skills to be enabled
         * @param player - Player the bloodline belongs to
         * @return Map of attributes and AttributeModifiers that should be applied to the bloodline player.
         */
        Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, Player player);
    default void onBloodlineChange(Player player, int rank) {
        enableDefaultSkills(rank, player);
    }
    /**
     * @return Bloodline's ID
     */ 
    ResourceLocation getBloodlineId();

    /**
     * @return The faction that the bloodline is meant for. Each bloodline can only be for one faction.
     */

    IPlayableFaction<?> getFaction();

    @Nullable <T extends IFactionPlayer<T>> ISkillHandler<?> getSkillHandler(Player player);

    /**
     *
     * @return The skill type that belongs to the bloodline
     */
    BloodlineSkillType getSkillType();

    ResourceKey<ISkillTree> getSkillTree();

    /**
     * @return A list of skills that will be enabled by default.
     */

    ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills();
    //gross
    default void enableDefaultSkills(int rank, Player player) {
        if(!player.getCommandSenderWorld().isClientSide) {
            ModConfigSpec.ConfigValue<List<? extends String>>[] defaultSkills = getDefaultEnabledSkills();
            ISkillHandler<?> skillHandler = getSkillHandler(player);
            for(int i = 0; i < rank; i++ ) {
                defaultSkills[i].get().forEach(str -> {
                    ResourceLocation skill = ResourceLocation.parse(str);
                    //noinspection rawtypes;
                    ISkill iSkill = RegUtil.getSkill(skill);
                    if(RegUtil.getSkill(skill) == null || iSkill.getFaction().isEmpty() || iSkill.getFaction().get() != getFaction()) {
                        throw new IllegalStateException("Default Bloodline Enabled Skill " + skill + " is invalid!");
                    } else if(!skillHandler.isSkillEnabled(iSkill)) {
                        //noinspection unchecked
                        skillHandler.enableSkill(iSkill);
                    }
                });
            }
        }
    }
}
