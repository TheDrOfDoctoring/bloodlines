package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class GraveboundSoulInfusionAction extends GraveboundSoulAction implements ILastingAction<IHunterPlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getConsumedSouls(IHunterPlayer player) {
        return HunterBloodlinesConfig.soulInfusionSoulRequirement.get();
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        if(!super.activate(iHunterPlayer, activationContext)) {
            return false;
        }
        consumeSouls(iHunterPlayer);
        applyEffect(iHunterPlayer);
        iHunterPlayer.asEntity().heal(5);

        return true;
    }

    protected void applyEffect(IHunterPlayer hunter) {
        this.addEffectInstance(hunter, new MobEffectInstance(MobEffects.REGENERATION, 25, 1, false, false));
        this.addEffectInstance(hunter, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 25, 0, false, false));

    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.soulInfusionCooldown.get() * 20;
    }

    @Override
    public int getDuration(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.soulInfusionDuration.get() * 20;
    }

    @Override
    public void onActivatedClient(IHunterPlayer iHunterPlayer) {

    }

    @Override
    public void onDeactivated(IHunterPlayer iHunterPlayer) {
        this.removePotionEffect(iHunterPlayer, MobEffects.REGENERATION);
        this.removePotionEffect(iHunterPlayer, MobEffects.DAMAGE_RESISTANCE);

    }

    @Override
    public void onReActivated(IHunterPlayer iHunterPlayer) {

    }

    public boolean onUpdate(IHunterPlayer hunter) {
        if (!hunter.isRemote() && hunter.asEntity().tickCount % 20 == 0) {
            this.applyEffect(hunter);
        }

        return false;
    }
    public boolean showHudCooldown(Player player) {
        return true;
    }
}
