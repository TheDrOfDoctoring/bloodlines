package com.thedrofdoctoring.bloodlines.advancements.criterion;

import com.thedrofdoctoring.bloodlines.advancements.BloodlinesAdvancementTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.PlayerTrigger;

import java.util.Optional;

public class GenericCriterion {

    public static Criterion<PlayerTrigger.TriggerInstance> drinkVampireBlood() {
        return BloodlinesAdvancementTriggers.DRINK_VAMPIRE_BLOOD.get()
                .createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static Criterion<PlayerTrigger.TriggerInstance> checkBloodline() {
        return BloodlinesAdvancementTriggers.CHECK_BLOODLINE.get()
                .createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }
}
