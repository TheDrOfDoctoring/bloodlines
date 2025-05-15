package com.thedrofdoctoring.bloodlines.skills;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public enum BloodlineSkillType implements ISkillType {
    //Each bloodline needs its own unique skill type so that they are given the correct skills.
    NOBLE(Bloodlines.rl("noble"), "", faction -> faction == VReference.VAMPIRE_FACTION, faction -> true),
    ZEALOT(Bloodlines.rl("zealot"), "", faction -> faction == VReference.VAMPIRE_FACTION, faction -> true),
    ECTOTHERM(Bloodlines.rl("ectotherm"), "", faction -> faction == VReference.VAMPIRE_FACTION, faction -> true),
    BLOODKNIGHT(Bloodlines.rl("bloodknight"), "", faction -> faction == VReference.VAMPIRE_FACTION, faction -> true),

    GRAVEBOUND(Bloodlines.rl("gravebound"), "", faction -> faction == VReference.HUNTER_FACTION, faction -> true);

    public final ResourceLocation id;
    public final String nameSuffix;
    public final Predicate<IPlayableFaction<?>> isForFaction;
    public final Predicate<IFactionPlayerHandler> isUnlocked;

    BloodlineSkillType(ResourceLocation id, String nameSuffix, Predicate<IPlayableFaction<?>> isForFaction, Predicate<IFactionPlayerHandler> isUnlocked) {
        this.id = id;
        this.nameSuffix = nameSuffix;
        this.isForFaction = isForFaction;
        this.isUnlocked = isUnlocked;
    }

    public @NotNull ResourceLocation createIdForFaction(@NotNull ResourceLocation id) {
        return Bloodlines.rl(this.id.getPath());
    }
    @Override
    public boolean isForFaction(@NotNull IPlayableFaction<?> faction) {
        return this.isForFaction.test(faction);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return this.id;
    }

    @Override
    public boolean isUnlocked(IFactionPlayerHandler handler) {
        return this.isUnlocked.test(handler);
    }
}
