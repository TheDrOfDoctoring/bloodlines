package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BloodlineSkillHandler  {

    private int skillPoints;
    private int enabledSkills;

    private final Map<ResourceLocation, Integer> rewardMap = new HashMap<>();

    public void deserializeNBT(CompoundTag nbt) {
        this.skillPoints = nbt.getInt("blSkillPoints");
        this.enabledSkills = nbt.getInt("blEnabledSkills");
        deserialiseRewardMap(nbt);
    }
    public CompoundTag serializeNBT(CompoundTag nbt) {
        nbt.putInt("blSkillPoints", skillPoints);
        nbt.putInt("blEnabledSkills", enabledSkills);
        serializeRewardMap(nbt);
        return nbt;
    }

    public void deserializeUpdateNBT(CompoundTag nbt) {
        this.skillPoints = nbt.getInt("blSkillPoints");
        this.enabledSkills = nbt.getInt("blEnabledSkills");
        deserialiseRewardMap(nbt);
    }
    public CompoundTag serializeUpdateNBT(CompoundTag nbt) {
        nbt.putInt("blSkillPoints", skillPoints);
        nbt.putInt("blEnabledSkills", enabledSkills);
        serializeRewardMap(nbt);
        return nbt;
    }
    public void setEnabledSkills(int enabledSkills) {
        this.enabledSkills = enabledSkills;
    }
    public int getEnabledSkills() {
        return this.enabledSkills;
    }

    public void clearSkillPoints() {
        this.skillPoints = 0;
        this.rewardMap.clear();
    }
    public int getRemainingSkillPoints() {
        return Math.max(0, this.skillPoints - this.enabledSkills);
    }

    public void addSkillPoints(ResourceLocation source, int amount) {
        this.skillPoints += amount;
        if(rewardMap.containsKey(source)) {
            rewardMap.compute(source, (k, totalGained) -> totalGained == null ? amount : totalGained + amount);
            return;
        }
        rewardMap.put(source, amount);
    }

    public void addSkillPoints(int amount) {
        this.skillPoints += amount;
    }

    public int getPointsFromSource(ResourceLocation source) {
        return rewardMap.getOrDefault(source, 0);
    }


    private void serializeRewardMap(CompoundTag nbt) {
        for(Map.Entry<ResourceLocation, Integer> entry : this.rewardMap.entrySet()) {
            nbt.putInt(entry.getKey().toString(), entry.getValue());
        }
    }
    private void deserialiseRewardMap(CompoundTag nbt) {
        for(String key : nbt.getAllKeys()) {

            DataResult<ResourceLocation> location = ResourceLocation.read(key);
            if(location.isError()) continue;

            int val = nbt.getInt(key);
            rewardMap.put(ResourceLocation.parse(key), val);

        }
    }
}
