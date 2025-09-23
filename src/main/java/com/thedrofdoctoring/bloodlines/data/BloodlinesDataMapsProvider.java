package com.thedrofdoctoring.bloodlines.data;


import com.thedrofdoctoring.bloodlines.data.datamaps.BloodlinesDataMaps;
import com.thedrofdoctoring.bloodlines.data.datamaps.EntitySoulData;
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

public class BloodlinesDataMapsProvider extends DataMapProvider {

    public BloodlinesDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        gatherEntityBlood(builder(ModDataMaps.ENTITY_BLOOD_MAP));
        gatherEntitySouls(builder(BloodlinesDataMaps.ENTITY_SOULS));
    }
    protected void gatherEntityBlood(Builder<IEntityBlood, EntityType<?>> entityBloodValues) {
        Function<EntityType<?>, Holder<EntityType<?>>> holder = BuiltInRegistries.ENTITY_TYPE::wrapAsHolder;

        entityBloodValues.add(holder.apply(ModEntities.ADVANCED_VAMPIRE.get()), new EntityBloodEntry(30), false);
        entityBloodValues.add(holder.apply(ModEntities.VAMPIRE.get()), new EntityBloodEntry(20), false);
        entityBloodValues.add(holder.apply(ModEntities.ADVANCED_VAMPIRE_IMOB.get()), new EntityBloodEntry(30), false);
        entityBloodValues.add(holder.apply(ModEntities.VAMPIRE_IMOB.get()), new EntityBloodEntry(20), false);
    }
    protected void gatherEntitySouls(Builder<EntitySoulData, EntityType<?>> entitySoulValues) {
        Function<EntityType<?>, Holder<EntityType<?>>> holder = BuiltInRegistries.ENTITY_TYPE::wrapAsHolder;

        entitySoulValues.add(holder.apply(ModEntities.ADVANCED_VAMPIRE.get()), new EntitySoulData(4, 0.2f), false);
        entitySoulValues.add(holder.apply(ModEntities.VAMPIRE.get()), new EntitySoulData(3, 0.2f), false);
        entitySoulValues.add(holder.apply(ModEntities.ADVANCED_VAMPIRE_IMOB.get()), new EntitySoulData(4, 0.2f), false);
        entitySoulValues.add(holder.apply(ModEntities.VAMPIRE_IMOB.get()), new EntitySoulData(3, 0.2f), false);

        entitySoulValues.add(holder.apply(ModEntities.ADVANCED_HUNTER.get()), new EntitySoulData(6, 0.2f), false);
        entitySoulValues.add(holder.apply(ModEntities.HUNTER.get()), new EntitySoulData(4, 0.2f), false);
        entitySoulValues.add(holder.apply(ModEntities.ADVANCED_HUNTER_IMOB.get()), new EntitySoulData(6, 0.2f), false);
        entitySoulValues.add(holder.apply(ModEntities.HUNTER_IMOB.get()), new EntitySoulData(4, 0.2f), false);

        entitySoulValues.add(holder.apply(ModEntities.VILLAGER_ANGRY.get()), new EntitySoulData(6, 0.5f), false);
        entitySoulValues.add(holder.apply(ModEntities.VILLAGER_CONVERTED.get()), new EntitySoulData(6, 0.75f), false);
        entitySoulValues.add(holder.apply(EntityType.VILLAGER), new EntitySoulData(6, 1f), false);
        entitySoulValues.add(holder.apply(EntityType.WANDERING_TRADER), new EntitySoulData(6, 1f), false);
        entitySoulValues.add(holder.apply(EntityType.VINDICATOR), new EntitySoulData(5, 0.35f), false);
        entitySoulValues.add(holder.apply(EntityType.ILLUSIONER), new EntitySoulData(5, 0.35f), false);


        entitySoulValues.add(holder.apply(EntityType.PLAYER), new EntitySoulData(8, 0.1f), false);

        entitySoulValues.add(holder.apply(EntityType.PIGLIN), new EntitySoulData(3, 0.5f), false);


        entitySoulValues.add(holder.apply(ModEntities.VAMPIRE_BARON.get()), new EntitySoulData(8, 0.1f), false);



    }

}
