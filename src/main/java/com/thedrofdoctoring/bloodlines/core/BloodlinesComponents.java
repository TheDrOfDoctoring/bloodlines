package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.items.attachments.ChaliceBlood;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BloodlinesComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Bloodlines.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ChaliceBlood>> CHALICE_BLOOD = DATA_COMPONENTS.registerComponentType("bloodlines_chalice", (builder) -> builder.persistent(ChaliceBlood.CODEC).networkSynchronized(ChaliceBlood.STREAM_CODEC));
}
