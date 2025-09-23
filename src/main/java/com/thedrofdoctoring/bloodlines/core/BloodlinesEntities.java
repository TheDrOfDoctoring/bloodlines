package com.thedrofdoctoring.bloodlines.core;

import com.mojang.serialization.MapCodec;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.entity.entities.LingeringDevourEntity;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSubPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BloodlinesEntities {
    public static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> ENTITY_SUB_PREDICATES = DeferredRegister.create(Registries.ENTITY_SUB_PREDICATE_TYPE,Bloodlines.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Bloodlines.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<LingeringDevourEntity>> LINGERING_DEVOUR_ENTITY = prepareEntityType("lingering_devour", () -> EntityType.Builder.<LingeringDevourEntity>of(LingeringDevourEntity::new, MobCategory.MISC).sized(6.0F, 0.5F).fireImmune(), false);


    public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>,MapCodec<? extends EntitySubPredicate>> BLOODLINE_SUB_PREDICATE = ENTITY_SUB_PREDICATES.register("bloodline", () -> BloodlineSubPredicate.CODEC);

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> prepareEntityType(String id, @NotNull Supplier<EntityType.Builder<T>> builder, boolean spawnable) {
        return ENTITY_TYPES.register(id, () -> {
            EntityType.Builder<T> type = builder.get().setTrackingRange(80).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true);
            if (!spawnable) {
                type.noSummon();
            }
            return type.build(Bloodlines.MODID + ":" + id);
        });
    }
}
