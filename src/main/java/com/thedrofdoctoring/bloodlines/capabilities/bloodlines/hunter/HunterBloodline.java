package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public abstract class HunterBloodline implements IBloodline {

    @Override
    public IPlayableFaction<IHunterPlayer> getFaction() {
        return VReference.HUNTER_FACTION;
    }

    @Override
    public @Nullable ISkillHandler<IHunterPlayer> getSkillHandler(Player player) {
        return HunterPlayer.get(player).getSkillHandler();
    }

}
