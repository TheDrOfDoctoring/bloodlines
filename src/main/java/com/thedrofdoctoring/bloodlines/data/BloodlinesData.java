package com.thedrofdoctoring.bloodlines.data;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.data.spawn_modifiers.BloodlineRankDistribution;
import com.thedrofdoctoring.bloodlines.data.spawn_modifiers.BloodlineRankDistributions;
import com.thedrofdoctoring.bloodlines.data.spawn_modifiers.BloodlineSpawnModifier;
import com.thedrofdoctoring.bloodlines.data.spawn_modifiers.BloodlinesSpawnModifiers;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.tasks.BloodlineTasks;
import de.teamlapen.vampirism.api.VampirismRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class BloodlinesData {

    public static final ResourceKey<Registry<BloodlineSpawnModifier>> BLOODLINE_SPAWN_MODIFIERS = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bloodlines.MODID, "bloodline_spawn_modifiers"));
    public static final ResourceKey<Registry<BloodlineRankDistribution>> BLOODLINE_RANK_DISTRIBUTION = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bloodlines.MODID, "bloodline_rank_distribution"));

    
    public static final RegistrySetBuilder DATA_BUILDER = new RegistrySetBuilder()
            .add(BLOODLINE_RANK_DISTRIBUTION, BloodlineRankDistributions::createRankDistributions)
            .add(BLOODLINE_SPAWN_MODIFIERS, BloodlinesSpawnModifiers::createSpawnModifiers)
            .add(VampirismRegistries.Keys.TASK, BloodlineTasks::createTasks)
            .add(VampirismRegistries.Keys.SKILL_TREE, BloodlineSkills.Trees::createSkillTrees)
            .add(VampirismRegistries.Keys.SKILL_NODE, BloodlineSkills.Nodes::createSkillNodes);
}
