package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlineState;
import de.teamlapen.lib.HelperLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public interface IBloodlineManager {
    int getRank();

    void setRank(int rank);

    IBloodline getBloodline();

    ResourceLocation getBloodlineId();

    void setBloodline(IBloodline bloodline);

    LivingEntity getEntity();

    void updateAttributes(IBloodline oldBloodline);

    default void sync(boolean all) {
        HelperLib.sync(this, getEntity(), all);
    }

    void onBloodlineChange(IBloodline oldBloodline, int oldRank);

    Optional<BloodlineState> getBloodlineState();

}
