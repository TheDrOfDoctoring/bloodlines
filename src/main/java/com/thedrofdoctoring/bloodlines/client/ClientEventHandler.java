package com.thedrofdoctoring.bloodlines.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.FogShape;
import com.thedrofdoctoring.bloodlines.BloodlineReference;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.ISpecialAttributes;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.mixin.ScreenAccessor;
import de.teamlapen.vampirism.client.gui.screens.VampirismContainerScreen;
import de.teamlapen.vampirism.client.gui.screens.skills.SkillsScreen;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static com.mojang.text2speech.Narrator.LOGGER;

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
            BloodlineManager.getOpt(event.getScreen().getMinecraft().player).ifPresent(bl -> {
                if(bl.getBloodline() != null) {
                    bloodlineRank = bl.getRank();
                    bloodlineName = bl.getBloodlineId().getPath();
                } else {
                    bloodlineRank = 0;
                    bloodlineName = null;
                }

            });
        }
    }

    @SubscribeEvent
    public void screenRenderEvent(ContainerScreenEvent.Render.Foreground event) {
        if(event.getContainerScreen() instanceof VampirismContainerScreen vampScreen) {
            if (bloodlineName != null) {
                bloodlineName = bloodlineName.substring(0, 1).toUpperCase() + bloodlineName.substring(1).toLowerCase();
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
            if (((ISpecialAttributes) VampirePlayer.get(mc.player).getSpecialAttributes()).bloodlines$isInWall()) {
                event.setCanceled(true);
                event.setFogShape(FogShape.SPHERE);
                event.setNearPlaneDistance(0.1f);
                event.setFarPlaneDistance(10f);
            }

            if (BloodlineManager.get(mc.player).getBloodline() == BloodlineReference.ECTOTHERM) {
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
