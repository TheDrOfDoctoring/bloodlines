package com.thedrofdoctoring.bloodlines.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineZealot;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.task.ITaskRewardInstance;
import de.teamlapen.vampirism.api.entity.player.task.Task;
import de.teamlapen.vampirism.api.entity.player.task.TaskReward;
import de.teamlapen.vampirism.api.entity.player.task.TaskUnlocker;
import de.teamlapen.vampirism.core.ModEntities;
import de.teamlapen.vampirism.core.ModStats;
import de.teamlapen.vampirism.entity.player.tasks.TaskBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BloodlineTasks {
    public static final DeferredRegister<MapCodec<? extends TaskUnlocker>> TASK_UNLOCKER = DeferredRegister.create(VampirismRegistries.Keys.TASK_UNLOCKER, Bloodlines.MODID);
    public static final DeferredRegister<MapCodec<? extends TaskReward>> TASK_REWARDS = DeferredRegister.create(VampirismRegistries.Keys.TASK_REWARD, Bloodlines.MODID);
    public static final DeferredRegister<MapCodec<? extends ITaskRewardInstance>> TASK_REWARD_INSTANCES = DeferredRegister.create(VampirismRegistries.Keys.TASK_REWARD_INSTANCE, Bloodlines.MODID);
    public static final DeferredHolder<MapCodec<? extends TaskUnlocker>, MapCodec<BloodlineUnlocker>> BLOODLINE_UNLOCKER = TASK_UNLOCKER.register("bloodline", () -> BloodlineUnlocker.CODEC);
    public static final DeferredHolder<MapCodec<? extends TaskReward>, MapCodec<BloodlineRankReward>> BLOODLINE_RANK_REWARD = TASK_REWARDS.register("bloodline_reward", () -> BloodlineRankReward.CODEC);
    public static final DeferredHolder<MapCodec<? extends ITaskRewardInstance>, MapCodec<BloodlineRankReward>> BLOODLINE_RANK_REWARD_INSTANCE = TASK_REWARD_INSTANCES.register("bloodline_reward", () -> BloodlineRankReward.CODEC);
    public static final DeferredHolder<MapCodec<? extends TaskReward>, MapCodec<BloodlinePerkReward>> BLOODLINE_PERK_REWARD = TASK_REWARDS.register("bloodline_perk_reward", () -> BloodlinePerkReward.CODEC);
    public static final DeferredHolder<MapCodec<? extends ITaskRewardInstance>, MapCodec<BloodlinePerkReward>> BLOODLINE_PERK_REWARD_INSTANCE = TASK_REWARD_INSTANCES.register("bloodline_perk_reward", () -> BloodlinePerkReward.CODEC);
    public static final ResourceKey<Task> BLOODLINE_NOBLE_1 = task("bloodline_noble_one");
    public static final ResourceKey<Task> BLOODLINE_NOBLE_2 = task("bloodline_noble_two");
    public static final ResourceKey<Task> BLOODLINE_NOBLE_3 = task("bloodline_noble_three");
    public static final ResourceKey<Task> BLOODLINE_ZEALOT_1 = task("bloodline_zealot_one");
    public static final ResourceKey<Task> BLOODLINE_ZEALOT_2 = task("bloodline_zealot_two");
    public static final ResourceKey<Task> BLOODLINE_ZEALOT_3 = task("bloodline_zealot_three");
    public static final ResourceKey<Task> BLOODLINE_ECTOTHERM_1 = task("bloodline_ectotherm_one");
    public static final ResourceKey<Task> BLOODLINE_ECTOTHERM_2 = task("bloodline_ectotherm_two");
    public static final ResourceKey<Task> BLOODLINE_ECTOTHERM_3 = task("bloodline_ectotherm_three");
    public static final ResourceKey<Task> BLOODLINE_ECTOTHERM_PERK_POINTS = task("bloodline_perk_points_ectotherm_1");
    public static final ResourceKey<Task> BLOODLINE_NOBLE_PERK_POINTS = task("bloodline_perk_points_noble_1");
    public static final ResourceKey<Task> BLOODLINE_ZEALOT_PERK_POINTS = task("bloodline_perk_points_zealot_1");

    private static ResourceKey<Task> task(String path) {
        return ResourceKey.create(VampirismRegistries.Keys.TASK, Bloodlines.rl(path));
    }
    public static void createTasks(BootstrapContext<Task> context) {
        context.register(BLOODLINE_NOBLE_1, TaskBuilder.builder(BLOODLINE_NOBLE_1).defaultTitle()
                .setReward(new BloodlineRankReward(2))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineNoble.NOBLE, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 16))
                .addRequirement(new ItemStack(Items.GOLD_BLOCK, 4))
                .addRequirement(new ItemStack(Items.EMERALD, 16))
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 10)
                .build());
        context.register(BLOODLINE_NOBLE_2, TaskBuilder.builder(BLOODLINE_NOBLE_2).defaultTitle()
                .setReward(new BloodlineRankReward(3))
                .unlockedBy(new BloodlineUnlocker(2, BloodlineNoble.NOBLE, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 24))
                .addRequirement(new ItemStack(Items.GOLD_BLOCK, 8))
                .addRequirement(new ItemStack(Items.EMERALD, 32))
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 20)
                .build());
        context.register(BLOODLINE_NOBLE_3, TaskBuilder.builder(BLOODLINE_NOBLE_3).defaultTitle()
                .setReward(new BloodlineRankReward(4))
                .unlockedBy(new BloodlineUnlocker(3, BloodlineNoble.NOBLE, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 32))
                .addRequirement(new ItemStack(Items.GOLD_BLOCK, 12))
                .addRequirement(new ItemStack(Items.EMERALD, 64))
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 30)
                .build());

        context.register(BLOODLINE_NOBLE_PERK_POINTS, TaskBuilder.builder(BLOODLINE_NOBLE_PERK_POINTS).defaultTitle()
                .setReward(new BloodlinePerkReward(1))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineNoble.NOBLE, false))
                .addRequirement(ModStats.BLOOD_DRUNK.get(), 10000 )
                .addRequirement(Stats.TRADED_WITH_VILLAGER, 20)
                .addRequirement(new ItemStack(Items.EMERALD, 24))
                .build());

        context.register(BLOODLINE_ZEALOT_1, TaskBuilder.builder(BLOODLINE_ZEALOT_1).defaultTitle()
                .setReward(new BloodlineRankReward(2))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineZealot.ZEALOT, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 16))
                .addRequirement(new ItemStack(Items.SCULK, 64))
                .addRequirement(new ItemStack(Items.AMETHYST_BLOCK, 16))
                .addRequirement(EntityType.ENDERMAN, 12)
                .build());
        context.register(BLOODLINE_ZEALOT_2, TaskBuilder.builder(BLOODLINE_ZEALOT_2).defaultTitle()
                .setReward(new BloodlineRankReward(3))
                .unlockedBy(new BloodlineUnlocker(2, BloodlineZealot.ZEALOT, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 32))
                .addRequirement(new ItemStack(Items.SCULK, 64))
                .addRequirement(new ItemStack(Items.AMETHYST_BLOCK, 24))
                .addRequirement(EntityType.ENDERMAN, 24)
                .build());
        context.register(BLOODLINE_ZEALOT_3, TaskBuilder.builder(BLOODLINE_ZEALOT_3).defaultTitle()
                .setReward(new BloodlineRankReward(4))
                .unlockedBy(new BloodlineUnlocker(3, BloodlineZealot.ZEALOT, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 64))
                .addRequirement(new ItemStack(Items.AMETHYST_BLOCK, 48))
                .addRequirement(ModStats.MOTHER_DEFEATED.get(), 1)
                .addRequirement(EntityType.ENDERMAN, 48)
                .build());
        context.register(BLOODLINE_ZEALOT_PERK_POINTS, TaskBuilder.builder(BLOODLINE_ZEALOT_PERK_POINTS).defaultTitle()
                .setReward(new BloodlinePerkReward(1))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineZealot.ZEALOT, false))
                .addRequirement(new ItemStack(Items.DIAMOND, 8))
                .addRequirement(new ItemStack(Items.AMETHYST_BLOCK, 12))
                .addRequirement(ModEntities.ADVANCED_VAMPIRE.get(), 10)
                .addRequirement(EntityType.ENDERMAN, 10)
                .build());
        context.register(BLOODLINE_ECTOTHERM_1, TaskBuilder.builder(BLOODLINE_ECTOTHERM_1).defaultTitle()
                .setReward(new BloodlineRankReward(2))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineFrost.ECTOTHERM, true))
                .addRequirement(new ItemStack(Items.COD, 32))
                .addRequirement(new ItemStack(Items.WATER_BUCKET, 10))
                .addRequirement(EntityType.GUARDIAN, 10)
                .build());
        context.register(BLOODLINE_ECTOTHERM_2, TaskBuilder.builder(BLOODLINE_ECTOTHERM_2).defaultTitle()
                .setReward(new BloodlineRankReward(3))
                .unlockedBy(new BloodlineUnlocker(2, BloodlineFrost.ECTOTHERM, true))
                .addRequirement(new ItemStack(Items.SALMON, 48))
                .addRequirement(new ItemStack(Items.ICE, 30))
                .addRequirement(EntityType.GUARDIAN, 20)
                .build());
        context.register(BLOODLINE_ECTOTHERM_3, TaskBuilder.builder(BLOODLINE_ECTOTHERM_3).defaultTitle()
                .setReward(new BloodlineRankReward(4))
                .unlockedBy(new BloodlineUnlocker(3, BloodlineFrost.ECTOTHERM, true))
                .addRequirement(new ItemStack(Items.TROPICAL_FISH, 64))
                .addRequirement(new ItemStack(Items.PACKED_ICE, 64))
                .addRequirement(EntityType.ELDER_GUARDIAN, 1)
                .build());
        context.register(BLOODLINE_ECTOTHERM_PERK_POINTS, TaskBuilder.builder(BLOODLINE_ECTOTHERM_PERK_POINTS).defaultTitle()
                .setReward(new BloodlinePerkReward(1))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineFrost.ECTOTHERM, false))
                .addRequirement(new ItemStack(Items.SALMON, 32))
                .addRequirement(new ItemStack(Items.DIAMOND, 8))
                .addRequirement(EntityType.ELDER_GUARDIAN, 1)
                .build());
    }
}