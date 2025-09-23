package com.thedrofdoctoring.bloodlines.networking;

import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundDevourSoulPacket;
import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundIcePacket;
import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundPossessionInputPacket;
import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundPossessionInteractPacket;
import com.thedrofdoctoring.bloodlines.networking.packets.from_server.ClientboundLeapPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ServerPayloadHandler {

    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0");
        registrar.playToServer(
                ServerboundIcePacket.TYPE,
                ServerboundIcePacket.CODEC,
                ServerboundIcePacket::handleIcePacket
        );
        registrar.playToServer(
                ServerboundDevourSoulPacket.TYPE,
                ServerboundDevourSoulPacket.CODEC,
                ServerboundDevourSoulPacket::handleDevourSoulPacket
        );
        registrar.playToServer(
                ServerboundPossessionInputPacket.TYPE,
                ServerboundPossessionInputPacket.CODEC,
                ServerboundPossessionInputPacket::handlePossessionPacket
        );
        registrar.playToServer(
                ServerboundPossessionInteractPacket.TYPE,
                ServerboundPossessionInteractPacket.CODEC,
                ServerboundPossessionInteractPacket::handlePossessionInteract
        );

        registrar.playToClient(
                ClientboundLeapPacket.TYPE,
                ClientboundLeapPacket.CODEC,
                ClientboundLeapPacket::handleLeapPacket
        );


    }


}
