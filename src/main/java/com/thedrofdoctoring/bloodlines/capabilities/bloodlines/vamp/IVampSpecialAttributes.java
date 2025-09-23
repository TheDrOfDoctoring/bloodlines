package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp;

// Deprecated, use BloodlinesPlayerAttributes / Bloodline Special Attributes
public interface IVampSpecialAttributes {
    boolean bloodlines$getDayWalker();
    void bloodlines$setDayWalker(boolean dayWalker);
    boolean bloodlines$getMesmerise();

    void bloodlines$setMesmerise(boolean mesmerise);

    int bloodlines$getLeeching();

    void bloodlines$setLeeching(int leeching);

    boolean bloodlines$getSlowSun();
    void bloodlines$setSlowSun(boolean slowSun);

    boolean bloodlines$getRefraction();
    void bloodlines$setRefraction(boolean refraction);
}
