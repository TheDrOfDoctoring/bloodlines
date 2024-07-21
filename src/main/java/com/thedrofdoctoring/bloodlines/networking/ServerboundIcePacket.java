package com.thedrofdoctoring.bloodlines.networking;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

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
