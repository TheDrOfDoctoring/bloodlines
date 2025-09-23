package com.thedrofdoctoring.bloodlines.skills.actions.vampire.noble;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class NobleMesmeriseAction extends NobleBloodlineAction implements ILastingAction<IVampirePlayer> {

    public boolean activate(@NotNull IVampirePlayer vampire, IAction.ActivationContext context) {
        activate(vampire);
        return true;
    }
    private void activate(IVampirePlayer vamp) {
        IVampSpecialAttributes specialAttributes = (IVampSpecialAttributes) ((VampirePlayer) vamp).getSpecialAttributes();
        specialAttributes.bloodlines$setMesmerise(true);
    }


    public int getCooldown(IVampirePlayer player) {
        return CommonConfig.mesmeriseCooldown.get() * 20;
    }

    public int getDuration(@NotNull IVampirePlayer player) {
        return CommonConfig.mesmeriseDuration.get() * 20;
    }

    @Override
    public boolean canBeUsedBy(IVampirePlayer player) {
        return true;
    }

    public boolean isEnabled() {
        return CommonConfig.mesmeriseEnabled.get();
    }

    public void onActivatedClient(IVampirePlayer vampire) {
        activate(vampire);
    }

    public void onDeactivated(@NotNull IVampirePlayer vampire) {
        IVampSpecialAttributes specialAttributes = (IVampSpecialAttributes) ((VampirePlayer) vampire).getSpecialAttributes();
        specialAttributes.bloodlines$setMesmerise(false);
    }

    public void onReActivated(IVampirePlayer vampire) {
        activate(vampire);
    }

    public boolean onUpdate(IVampirePlayer vampire) {
        return false;
    }

    public boolean showHudCooldown(Player player) {
        return true;
    }

    public boolean showHudDuration(Player player) {
        return true;
    }



}
