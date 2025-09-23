package com.thedrofdoctoring.bloodlines.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityInvoker {

    @Invoker("setSharedFlag")
    public void invokeSetFlag(int flag, boolean set);
}
