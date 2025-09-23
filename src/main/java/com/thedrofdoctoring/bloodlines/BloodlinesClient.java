package com.thedrofdoctoring.bloodlines;

import com.thedrofdoctoring.bloodlines.client.BloodlineEntityRenderManager;
import com.thedrofdoctoring.bloodlines.client.ClientEventHandler;
import com.thedrofdoctoring.bloodlines.client.core.BloodlinesItemClient;
import com.thedrofdoctoring.bloodlines.client.core.BloodlinesKeys;
import com.thedrofdoctoring.bloodlines.client.core.BloodlinesOverlays;
import com.thedrofdoctoring.bloodlines.client.gui.overlay.SoulbarOverlay;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@Mod(value = Bloodlines.MODID, dist = Dist.CLIENT)
public class BloodlinesClient {

    private static BloodlinesClient INSTANCE;

    private final BloodlineEntityRenderManager renderManager;

    public BloodlinesClient(IEventBus modBus, ModContainer container) {
        INSTANCE = this;
        Bloodlines.onServer = false;

        this.setupOverlays();

        modBus.addListener(BloodlinesOverlays::registerScreenOverlays);
        modBus.addListener(BloodlinesKeys::registerKeyMapping);

        modBus.addListener(this::setupClient);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        NeoForge.EVENT_BUS.register(new ClientEventHandler(Minecraft.getInstance()));
        NeoForge.EVENT_BUS.register(new BloodlinesKeys());


        renderManager = new BloodlineEntityRenderManager(Minecraft.getInstance());

        NeoForge.EVENT_BUS.addListener(this::onAddReloadListenerEvent);

    }
    private void setupOverlays() {
        BloodlinesOverlays.SOUL_BAR = new SoulbarOverlay();
    }


    private void setupClient(final FMLClientSetupEvent event) {
        event.enqueueWork(BloodlinesItemClient::registerItemModelProperties);

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
