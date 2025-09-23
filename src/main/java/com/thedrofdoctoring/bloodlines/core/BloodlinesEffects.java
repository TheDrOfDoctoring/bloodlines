package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.effects.BloodFrenzyEffect;
import com.thedrofdoctoring.bloodlines.effects.ColdbloodedEffect;
import com.thedrofdoctoring.bloodlines.effects.HeinousCurseEffect;
import com.thedrofdoctoring.bloodlines.effects.SoulRendingEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BloodlinesEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Bloodlines.MODID);

    public static final DeferredHolder<MobEffect, BloodFrenzyEffect> BLOOD_FRENZY = EFFECTS.register("blood_frenzy", BloodFrenzyEffect::new);
    public static final DeferredHolder<MobEffect, HeinousCurseEffect> HEINOUS_CURSE = EFFECTS.register("heinous_curse", HeinousCurseEffect::new);
    public static final DeferredHolder<MobEffect, ColdbloodedEffect> COLD_BLOODED = EFFECTS.register("cold_blooded", ColdbloodedEffect::new);
    public static final DeferredHolder<MobEffect, SoulRendingEffect> SOUL_RENDING = EFFECTS.register("soul_rending", SoulRendingEffect::new);
}
