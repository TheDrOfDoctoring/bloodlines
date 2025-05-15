package com.thedrofdoctoring.bloodlines.items.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ChaliceBlood(int blood) {
    public static final ChaliceBlood EMPTY = new ChaliceBlood(0);
    public static final Codec<ChaliceBlood> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("blood").forGetter(ChaliceBlood::blood))
            .apply(instance, ChaliceBlood::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ChaliceBlood> STREAM_CODEC;

    public int blood() {
        return this.blood;
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ChaliceBlood::blood, ChaliceBlood::new);
    }
}
