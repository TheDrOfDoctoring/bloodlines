package com.thedrofdoctoring.bloodlines.skills.actions.vampire;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import net.minecraft.world.entity.player.Player;

public abstract class DefaultVampireBloodlineAction extends DefaultVampireAction {

    protected abstract IBloodline getBloodlineType();

    @Override
    public boolean showInSelectAction(Player player) {
        return BloodlineHelper.hasBloodline(getBloodlineType(), player);
    }
}
