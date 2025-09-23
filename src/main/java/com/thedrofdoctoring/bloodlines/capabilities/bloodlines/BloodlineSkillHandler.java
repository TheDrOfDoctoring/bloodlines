package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import net.minecraft.nbt.CompoundTag;

public class BloodlineSkillHandler  {

    private int otherSkillPoints;
    private int taskSkillPoints;
    private int enabledSkills;

    public void deserializeNBT(CompoundTag nbt) {
        this.taskSkillPoints = nbt.getInt("blSkillPoints");
        this.otherSkillPoints = nbt.getInt("blOtherSkillPoints");
        this.enabledSkills = nbt.getInt("blEnabledSkills");
    }
    public CompoundTag serializeNBT(CompoundTag nbt) {
        nbt.putInt("blSkillPoints", taskSkillPoints);
        nbt.putInt("blOtherSkillPoints", otherSkillPoints);
        nbt.putInt("blEnabledSkills", enabledSkills);
        return nbt;
    }

    public void deserializeUpdateNBT(CompoundTag nbt) {
        this.taskSkillPoints = nbt.getInt("blSkillPoints");
        this.otherSkillPoints = nbt.getInt("blOtherSkillPoints");
        this.enabledSkills = nbt.getInt("blEnabledSkills");
    }
    public CompoundTag serializeUpdateNBT(CompoundTag nbt) {
        nbt.putInt("blSkillPoints", taskSkillPoints);
        nbt.putInt("blOtherSkillPoints", otherSkillPoints);
        nbt.putInt("blEnabledSkills", enabledSkills);
        return nbt;
    }
    public void setEnabledSkills(int enabledSkills) {
        this.enabledSkills = enabledSkills;
    }
    public int getEnabledSkills() {
        return this.enabledSkills;
    }


    public void clearSkillPoints() {
        this.taskSkillPoints = 0;
        this.otherSkillPoints = 0;
    }

    public int getRemainingSkillPoints() {
        return Math.max(0, this.getTotalSkillPoints() - this.enabledSkills);
    }
    public int getTotalSkillPoints() {
        return this.taskSkillPoints + this.otherSkillPoints;
    }
    public int getTaskSkillPoints() {
        return this.taskSkillPoints;
    }

    public void addSkillPoints(int amount, boolean fromTask) {
        if(fromTask) {
            this.taskSkillPoints += amount;
        } else {
            this.otherSkillPoints += amount;
        }

    }
    public void addSkillPoints(int amount) {
        addSkillPoints(amount, false);
    }

}
