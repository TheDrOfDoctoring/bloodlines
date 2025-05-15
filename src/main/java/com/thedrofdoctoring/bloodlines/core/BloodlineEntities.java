package com.thedrofdoctoring.bloodlines.core;

import com.mojang.serialization.MapCodec;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSubPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
@SuppressWarnings("unused")
public class BloodlineEntities {
    public static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> ENTITY_SUB_PREDICATES = DeferredRegister.create(Registries.ENTITY_SUB_PREDICATE_TYPE,Bloodlines.MODID);

    public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>,MapCodec<? extends EntitySubPredicate>> BLOODLINE_SUB_PREDICATE = ENTITY_SUB_PREDICATES.register("bloodline", () -> BloodlineSubPredicate.CODEC);

}
