package com.thedrofdoctoring.bloodlines.advancements;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.advancements.criterion.BloodlineCriterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BloodlinesAdvancementTriggers {

    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, Bloodlines.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, BloodlineCriterion> BLOODLINE_RANK = TRIGGERS.register("bloodline_rank", BloodlineCriterion::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> DRINK_VAMPIRE_BLOOD = TRIGGERS.register("bloodknight_blood_drink", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> CHECK_BLOODLINE = TRIGGERS.register("check_bloodline", PlayerTrigger::new);

}
