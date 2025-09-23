package com.thedrofdoctoring.bloodlines.client.core;

import com.mojang.blaze3d.platform.InputConstants;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundDevourSoulPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class BloodlinesKeys {

    private static final String CATEGORY = "keys.bloodlines.category";

    public static final KeyMapping DEVOUR_SOUL = new KeyMapping("keys.bloodines.devour_soul", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY);


    public static void registerKeyMapping(@NotNull RegisterKeyMappingsEvent event) {
        event.register(DEVOUR_SOUL);
    }
    @SubscribeEvent
    public void handleKey(InputEvent.Key event) {
        int action = event.getAction();
        if(DEVOUR_SOUL.isDown() && action == InputConstants.PRESS) {
            devour();
        }
    }
    private void devour() {
        HitResult mouseOver = Minecraft.getInstance().hitResult;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && mouseOver != null && !player.isSpectator()) {
            IBloodline bl = BloodlinesPlayerAttributes.get(player).bloodline;
            if (bl == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get()) {
                if (mouseOver instanceof EntityHitResult) {
                    player.connection.send(new ServerboundDevourSoulPacket(((EntityHitResult) mouseOver).getEntity().getId()));
                }
            }
        }
    }

}
