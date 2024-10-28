package com.thedrofdoctoring.bloodlines.data.spawn_modifiers;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.data.BloodlinesData;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public class BloodlineRankDistributions {

    public static final ResourceKey<BloodlineRankDistribution> BLOODLINE_NOBLE_RANK_DISTRIBUTION = ResourceKey.create(
            BloodlinesData.BLOODLINE_RANK_DISTRIBUTION,
            BloodlineRegistry.BLOODLINE_NOBLE.get().getBloodlineId()
    );
    public static final ResourceKey<BloodlineRankDistribution> BLOODLINE_ECTOTHERM_RANK_DISTRIBUTION = ResourceKey.create(
            BloodlinesData.BLOODLINE_RANK_DISTRIBUTION,
            BloodlineRegistry.BLOODLINE_ECTOTHERM.get().getBloodlineId()
    );
    public static final ResourceKey<BloodlineRankDistribution> BLOODLINE_ZEALOT_RANK_DISTRIBUTION = ResourceKey.create(
            BloodlinesData.BLOODLINE_RANK_DISTRIBUTION,
            BloodlineRegistry.BLOODLINE_ZEALOT.get().getBloodlineId()
    );

    public static void createRankDistributions(BootstrapContext<BloodlineRankDistribution> context) {
        context.register(BLOODLINE_NOBLE_RANK_DISTRIBUTION,
                new BloodlineRankDistribution(
                        List.of(0.6f, 0.3f, 0.2f, 0.075f, 0.025f)
                )
        );
        context.register(BLOODLINE_ECTOTHERM_RANK_DISTRIBUTION,
                new BloodlineRankDistribution(
                        List.of(0.3f, 0.3f, 0.2f, 0.15f, 0.05f)
                )
        );
        context.register(BLOODLINE_ZEALOT_RANK_DISTRIBUTION,
                new BloodlineRankDistribution(
                        List.of(0.2f, 0.3f, 0.3f, 0.1f, 0.1f)
                )
        );
    }
}
