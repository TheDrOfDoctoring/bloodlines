package com.thedrofdoctoring.bloodlines.skills;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;

public class BloodlineParentSkill<T extends IFactionPlayer<T>> extends BloodlineSkill<T> {

    int rank;

    BloodlineParentSkill(IBloodline bloodline, boolean desc, int cost, int rank) {
        super(bloodline, desc, cost, false);
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }
}
