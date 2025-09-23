package com.thedrofdoctoring.bloodlines.client.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

public class BloodlinesOverlays {

    public static LayeredDraw.Layer SOUL_BAR;
    public static final ResourceLocation SOUL_BAR_ID = Bloodlines.rl("soul_bar");

    public static void registerScreenOverlays(@NotNull RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, SOUL_BAR_ID, SOUL_BAR);
    }
}
