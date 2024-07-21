package com.thedrofdoctoring.bloodlines.items;


import de.teamlapen.vampirism.core.ModFluids;
import de.teamlapen.vampirism.items.BloodBottleFluidHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class BottomlessChaliceFluidHandler extends BloodBottleFluidHandler {
    public BottomlessChaliceFluidHandler(@NotNull ItemStack container, int capacity) {
        super(container, capacity);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction action) {
        int currentAmt = this.getBlood(this.container);
        if (currentAmt == 0) {
            return FluidStack.EMPTY;
        } else {
            FluidStack stack = new FluidStack((Fluid) ModFluids.BLOOD.get(), Math.min(currentAmt, getAdjustedAmount(maxDrain)));
            if (action.execute()) {
                this.setBlood(this.container, currentAmt - stack.getAmount());
            }
            return stack;
        }
    }
    @Override
    public int getBlood(@NotNull ItemStack stack) {
        return stack.getDamageValue() * 100;
    }


}

