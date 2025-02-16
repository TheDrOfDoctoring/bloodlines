package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.particle.GenericParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

//i would never use this idea in any other mod
public class NobleCelerityAction extends DefaultVampireAction implements ILastingAction<IVampirePlayer> {

    public static final ResourceLocation CELERITY_SPEED_LOCATION = Bloodlines.rl("noble_celerity_speed");
    public static final ResourceLocation CELERITY_STEP_LOCATION = Bloodlines.rl("noble_celerity_step");
    public boolean activate(@NotNull IVampirePlayer vampire, IAction.ActivationContext context) {
        Player player = vampire.asEntity();
        BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), CELERITY_SPEED_LOCATION);
        BloodlineManager.removeModifier(player.getAttribute(Attributes.STEP_HEIGHT), CELERITY_STEP_LOCATION);
        int rank = BloodlineHelper.getBloodlineRank(player);
        player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(CELERITY_SPEED_LOCATION, CommonConfig.nobleCeleritySpeedMultipliers.get().get(rank - 1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        if(rank >= CommonConfig.celerityStepAssistRank.get()) {
            player.getAttribute(Attributes.STEP_HEIGHT).addPermanentModifier(new AttributeModifier(CELERITY_STEP_LOCATION, 0.5, AttributeModifier.Operation.ADD_VALUE));
        }
        return true;
    }


    public int getCooldown(IVampirePlayer player) {
        return CommonConfig.celerityCooldown.get() * 20;
    }

    public int getDuration(@NotNull IVampirePlayer player) {
        return CommonConfig.celerityDuration.get() * 20;
    }

    @Override
    public boolean canBeUsedBy(IVampirePlayer player) {
        return true;
    }

    public boolean isEnabled() {
        return CommonConfig.celerityEnabled.get();
    }

    public void onActivatedClient(IVampirePlayer vampire) {
    }

    public void onDeactivated(@NotNull IVampirePlayer vampire) {
        BloodlineManager.removeModifier(vampire.asEntity().getAttribute(Attributes.MOVEMENT_SPEED), CELERITY_SPEED_LOCATION);
        BloodlineManager.removeModifier(vampire.asEntity().getAttribute(Attributes.STEP_HEIGHT), CELERITY_STEP_LOCATION);
    }

    public void onReActivated(IVampirePlayer vampire) {
    }

    public boolean onUpdate(IVampirePlayer vampire) {
        if(!vampire.asEntity().getCommandSenderWorld().isClientSide) {
            Player player = vampire.asEntity();
            ModParticles.spawnParticlesServer(player.getCommandSenderWorld(), new GenericParticleOptions(ResourceLocation.fromNamespaceAndPath("minecraft", "generic_4"), 5, 0xd3d3d3, 0.1F) , player.getX(), player.getY(), player.getZ(), 3, 0,0, 0, 0);
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
