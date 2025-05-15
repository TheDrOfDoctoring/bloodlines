package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class ZealotWallClimbAction extends DefaultVampireAction implements ILastingAction<IVampirePlayer> {
    public boolean activate(@NotNull IVampirePlayer vampire, IAction.ActivationContext context) {
        return true;
    }


    public int getCooldown(IVampirePlayer player) {
        return CommonConfig.zealotWallClimbCooldown.get() * 20;
    }

    public int getDuration(@NotNull IVampirePlayer player) {
        return CommonConfig.zealotWallClimbDuration.get() * 20;
    }

    @Override
    public boolean canBeUsedBy(IVampirePlayer player) {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public void onActivatedClient(IVampirePlayer vampire) {
    }

    public void onDeactivated(@NotNull IVampirePlayer vampire) {

    }

    public void onReActivated(IVampirePlayer vampire) {
    }

    public boolean onUpdate(IVampirePlayer vampire) {
        Player player = vampire.asEntity();
        if (player.getCommandSenderWorld().isClientSide && (player.horizontalCollision || player.minorHorizontalCollision)) {
            float climbSpeed = player.isCrouching() ? 0.0f : CommonConfig.zealotWallClimbSpeed.get().floatValue();
            player.setDeltaMovement(player.getDeltaMovement ().x, climbSpeed, player.getDeltaMovement().z);
            player.fallDistance = 0;
        }
        return false;
    }

    public boolean showHudCooldown(Player player) {
        return true;
    }

    public boolean showHudDuration(Player player) {
        return true;
    }
}
