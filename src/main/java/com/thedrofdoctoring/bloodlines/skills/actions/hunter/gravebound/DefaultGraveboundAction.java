package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.vampirism.api.entity.player.hunter.DefaultHunterAction;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class DefaultGraveboundAction extends DefaultHunterAction {
    @Override
    public boolean canBeUsedBy(IHunterPlayer player) {
        return !BloodlinesPlayerAttributes.get(player.asEntity()).getGraveboundData().mistForm;
    }

    @Override
    public boolean showInSelectAction(Player player) {
        return !BloodlinesPlayerAttributes.get(player).getGraveboundData().mistForm && BloodlineHelper.hasBloodline(BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), player);
    }
}
