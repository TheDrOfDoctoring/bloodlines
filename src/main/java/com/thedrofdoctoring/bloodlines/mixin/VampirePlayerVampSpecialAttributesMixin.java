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
    public boolean bloodlines$refraction;

    @Unique
    public boolean bloodlines$dayWalker;


    @Override
    public boolean bloodlines$getRefraction() {
        return bloodlines$refraction;
    }

    @Override
    public void bloodlines$setRefraction(boolean bloodlines$waterCooling) {
        this.bloodlines$refraction = bloodlines$waterCooling;
    }


    @Override
    public boolean bloodlines$getDayWalker() {
        return this.bloodlines$dayWalker;
    }

    @Override
    public void bloodlines$setDayWalker(boolean dayWalker) {
        this.bloodlines$dayWalker = dayWalker;
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

}
