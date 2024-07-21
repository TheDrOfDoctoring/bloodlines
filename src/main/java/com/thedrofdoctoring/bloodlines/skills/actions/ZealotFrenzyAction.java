package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class ZealotFrenzyAction extends DefaultVampireAction implements ILastingAction<IVampirePlayer> {

    private static final ResourceLocation FRENZY_MINING_LOCATION = Bloodlines.rl("zealot_frenzy_mining_speed");

    public boolean activate(@NotNull IVampirePlayer vampire, IAction.ActivationContext context) {
        Player player = vampire.asEntity();
        BloodlineManager.removeModifier(player.getAttribute(Attributes.BLOCK_BREAK_SPEED), FRENZY_MINING_LOCATION);
        int rank = BloodlineHelper.getBloodlineRank(player);
        player.getAttribute(Attributes.BLOCK_BREAK_SPEED).addPermanentModifier(new AttributeModifier(FRENZY_MINING_LOCATION, CommonConfig.zealotMiningSpeedMultipliers.get().get(rank - 1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return true;
    }


    public int getCooldown(IVampirePlayer player) {
        return CommonConfig.zealotFrenzyCooldown.get() * 20;
    }

    public int getDuration(@NotNull IVampirePlayer player) {
        return CommonConfig.zealotFrenzyDuration.get() * 20;
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
        BloodlineManager.removeModifier(vampire.asEntity().getAttribute(Attributes.BLOCK_BREAK_SPEED), FRENZY_MINING_LOCATION);
    }

    public void onReActivated(IVampirePlayer vampire) {
        Player player = vampire.asEntity();
        BloodlineManager.removeModifier(player.getAttribute(Attributes.BLOCK_BREAK_SPEED), FRENZY_MINING_LOCATION);
        int rank = BloodlineHelper.getBloodlineRank(player);
        player.getAttribute(Attributes.BLOCK_BREAK_SPEED).addPermanentModifier(new AttributeModifier(FRENZY_MINING_LOCATION, CommonConfig.zealotMiningSpeedMultipliers.get().get(rank - 1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
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