package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setCamera(Lnet/minecraft/world/entity/Entity;)V"))
    public void tick(CallbackInfo ci) {

        ServerPlayer player = ((ServerPlayer) (Object) this);
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
        if(atts != null) {
            atts.getGraveboundData().possessionActive = false;
        }
    }
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;absMoveTo(DDDFF)V"
            ),
            remap = false
    )
    @SuppressWarnings("ConstantConditions")
    private void preventPossessedMove(ServerPlayer instance, double x, double y, double z, float a, float b, Operation<Void> original) {
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(((ServerPlayer) (Object) this));
        if(atts != null) {
            if(atts.getGraveboundData().possessionActive) {
                return;
            }
        }
        original.call(instance, x, y, z, a, b);
    }


}
