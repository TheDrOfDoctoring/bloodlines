package com.thedrofdoctoring.bloodlines.networking.packets.from_server;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
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

    public static void handleLeapPacket(ClientboundLeapPacket ignoredUseless, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            double distanceMult = CommonConfig.ectothermDolphinLeapDistance.get();
            Vec3 vec = player.getViewVector(1);
            player.setDeltaMovement(new Vec3(vec.x * distanceMult, vec.y * distanceMult, player.getLookAngle().z * distanceMult));
        });
    }
}
