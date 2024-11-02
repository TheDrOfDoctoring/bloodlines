package com.thedrofdoctoring.bloodlines.core.bloodline;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineZealot;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class BloodlineRegistry {

    public static final ResourceKey<Registry<IBloodline>> BLOODLINE_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bloodlines.MODID, "bloodlines"));
    public static final Registry<IBloodline> BLOODLINE_REGISTRY = new RegistryBuilder<>(BLOODLINE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(Bloodlines.MODID, "empty"))
            .create();


    public static final DeferredRegister<IBloodline> BLOODLINES = DeferredRegister.create(BLOODLINE_REGISTRY, Bloodlines.MODID);

    public static final DeferredHolder<IBloodline, BloodlineNoble> BLOODLINE_NOBLE = BLOODLINES.register("noble", BloodlineNoble::new);
    public static final DeferredHolder<IBloodline, BloodlineFrost> BLOODLINE_ECTOTHERM = BLOODLINES.register("ectotherm", BloodlineFrost::new);
    public static final DeferredHolder<IBloodline, BloodlineZealot> BLOODLINE_ZEALOT = BLOODLINES.register("zealot", BloodlineZealot::new);


}
