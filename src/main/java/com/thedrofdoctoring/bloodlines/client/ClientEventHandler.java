package com.thedrofdoctoring.bloodlines.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.mixin.ScreenAccessor;
import de.teamlapen.vampirism.client.gui.screens.VampirismContainerScreen;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.NotNull;

public class ClientEventHandler {

    private int bloodlineRank;
    private String bloodlineName;
    private final Minecraft mc;
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath("vampirism", "skills_screen/node");

    public ClientEventHandler(@NotNull Minecraft mc) {
        this.mc = mc;
    }
    @SubscribeEvent
    public void screenInitEvent(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof VampirismContainerScreen) {
            if(mc.player == null) return;
            BloodlineManager bl = BloodlineManager.get(mc.player);
            if(bl.getBloodline() != null) {
                bloodlineRank = bl.getRank();
                bloodlineName = bl.getBloodline().getName();
            } else {
                bloodlineRank = 0;
                bloodlineName = null;
            }
        }
    }

    @SubscribeEvent
    public void screenRenderEvent(ContainerScreenEvent.Render.Foreground event) {
        if(event.getContainerScreen() instanceof VampirismContainerScreen vampScreen) {
            if (bloodlineName != null) {
                String blRank = "(" + bloodlineRank + ")";
                GuiGraphics graphics = event.getGuiGraphics();
                Font font = ((ScreenAccessor) vampScreen).getFont();
                float x = 53;
                float y = -8;
                graphics.blitSprite(BACKGROUND,  50, -11, 140, 13);
                graphics.drawString(font, bloodlineName, x , y, 0x800000, false);
                graphics.drawString(font, blRank, 50 + 123, y, 0x800000, false);

            }
        }
    }
    @SubscribeEvent
    public void gameRenderEvent(ViewportEvent.RenderFog event) {
        if (mc.player != null) {
            boolean inWall = ((IVampSpecialAttributes) VampirePlayer.get(mc.player).getSpecialAttributes()).bloodlines$isInWall();
            if (inWall) {
                event.setCanceled(true);
                event.setFogShape(FogShape.SPHERE);
                event.setNearPlaneDistance(0.1f);
                event.setFarPlaneDistance(10f);
            }

            if (BloodlineManager.get(mc.player).getBloodline() == BloodlineRegistry.BLOODLINE_ECTOTHERM.get() && !inWall) {
                int rank = BloodlineHelper.getBloodlineRank(mc.player) - 1;
                int viewDist = CommonConfig.ectothermUnderwaterVisionDistance.get().get(rank);
                if(viewDist > 0) {
                    event.setCanceled(true);
                    event.setFarPlaneDistance(viewDist);
                }
            }
        }



    }

}
