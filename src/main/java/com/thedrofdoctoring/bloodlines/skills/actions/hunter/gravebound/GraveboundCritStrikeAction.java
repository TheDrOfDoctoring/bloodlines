package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.particle.GenericParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class GraveboundCritStrikeAction extends DefaultGraveboundAction implements ILastingAction<IHunterPlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().critStrikeActive = true;
        return true;
    }



    @Override
    public void onActivatedClient(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().critStrikeActive = true;

    }

    @Override
    public void onDeactivated(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().critStrikeActive = false;

    }

    @Override
    public void onReActivated(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().critStrikeActive = true;
    }

    @Override
    public int getDuration(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.sorcerousStrikeDuration.get() * 20;
    }

    @Override
    public boolean onUpdate(IHunterPlayer iHunterPlayer) {
        if(iHunterPlayer.asEntity().tickCount % 10 == 0) {
            return BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().critStrikeActive;
        }
        if(!iHunterPlayer.asEntity().getCommandSenderWorld().isClientSide) {
            Player player = iHunterPlayer.asEntity();
            ModParticles.spawnParticlesServer(player.getCommandSenderWorld(), new GenericParticleOptions(ResourceLocation.fromNamespaceAndPath("minecraft", "generic_4"), 5, 0x800080, 0.1F) , player.getX(), player.getY(), player.getZ(), 3, 0,0, 0, 0);
        }
        return false;
    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.sorcerousStrikeCooldown.get() * 20;
    }
    public boolean showHudCooldown(Player player) {
        return true;
    }
}
