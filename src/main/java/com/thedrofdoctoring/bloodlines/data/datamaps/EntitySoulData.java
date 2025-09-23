package com.thedrofdoctoring.bloodlines.data.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record   EntitySoulData(int souls, float baseDevourHealth) {
    public static final EntitySoulData NONE = new EntitySoulData(0, 0f);


    public static final Codec<EntitySoulData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("souls").forGetter(EntitySoulData::souls),
            Codec.FLOAT.fieldOf("base_devour_health").forGetter(EntitySoulData::baseDevourHealth)
    ).apply(instance, EntitySoulData::new));
}
