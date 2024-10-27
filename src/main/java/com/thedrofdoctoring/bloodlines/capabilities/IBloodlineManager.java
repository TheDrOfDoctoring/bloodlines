package com.thedrofdoctoring.bloodlines.capabilities;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface IBloodlineManager {
    int getRank();
    void setRank(int rank);

    IBloodline getBloodline();
    ResourceLocation getBloodlineId();
    void setBloodline(IBloodline bloodline);

    LivingEntity getEntity();
    void updateAttributes(IBloodline oldBloodline);

}
