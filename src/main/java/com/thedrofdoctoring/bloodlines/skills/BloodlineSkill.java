package com.thedrofdoctoring.bloodlines.skills;

import com.mojang.datafixers.util.Either;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.entity.player.skills.VampirismSkill;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public class BloodlineSkill <T extends IFactionPlayer<T>> extends VampirismSkill<T> implements IBloodlineSkill, ISkill<T> {
    private final ResourceLocation bloodlineId;
    private final int cost;
    private final IBloodline bloodline;
    private final boolean hasCost;

    BloodlineSkill(IBloodline bloodline, boolean desc, int cost) {
        this(bloodline, desc, cost, true);
    }
    @Override
    public @NotNull BloodlineSkill<T> setToggleActions(Consumer<T> activateIn, Consumer<T> deactivateIn) {
        return (BloodlineSkill<T>) super.setToggleActions(activateIn, deactivateIn);
    }

    @Override
    public @NotNull VampirismSkill<T> setHasDefaultDescription() {
        return super.setHasDefaultDescription();
    }

    BloodlineSkill(IBloodline bloodline, boolean desc, int cost, boolean hasCost) {
        super(Either.left(bloodline.getSkillTree()), cost, desc);
        this.bloodline = bloodline;
        this.bloodlineId = bloodline.getBloodlineId();
        this.cost = cost;
        this.hasCost = hasCost;
        BloodlineSkills.addSkill(this, bloodline);
    }


    @Override
    public @NotNull Optional<IPlayableFaction<?>> getFaction() {
        return Optional.of(getBloodline().getFaction());
    }


    @Override
    public IBloodline getBloodline() {
        return this.bloodline;
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return this.bloodlineId;
    }

    @Override
    public boolean requiresBloodlineSkillPoints() {
        return this.hasCost;
    }
    @Override
    public int getSkillPointCost() {
        return cost;
    }
}
