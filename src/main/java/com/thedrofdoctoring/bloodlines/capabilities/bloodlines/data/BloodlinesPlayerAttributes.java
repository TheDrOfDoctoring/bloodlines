package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineZealot;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BloodlinesPlayerAttributes {
    public static BloodlinesPlayerAttributes get(@NotNull Player player) {
        return ((IBloodlinesPlayer) player).bloodlines$getBloodlinesAtts();

    }

    private final BloodlineGravebound.SpecialAttributes graveboundAtts = new BloodlineGravebound.SpecialAttributes();
    private final BloodlineFrost.SpecialAttributes ectothermAtts = new BloodlineFrost.SpecialAttributes();
    private final BloodlineZealot.SpecialAttributes zealotAtts = new BloodlineZealot.SpecialAttributes();



    public int bloodlineRank;
    public IBloodline bloodline;

    public BloodlineGravebound.SpecialAttributes getGraveboundData() {
        return graveboundAtts;
    }
    public BloodlineFrost.SpecialAttributes getEctothermAtts() {
        return ectothermAtts;
    }
    public BloodlineZealot.SpecialAttributes getZealotAtts() {
        return zealotAtts;
    }

    public boolean inWall;

}
