package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public abstract class VampireBloodline implements IBloodline {

    @Override
    public IPlayableFaction<IVampirePlayer> getFaction() {
        return VReference.VAMPIRE_FACTION;
    }
    @Override
    public @Nullable ISkillHandler<IVampirePlayer> getSkillHandler(Player player) {
        return VampirePlayer.get(player).getSkillHandler();
    }

    public abstract void onBloodDrink(BloodDrinkEvent.PlayerDrinkBloodEvent event, int blRank, VampirePlayer bloodlinePlayer);

}
