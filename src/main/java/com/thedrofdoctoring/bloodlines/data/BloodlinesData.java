package com.thedrofdoctoring.bloodlines.data;

import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.tasks.BloodlineTasks;
import de.teamlapen.vampirism.api.VampirismRegistries;
import net.minecraft.core.RegistrySetBuilder;

public class BloodlinesData {
    public static final RegistrySetBuilder DATA_BUILDER = new RegistrySetBuilder()
            .add(VampirismRegistries.Keys.TASK, BloodlineTasks::createTasks)
            .add(VampirismRegistries.Keys.SKILL_TREE, BloodlineSkills.Trees::createSkillTrees)
            .add(VampirismRegistries.Keys.SKILL_NODE, BloodlineSkills.Nodes::createSkillNodes);
}
