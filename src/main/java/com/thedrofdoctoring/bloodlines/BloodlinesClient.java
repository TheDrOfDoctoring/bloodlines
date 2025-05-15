package com.thedrofdoctoring.bloodlines;

import com.thedrofdoctoring.bloodlines.client.BloodlineEntityRenderManager;
import com.thedrofdoctoring.bloodlines.client.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@Mod(value = Bloodlines.MODID, dist = Dist.CLIENT)
public class BloodlinesClient {

    private static BloodlinesClient INSTANCE;

    private final BloodlineEntityRenderManager renderManager;

    public BloodlinesClient(ModContainer container) {
        INSTANCE = this;
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        NeoForge.EVENT_BUS.register(new ClientEventHandler(Minecraft.getInstance()));

        renderManager = new BloodlineEntityRenderManager(Minecraft.getInstance());

        NeoForge.EVENT_BUS.addListener(this::onAddReloadListenerEvent);

    }

    public static BloodlinesClient getInstance() {
        return INSTANCE;
    }
    public void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(this.renderManager);
    }
    public BloodlineEntityRenderManager getRenderManager() {
        return renderManager;
    }

}
