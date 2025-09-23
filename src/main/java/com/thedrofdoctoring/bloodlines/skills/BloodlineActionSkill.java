package com.thedrofdoctoring.bloodlines.skills;

import com.mojang.datafixers.util.Either;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.entity.player.skills.ActionSkill;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class BloodlineActionSkill<T extends IFactionPlayer<T>> extends ActionSkill<T> implements IBloodlineSkill {

    private final IBloodline bloodline;
    private final boolean hasCost;
    private final int requiredBloodlineRank;

    public BloodlineActionSkill(Supplier<? extends IAction<T>> action, int skillPointCost, boolean customDescription, IBloodline bloodline, boolean hasCost) {
        super(action, Either.left(bloodline.getSkillTree()), skillPointCost, customDescription);
        this.bloodline = bloodline;
        this.hasCost = hasCost;
        this.requiredBloodlineRank = 0;
        BloodlineSkills.addSkill(this, bloodline);
    }
    public BloodlineActionSkill(Supplier<? extends IAction<T>> action, int skillPointCost, boolean customDescription, IBloodline bloodline, boolean hasCost, int requiredBloodlineRank) {
        super(action, Either.left(bloodline.getSkillTree()), skillPointCost, customDescription);
        this.bloodline = bloodline;
        this.hasCost = hasCost;
        this.requiredBloodlineRank = requiredBloodlineRank;
        BloodlineSkills.addSkill(this, bloodline);
    }
    @Override
    public IBloodline getBloodline() {
        return this.bloodline;
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return this.bloodline.getBloodlineId();
    }

    @Override
    public boolean requiresBloodlineSkillPoints() {
        return this.hasCost;
    }

    @Override
    public int requiredBloodlineRank() {
        return requiredBloodlineRank;
    }
}
