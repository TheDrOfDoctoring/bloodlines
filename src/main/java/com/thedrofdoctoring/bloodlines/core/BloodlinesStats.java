package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class BloodlinesStats {

    public static final DeferredRegister<ResourceLocation> CUSTOM_STAT = DeferredRegister.create(Registries.CUSTOM_STAT, Bloodlines.MODID);


    public static final DeferredHolder<ResourceLocation, ResourceLocation> SOULS_DEVOURED = add("souls_devoured");
    public static final DeferredHolder<ResourceLocation, ResourceLocation> MOBS_SOUL_DEVOURED = add("creatures_souls_devoured");


    private static DeferredHolder<ResourceLocation, ResourceLocation> add(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CUSTOM_STAT.getNamespace(), name);
        return CUSTOM_STAT.register(name, () -> id);
    }


}
