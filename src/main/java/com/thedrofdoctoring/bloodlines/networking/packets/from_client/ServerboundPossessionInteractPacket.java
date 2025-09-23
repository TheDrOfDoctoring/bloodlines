package com.thedrofdoctoring.bloodlines.networking.packets.from_client;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound.GraveboundPossessionAction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundPossessionInteractPacket(boolean attack) implements CustomPacketPayload {


    public static final CustomPacketPayload.Type<ServerboundPossessionInteractPacket> TYPE = new CustomPacketPayload.Type<>(Bloodlines.rl("possession_interaction"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundPossessionInteractPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ServerboundPossessionInteractPacket::attack,
            ServerboundPossessionInteractPacket::new);


    @Override
    public @NotNull Type<ServerboundPossessionInteractPacket> type() {
        return TYPE;
    }



    public static void handlePossessionInteract(ServerboundPossessionInteractPacket possessionPacket, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
            if(atts.getGraveboundData().possessedEntity != null) {
                GraveboundPossessionAction.handlePossessionInteraaction(player, possessionPacket, atts.getGraveboundData().possessedEntity);
            }
        });


    }


}
