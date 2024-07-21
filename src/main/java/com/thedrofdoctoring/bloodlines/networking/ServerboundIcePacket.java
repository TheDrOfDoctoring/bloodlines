package com.thedrofdoctoring.bloodlines.networking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerboundIcePacket implements CustomPacketPayload {

    private static ServerboundIcePacket INSTANCE;

    private ServerboundIcePacket() {
    }
    public static ServerboundIcePacket getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ServerboundIcePacket();
        }
        return INSTANCE;
    }
    public static final CustomPacketPayload.Type<ServerboundIcePacket> TYPE = new CustomPacketPayload.Type<>(Bloodlines.rl("ice"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundIcePacket> CODEC = StreamCodec.unit(getInstance());
    @NotNull
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


}
