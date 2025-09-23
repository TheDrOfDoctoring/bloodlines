package com.thedrofdoctoring.bloodlines.client.gui.overlay;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SoulbarOverlay implements LayeredDraw.Layer {

    public static final ResourceLocation BACKGROUND = Bloodlines.rl("soul_bar/background");
    public static final ResourceLocation HALF = Bloodlines.rl("soul_bar/soul_half");
    public static final ResourceLocation FULL = Bloodlines.rl("soul_bar/soul_full");


    private final Minecraft mc = Minecraft.getInstance();

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker delta) {
        if(this.mc.options.hideGui) return;
        if(mc.player != null && mc.gameMode != null && BloodlineHelper.hasBloodline(BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), mc.player)) {
            if (this.mc.gameMode.hasExperience() && this.mc.player.isAlive()) {
                BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(mc.player);


                if(atts.bloodline != BloodlineRegistry.BLOODLINE_GRAVEBOUND.get()) return;
                if(atts.getGraveboundData().possessionActive) return;

                int souls = atts.getGraveboundData().souls;
                int max = atts.getGraveboundData().maxSouls;
                int left = this.mc.getWindow().getGuiScaledWidth() / 2 + 91;
                int top = this.mc.getWindow().getGuiScaledHeight() - this.mc.gui.rightHeight;
                this.mc.gui.rightHeight += 10;
                for (int i = 0; i < 10; i++) {
                    int idx = i * 2 + 1;
                    int x = left - i * 8 - 9;
                    if (idx <= max) {
                        graphics.blitSprite(BACKGROUND, x, top, 9,9);
                    }

                    if (idx < souls) {
                        graphics.blitSprite(FULL, x, top, 9 ,9);
                    } else if(idx == souls){
                        graphics.blitSprite(HALF, x, top, 9, 9);
                    }
                }
            }
        }
    }
}
