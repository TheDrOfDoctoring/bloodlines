package com.thedrofdoctoring.bloodlines.capabilities;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface IBloodlineManager {
    int getRank();
    void setRank(int rank);

    IBloodline getBloodline();
    IBloodline getBloodlineById(ResourceLocation id);
    ResourceLocation getBloodlineId();
    void setBloodline(IBloodline bloodline);

    <T extends IFactionPlayer<T>> void onBloodlineChange(IBloodline oldBloodline, int oldRank);
    Player getPlayer();
    void updateAttributes(IBloodline oldBloodline);

    BloodlineSkillHandler getSkillHandler();

}
