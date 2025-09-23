package com.thedrofdoctoring.bloodlines.data.datamaps;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class BloodlinesDataMaps {

    public static final DataMapType<EntityType<?>, EntitySoulData> ENTITY_SOULS = DataMapType.builder(
            Bloodlines.rl("entity_souls"),
            Registries.ENTITY_TYPE,
            EntitySoulData.CODEC
    ).synced(EntitySoulData.CODEC, true).build();

}
