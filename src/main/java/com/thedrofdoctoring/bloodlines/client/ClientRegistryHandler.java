package com.thedrofdoctoring.bloodlines.client;


import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.NeoForge;

public class ClientRegistryHandler {
    public static void init(){
        Minecraft mc = Minecraft.getInstance();
        if(mc != null) {
            ClientEventHandler handler = new ClientEventHandler(mc);
            NeoForge.EVENT_BUS.register(handler);
        }

    }
}
