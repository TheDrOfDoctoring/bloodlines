package com.thedrofdoctoring.bloodlines.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.capabilities.other.IPossessedEntity;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesAttachments;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.mixin.ScreenAccessor;
import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundPossessionInputPacket;
import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundPossessionInteractPacket;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import de.teamlapen.vampirism.api.client.VIngameOverlays;
import de.teamlapen.vampirism.client.gui.screens.VampirismContainerScreen;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import org.jetbrains.annotations.NotNull;

public class ClientEventHandler {

    private int bloodlineRank;
    private String bloodlineName;
    private final Minecraft mc;
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath("vampirism", "skills_screen/node");
    public static final ResourceLocation DEVOUR_SOUL_SPRITE = Bloodlines.rl("devour/devour_soul");



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
    public void onRenderFoodBar(RenderGuiLayerEvent.@NotNull Pre event) {
        if (mc.player == null || !mc.player.isAlive() || !BloodlineHelper.hasBloodline(BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), mc.player)) return;
        if (mc.gameMode != null && event.getName() == VanillaGuiLayers.FOOD_LEVEL && mc.gameMode.hasExperience()) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onRenderAirSupply(RenderGuiLayerEvent.@NotNull Pre event) {
        if(mc.player == null || !mc.player.isAlive() || !BloodlineHelper.hasBloodline(BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), mc.player)      ) return;

        if(event.getName().equals(VanillaGuiLayers.AIR_LEVEL)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderGUI(RenderGuiLayerEvent.Pre event) {
        if(mc.player != null) {
            ResourceLocation guiLayer = event.getName();
            if(guiLayer.equals(VanillaGuiLayers.EXPERIENCE_BAR) || guiLayer.equals(VanillaGuiLayers.EXPERIENCE_LEVEL) || guiLayer.equals(VIngameOverlays.FACTION_LEVEL_ID)) {
                if(BloodlinesPlayerAttributes.get(mc.player).getGraveboundData().possessionActive) {
                    event.setCanceled(true);
                }
            }


        }
    }

    @SubscribeEvent
    public void onRenderCrosshair(RenderGuiLayerEvent.@NotNull Pre event) {
        if (event.getName() != VanillaGuiLayers.CROSSHAIR || mc.player == null || !mc.player.isAlive()) {
            return;
        }
        IBloodline bloodline = BloodlinesPlayerAttributes.get(mc.player).bloodline;
        if(bloodline != BloodlineRegistry.BLOODLINE_GRAVEBOUND.get()) return;
        BloodlineGravebound gravebound = (BloodlineGravebound) bloodline;

        HitResult p = Minecraft.getInstance().hitResult;
        if(p != null && p.getType() == HitResult.Type.ENTITY) {
            EntityHitResult hitResult = (EntityHitResult) p;
            Entity entity = hitResult.getEntity();
            if(entity.isAlive() && entity instanceof LivingEntity living && gravebound.canDevour(living, mc.player)) {
                renderDevourIcon(event.getGuiGraphics(), this.mc.getWindow().getGuiScaledWidth(), this.mc.getWindow().getGuiScaledHeight());
                event.setCanceled(true);
            }
        }

    }


    private void renderDevourIcon(@NotNull GuiGraphics graphics, int width, int height) {

        if(mc.player == null) return;
        int left = width / 2 - 8;
        int top = height / 2 - 4;
        RenderSystem.enableBlend();
        float alpha = HunterPlayer.get(mc.player).getActionHandler().isActionOnCooldown(BloodlineActions.GRAVEBOUND_DEVOUR_SOUL.get()) ? 0.35f : 1;
        graphics.setColor(1f, 1f, 1f, alpha);
        graphics.blitSprite(DEVOUR_SOUL_SPRITE, 16, 16, 0,0, left, top, 16, 16);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.disableBlend();

    }
    @SubscribeEvent
    public void gameRenderEvent(ViewportEvent.RenderFog event) {
        if (mc.player != null) {
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(mc.player);
            boolean inWall = atts.inWall;
            if (inWall) {
                event.setCanceled(true);
                event.setFogShape(FogShape.SPHERE);
                event.setNearPlaneDistance(0.1f);
                event.setFarPlaneDistance(10f);

            }

            if (!inWall && atts.bloodline == BloodlineRegistry.BLOODLINE_ECTOTHERM.get()) {
                int rank = atts.bloodlineRank - 1;
                int viewDist = CommonConfig.ectothermUnderwaterVisionDistance.get().get(rank);
                if(viewDist > 0) {
                    event.setCanceled(true);
                    event.setFarPlaneDistance(viewDist);
                }
            }
        }

    }

    @SubscribeEvent
    public void onRenderHand(@NotNull RenderHandEvent event) {
        if (mc.player != null && mc.player.isAlive()) {
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(mc.player);
            if(atts.getGraveboundData().possessionActive || atts.getGraveboundData().mistForm) {
                event.setCanceled(true);
            }

        }
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRenderPlayerPreHigh(RenderPlayerEvent.@NotNull Pre event) {
        Player player = event.getEntity();
        float partialTicks = event.getPartialTick();
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);


        if(atts.getGraveboundData().mistForm) {
            event.setCanceled(true);
            ShulkerBullet mistFormCore = player.getData(BloodlinesAttachments.MIST_FORM);


            // Copy values
            mistFormCore.tickCount = player.tickCount;
            mistFormCore.setXRot(player.getXRot());
            mistFormCore.setYRot(player.getYRot());
            mistFormCore.yRotO = player.yRotO;
            mistFormCore.xRotO = player.xRotO;
            mistFormCore.setInvisible(player.isInvisible());


            float f = Mth.lerp(partialTicks, mistFormCore.yRotO, mistFormCore.getYRot());
            EntityRenderer<? super ShulkerBullet> renderer = mc.getEntityRenderDispatcher().getRenderer(mistFormCore);

            renderer.render(mistFormCore, f, partialTicks, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());

            ModParticles.spawnParticleClient(player.getCommandSenderWorld(), ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getX(), player.getY(), player.getZ(), 0d, 0d, 0d);
        }

    }


    @SubscribeEvent
    public void onHeartType(PlayerHeartTypeEvent event) {
        if(event.getType() == Gui.HeartType.POISIONED && BloodlinesPlayerAttributes.get(event.getEntity()).getGraveboundData().poisonImmunity) {
            event.setType(Gui.HeartType.NORMAL);
        }
    }
    @SubscribeEvent
    public void onPlayerInteract(InputEvent.InteractionKeyMappingTriggered event) {

        if(mc.player == null) return;

        if(BloodlinesPlayerAttributes.get(mc.player).getGraveboundData().possessionActive) {
            // We only bother sending a packet if it's either an attack or an interaction, so we know if the received packet isn't an attack, it's an interact.
            if(event.isAttack() || event.isUseItem()) {
                mc.player.connection.send(new ServerboundPossessionInteractPacket(event.isAttack()));
                event.setCanceled(true);
            }

        }

    }

    @SubscribeEvent
    public void onPlayerInput(MovementInputUpdateEvent event) {
        Input input = event.getInput();

        if(mc.player == null) return;

        if(event.getEntity() instanceof IPossessedEntity possessed) {
            if(possessed.bloodlines$isPossessed()) {
                stopInput(input);
            }
        }
        BloodlinesPlayerAttributes attributes = BloodlinesPlayerAttributes.get(mc.player);
        if(attributes.getGraveboundData().possessionActive && attributes.getGraveboundData().possessedEntity != null) {

            float sidewaysMotion = (
                    event.getInput().left ? 1f :
                        event.getInput().right ? -1f : 0 );
            float forwardMotion = (
                    event.getInput().up ? 1f :
                            event.getInput().down ? -1f : 0 );
            boolean sprinting = mc.options.keySprint.isDown();
            ServerboundPossessionInputPacket packet = new ServerboundPossessionInputPacket(sidewaysMotion, forwardMotion, mc.player.getXRot(), mc.player.getYRot(), mc.player.getYHeadRot(), input.jumping, input.shiftKeyDown, sprinting);
            mc.player.connection.send(packet);
            stopInput(input);


        }


    }
    public void stopInput(Input input) {
        input.up = false;
        input.down = false;
        input.right = false;
        input.left = false;
        input.jumping = false;
        input.forwardImpulse = 0f;
        input.leftImpulse = 0f;
        input.shiftKeyDown = false;
    }



}
