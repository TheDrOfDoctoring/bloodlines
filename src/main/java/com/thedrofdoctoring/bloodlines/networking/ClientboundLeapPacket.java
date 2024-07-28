package com.thedrofdoctoring.bloodlines.networking;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ClientboundLeapPacket implements CustomPacketPayload {

    private static ClientboundLeapPacket INSTANCE;

    private ClientboundLeapPacket() {
    }
    public static ClientboundLeapPacket getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ClientboundLeapPacket();
        }
        return INSTANCE;
    }
    public static final CustomPacketPayload.Type<ClientboundLeapPacket> TYPE = new CustomPacketPayload.Type<>(Bloodlines.rl("leap"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLeapPacket> CODEC = StreamCodec.unit(getInstance());
    @NotNull
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
