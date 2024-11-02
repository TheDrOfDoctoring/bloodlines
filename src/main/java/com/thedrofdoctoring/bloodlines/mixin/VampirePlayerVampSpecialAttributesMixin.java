package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayerSpecialAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VampirePlayerSpecialAttributes.class)
public class VampirePlayerVampSpecialAttributesMixin implements IVampSpecialAttributes {
    @Unique
    public boolean bloodlines$mesmerising;
    @Unique
    public boolean bloodlines$noble_slow_sun;
    @Unique
    public int bloodlines$leeching;
    @Unique
    public float bloodlines$stoneRunSpeed;
    @Unique
    public boolean bloodlines$shadowArmour;
    @Unique
    public boolean bloodlines$refraction;
    @Unique
    public boolean bloodlines$icePhasing;
    @Unique
    public boolean bloodlines$inWall;
    @Unique
    public boolean bloodlines$frost;

    @Override
    public boolean bloodlines$getShadowArmour() {
        return this.bloodlines$shadowArmour;
    }

    @Override
    public void bloodlines$setShadowArmour(boolean shadowArmour) {
        this.bloodlines$shadowArmour = shadowArmour;
    }

    @Override
    public boolean bloodlines$getRefraction() {
        return bloodlines$refraction;
    }

    @Override
    public void bloodlines$setRefraction(boolean bloodlines$waterCooling) {
        this.bloodlines$refraction = bloodlines$waterCooling;
    }

    @Override
    public boolean bloodlines$getIcePhasing() {
        return bloodlines$icePhasing;
    }

    @Override
    public void bloodlines$setIcePhasing(boolean icePhasing) {
        this.bloodlines$icePhasing = icePhasing;
    }
    @Override
    public boolean bloodlines$isInWall() {
        return bloodlines$inWall;
    }

    @Override
    public void bloodlines$setInWall(boolean inWall) {
        this.bloodlines$inWall = inWall;
    }

    @Override
    public boolean bloodlines$getFrostControl() {
        return this.bloodlines$frost;
    }

    @Override
    public void bloodlines$setFrost(boolean frostControl) {
        this.bloodlines$frost = frostControl;
    }

    @Override
    public boolean bloodlines$getMesmerise() {
        return bloodlines$mesmerising;
    }

    @Override
    public void bloodlines$setMesmerise(boolean mesmerise) {
        this.bloodlines$mesmerising = mesmerise;
    }

    @Override
    public int bloodlines$getLeeching() {
        return bloodlines$leeching;
    }

    @Override
    public void bloodlines$setLeeching(int leeching) {
        this.bloodlines$leeching = leeching;
    }

    @Override
    public boolean bloodlines$getSlowSun() {
        return bloodlines$noble_slow_sun;
    }

    @Override
    public void bloodlines$setSlowSun(boolean slowSun) {
        this.bloodlines$noble_slow_sun = slowSun;
    }

    @Override
    public float bloodlines$getStoneRunSpeed() {
        return bloodlines$stoneRunSpeed;
    }

    @Override
    public void bloodlines$setStoneRunSpeed(float stoneSpeed) {
        this.bloodlines$stoneRunSpeed = stoneSpeed;
    }
}
