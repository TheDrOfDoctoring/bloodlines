package com.thedrofdoctoring.bloodlines.data;



import de.teamlapen.vampirism.api.datamaps.IEntityBlood;
import de.teamlapen.vampirism.core.ModDataMaps;
import de.teamlapen.vampirism.core.ModEntities;
import de.teamlapen.vampirism.datamaps.EntityBloodEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class BloodlinesDataMaps extends DataMapProvider {

    public BloodlinesDataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        gatherEntityBlood(builder(ModDataMaps.ENTITY_BLOOD_MAP));
    }
    protected void gatherEntityBlood(Builder<IEntityBlood, EntityType<?>> entityValues) {
        Function<EntityType<?>, Holder<EntityType<?>>> holder = BuiltInRegistries.ENTITY_TYPE::wrapAsHolder;

        entityValues.add(holder.apply(ModEntities.ADVANCED_VAMPIRE.get()), new EntityBloodEntry(30), false);
        entityValues.add(holder.apply(ModEntities.VAMPIRE.get()), new EntityBloodEntry(20), false);
        entityValues.add(holder.apply(ModEntities.ADVANCED_VAMPIRE_IMOB.get()), new EntityBloodEntry(30), false);
        entityValues.add(holder.apply(ModEntities.VAMPIRE_IMOB.get()), new EntityBloodEntry(20), false);
    }
}
