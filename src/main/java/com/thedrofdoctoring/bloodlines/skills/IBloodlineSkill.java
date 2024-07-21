package com.thedrofdoctoring.bloodlines.skills;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import net.minecraft.resources.ResourceLocation;

public interface IBloodlineSkill {

    /**
     * @return - The bloodline the skill belongs to
     */

    IBloodline getBloodline();

    /**
     * @return The ID of the bloodline.
     */
    ResourceLocation getBloodlineId();

    /**
     * @return Whether the skill requires a bloodline skillpoint to unlock
     */
    boolean requiresBloodlineSkillPoints();

}
