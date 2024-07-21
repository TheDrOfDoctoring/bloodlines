package com.thedrofdoctoring.bloodlines.capabilities;

import net.minecraft.nbt.CompoundTag;

public class BloodlineSkillHandler  {

    private int skillPoints;
    private int enabledSkills;
    public void deserializeNBT(CompoundTag nbt) {
        this.skillPoints = nbt.getInt("blSkillPoints");
        this.enabledSkills = nbt.getInt("blEnabledSkills");
    }
    public CompoundTag serializeNBT(CompoundTag nbt) {
        nbt.putInt("blSkillPoints", skillPoints);
        nbt.putInt("blEnabledSkills", enabledSkills);
        return nbt;
    }

    public void deserializeUpdateNBT(CompoundTag nbt) {
        this.skillPoints = nbt.getInt("blSkillPoints");
        this.enabledSkills = nbt.getInt("blEnabledSkills");
    }
    public CompoundTag serializeUpdateNBT(CompoundTag nbt) {
        nbt.putInt("blSkillPoints", skillPoints);
        nbt.putInt("blEnabledSkills", enabledSkills);
        return nbt;
    }
    public void setEnabledSkills(int enabledSkills) {
        this.enabledSkills = enabledSkills;
    }
    public int getEnabledSkills() {
        return this.enabledSkills;
    }

    public int getSkillPoints() {
        return this.skillPoints;
    }
    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }
    public int getRemainingSkillPoints() {
        return Math.max(0, this.skillPoints - this.enabledSkills);
    }
}
