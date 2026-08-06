package com.thedrofdoctoring.bloodlines.client.screens;

import com.thedrofdoctoring.bloodlines.menus.BloodlinesMenus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class BloodlinesMenuScreens {

    public void registerScreens(RegisterMenuScreensEvent event) {
        event.register(BloodlinesMenus.PHYLACTERY.get(), PhylacteryScreen::new);

    }
    public static void register(IEventBus bus) {
        BloodlinesMenuScreens menuScreens = new BloodlinesMenuScreens();
        bus.addListener(menuScreens::registerScreens);
    }
}
