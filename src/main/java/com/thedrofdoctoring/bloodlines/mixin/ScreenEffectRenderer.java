package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(net.minecraft.client.renderer.ScreenEffectRenderer.class)
public class ScreenEffectRenderer {

    @ModifyReturnValue(method = "getOverlayBlock", at = @At("RETURN"))
    private static Pair<BlockState, BlockPos> hideIceOverlay(Pair<BlockState, BlockPos> original, Player player) {
        boolean inWall = BloodlinesPlayerAttributes.get(player).inWall;
        if(inWall) {
            return null;
        }
        return original;
    }
}
