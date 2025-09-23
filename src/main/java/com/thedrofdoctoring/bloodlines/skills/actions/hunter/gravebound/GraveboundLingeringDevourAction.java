package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.entity.entities.LingeringDevourEntity;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class GraveboundLingeringDevourAction extends GraveboundSoulAction{
    @Override
    public int getConsumedSouls(IHunterPlayer player) {
        return HunterBloodlinesConfig.lingeringDevourSoulCost.get();
    }
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        if(!super.activate(iHunterPlayer, activationContext)) {
            return false;
        }
        Optional<BlockPos> targetBlock = activationContext.targetBlock();
        if(targetBlock.isEmpty()) {
            return false;
        }
        BlockPos target = targetBlock.get();
        LingeringDevourEntity lingeringDevour = new LingeringDevourEntity(iHunterPlayer.asEntity().getCommandSenderWorld(), target.getX(), target.getY() + 1, target.getZ());
        lingeringDevour.setOwner(iHunterPlayer.asEntity());

        lingeringDevour.setParticle(ParticleTypes.SOUL);
        lingeringDevour.setRadius(4.5F);
        lingeringDevour.setDuration(HunterBloodlinesConfig.lingeringDevourDuration.get() * 20);
        lingeringDevour.setRadiusPerTick((8.0F - lingeringDevour.getRadius()) / (float)lingeringDevour.getDuration());
        lingeringDevour.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 1));

        iHunterPlayer.asEntity().getCommandSenderWorld().addFreshEntity(lingeringDevour);
        consumeSouls(iHunterPlayer);

        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.lingeringDevourCooldown.get() * 20;
    }
    public boolean showHudCooldown(Player player) {
        return true;
    }

}
