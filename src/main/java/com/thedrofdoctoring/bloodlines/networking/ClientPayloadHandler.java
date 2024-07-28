package com.thedrofdoctoring.bloodlines.networking;

import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleLeapPacket(ClientboundLeapPacket useless, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            double distanceMult = CommonConfig.ectothermDolphinLeapDistance.get();
            Vec3 vec = player.getViewVector(1);
            player.setDeltaMovement(new Vec3(vec.x * distanceMult, vec.y * distanceMult, player.getLookAngle().z * distanceMult));
        });
    }
}
