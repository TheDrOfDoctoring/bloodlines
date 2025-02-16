package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.effects.BloodFrenzyEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BloodlinesEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Bloodlines.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> BLOOD_FRENZY = EFFECTS.register("blood_frenzy", BloodFrenzyEffect::new);

}
