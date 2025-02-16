package com.thedrofdoctoring.bloodlines.data;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineBloodknight;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineZealot;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public class BloodlineAISelectors {

    public static final ResourceKey<BloodlineSelector> BLOODLINE_BLOODKNIGHT_TARGET_BLOODLINES = ResourceKey.create(
            BloodlinesData.BLOODLINE_AI_SELECTORS,
            BloodlineBloodknight.BLOOD_KNIGHT
    );
    public static final ResourceKey<BloodlineSelector> BLOODLINE_ECTOTHERM_TARGET_BLOODLINES = ResourceKey.create(
            BloodlinesData.BLOODLINE_AI_SELECTORS,
            BloodlineFrost.ECTOTHERM
    );
    public static final ResourceKey<BloodlineSelector> BLOODLINE_NOBLE_TARGET_BLOODLINES = ResourceKey.create(
            BloodlinesData.BLOODLINE_AI_SELECTORS,
            BloodlineNoble.NOBLE
    );
    public static final ResourceKey<BloodlineSelector> BLOODLINE_ZEALOT_TARGET_BLOODLINES = ResourceKey.create(
            BloodlinesData.BLOODLINE_AI_SELECTORS,
            BloodlineZealot.ZEALOT
    );

    public static void createAISelectors(BootstrapContext<BloodlineSelector> context) {
        context.register(
                BLOODLINE_BLOODKNIGHT_TARGET_BLOODLINES,
                new BloodlineSelector(
                        true,
                        List.of(
                                BloodlineRegistry.BLOODLINE_NOBLE.get(),
                                BloodlineRegistry.BLOODLINE_ECTOTHERM.get(),
                                BloodlineRegistry.BLOODLINE_ZEALOT.get()
                        )
                )
        );
        context.register(
                BLOODLINE_ZEALOT_TARGET_BLOODLINES,
                new BloodlineSelector(
                        false,
                        true,
                        List.of(
                                BloodlineRegistry.BLOODLINE_BLOODKNIGHT.get()
                        )

                )
        );
        context.register(
                BLOODLINE_ECTOTHERM_TARGET_BLOODLINES,
                new BloodlineSelector(
                        false,
                        true,
                        List.of(
                                BloodlineRegistry.BLOODLINE_BLOODKNIGHT.get()
                        )

                )
        );
        context.register(
                BLOODLINE_NOBLE_TARGET_BLOODLINES,
                new BloodlineSelector(
                        false,
                        true,
                        List.of(
                                BloodlineRegistry.BLOODLINE_BLOODKNIGHT.get()
                        )

                )
        );
    }
}
