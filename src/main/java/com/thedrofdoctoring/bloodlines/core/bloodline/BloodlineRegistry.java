package com.thedrofdoctoring.bloodlines.core.bloodline;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineZealot;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.items.BloodlineFang;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.HashSet;
import java.util.Set;

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
