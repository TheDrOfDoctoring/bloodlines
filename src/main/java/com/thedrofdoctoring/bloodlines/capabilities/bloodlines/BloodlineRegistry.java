package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class BloodlineRegistry {
    //arguably not a registry
    private static final HashSet<IBloodline> bloodlines = new HashSet<>();

    public static Set<IBloodline> getBloodlines() {
        return bloodlines;
    }
    public static Set<ResourceLocation> getBloodlineIds() {
        Set<ResourceLocation> ids = new HashSet<>();
        for(IBloodline bloodline : bloodlines) {
            ids.add(bloodline.getBloodlineId());
        }
        return ids;
    }
    public static void registerBloodline(IBloodline bloodline) {
        bloodlines.add(bloodline);
    }

}
