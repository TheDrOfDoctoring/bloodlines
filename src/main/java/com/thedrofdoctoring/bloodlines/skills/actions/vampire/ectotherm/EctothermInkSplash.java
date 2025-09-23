package com.thedrofdoctoring.bloodlines.skills.actions.vampire.ectotherm;

import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.particle.GenericParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EctothermInkSplash extends EctothermBloodlineAction {
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

        Player player = iVampirePlayer.asEntity();
        AABB aabb = new AABB(player.getOnPos());
        aabb = aabb.inflate(3, 3, 3);
        ModParticles.spawnParticlesServer(player.level(), new GenericParticleOptions(ResourceLocation.fromNamespaceAndPath("minecraft","generic_3"), 50, 0x27221f), player.getX(), player.getY(), player.getZ(), 50, 0,0 ,0, 0);
        ModParticles.spawnParticlesServer(player.level(), new GenericParticleOptions(ResourceLocation.fromNamespaceAndPath("minecraft","generic_5"), 50, 0x27221f), player.getX(), player.getY(), player.getZ(), 50, 0,0 ,0, 0);

        List<Entity> entities = player.level().getEntities(player, aabb);
        for(Entity entity : entities) {
            if(entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, CommonConfig.ectothermInkSplashBlindnessLength.get()));
            }
        }
        return true;
    }

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.ectothermInkSplashCooldown.get() * 20;
    }

    public boolean showHudCooldown(Player player) {
        return true;
    }

}
