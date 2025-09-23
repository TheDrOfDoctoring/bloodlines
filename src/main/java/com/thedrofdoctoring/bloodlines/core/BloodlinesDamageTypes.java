package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BloodlinesDamageTypes {

    public static final ResourceKey<DamageType> DEVOUR_SOUL = createKey("devour_soul");


    private static ResourceKey<DamageType> createKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Bloodlines.rl(name));
    }

    public static void createDamageTypes(BootstrapContext<DamageType> context) {
        context.register(DEVOUR_SOUL, new DamageType(Bloodlines.MODID + ".devour_soul", DamageScaling.NEVER, 0.0F, DamageEffects.HURT, DeathMessageType.DEFAULT));
    }

    public static Holder.Reference<DamageType> getDamageSource(@NotNull Level world, @NotNull ResourceKey<DamageType> damageTypeResourceKey) {
        var damageOpt = world.damageSources().damageTypes.getHolder(damageTypeResourceKey);

        Holder.Reference<DamageType> damageType;
        if(damageOpt.isPresent()) {
            damageType = damageOpt.get();
        } else {
            Bloodlines.LOGGER.warn("Unable to get Damage Source: {} ", damageTypeResourceKey);
            return null;
        }
        return damageType;
    }
}
