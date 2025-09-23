package com.thedrofdoctoring.bloodlines.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {


    @Invoker("getFallDamageSound")
    public SoundEvent invokeGetFallDamageSound(int flag);

    @Invoker("isAffectedByFluids")
    public boolean invokeAffectedByFluids();

    @Invoker("getWaterSlowDown")
    public float invokeGetWaterSlowDown();


}
