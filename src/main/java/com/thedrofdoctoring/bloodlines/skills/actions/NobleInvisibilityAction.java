package com.thedrofdoctoring.bloodlines.skills.actions;

import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.InvisibilityVampireAction;
import de.teamlapen.vampirism.entity.player.vampire.skills.VampireSkills;
import net.minecraft.world.entity.player.Player;

public class NobleInvisibilityAction extends InvisibilityVampireAction {
    @Override
    public boolean showInSelectAction(Player player) {
        return !(VampirePlayer.getOpt(player).map(vp -> vp.getSkillHandler().isSkillEnabled(VampireSkills.VAMPIRE_INVISIBILITY.get())).orElse(false));
    }

    @Override
    public boolean canBeUsedBy(IVampirePlayer player) {
        return !(player.getSkillHandler().isSkillEnabled(VampireSkills.VAMPIRE_INVISIBILITY.get()));
    }

}
