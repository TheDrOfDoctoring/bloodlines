package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EctothermDolphinLeap extends DefaultVampireAction implements ILastingAction<IVampirePlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean activate(IVampirePlayer iVampirePlayer, ActivationContext activationContext) {
        if(!iVampirePlayer.asEntity().isInWater()) {
            iVampirePlayer.asEntity().displayClientMessage(Component.translatable("skill.bloodlines.ectotherm.not_in_water"), true);
            return false;
        }
        return true;
    }

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.ectothermDolphinLeapCooldown.get() * 20;
    }

    @Override
    public int getDuration(IVampirePlayer iVampirePlayer) {
        return CommonConfig.ectothermDolphinLeapDuration.get() * 20;
    }

    @Override
    public void onActivatedClient(IVampirePlayer iVampirePlayer) {
        Player player = iVampirePlayer.asEntity();
        double distanceMult = CommonConfig.ectothermDolphinLeapDistance.get();
        Vec3 vec = player.getViewVector(1);
        player.setDeltaMovement(new Vec3(vec.x * distanceMult, vec.y * distanceMult, player.getLookAngle().z * distanceMult));
    }

    @Override
    public void onDeactivated(IVampirePlayer iVampirePlayer) {

    }

    @Override
    public void onReActivated(IVampirePlayer iVampirePlayer) {

    }

    @Override
    public boolean onUpdate(IVampirePlayer iVampirePlayer) {
        iVampirePlayer.asEntity().fallDistance = 0;
        if (!iVampirePlayer.isRemote() && iVampirePlayer.asEntity().tickCount % 50 == 0) {
            this.applyEffect(iVampirePlayer);
        }

        return false;
    }
    protected void applyEffect(IVampirePlayer vampire) {
        this.addEffectInstance(vampire, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 52, 1));
    }
}
