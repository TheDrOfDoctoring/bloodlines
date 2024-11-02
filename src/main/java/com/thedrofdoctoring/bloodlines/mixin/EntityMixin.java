package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyReturnValue(method = "isInWall", at = @At("RETURN"))
    private boolean handleWallPhasing(boolean original) {
        Entity entity = (Entity) (Object) this;
        if(entity instanceof Player player && Helper.isVampire(player)) {
            VampirePlayer vp = VampirePlayer.get(player);
            if(((IVampSpecialAttributes) vp.getSpecialAttributes()).bloodlines$getIcePhasing()) {
                return false;
            }
        }
        return original;
    }
}
