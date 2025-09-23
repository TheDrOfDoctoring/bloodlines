package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyReturnValue(method = "isInWall", at = @At("RETURN"))
    private boolean handleWallPhasing(boolean original) {
        Entity entity = (Entity) (Object) this;
        if(entity instanceof Player player) {
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
            if(atts.getEctothermAtts().icePhasing || atts.getGraveboundData().ghostWalk) {
                return false;
            }
        }
        return original;
    }


}
