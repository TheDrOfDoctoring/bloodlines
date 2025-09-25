package com.thedrofdoctoring.bloodlines.tasks;

import com.mojang.serialization.MapCodec;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineBloodknight;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineZealot;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import com.thedrofdoctoring.bloodlines.core.BloodlinesStats;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.task.ITaskRewardInstance;
import de.teamlapen.vampirism.api.entity.player.task.Task;
import de.teamlapen.vampirism.api.entity.player.task.TaskReward;
import de.teamlapen.vampirism.api.entity.player.task.TaskUnlocker;
import de.teamlapen.vampirism.core.ModEntities;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.core.ModStats;
import de.teamlapen.vampirism.entity.player.tasks.TaskBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
@SuppressWarnings("unused")
public class BloodlineTasks {
    public static final DeferredRegister<MapCodec<? extends TaskUnlocker>> TASK_UNLOCKER = DeferredRegister.create(VampirismRegistries.Keys.TASK_UNLOCKER, Bloodlines.MODID);
    public static final DeferredRegister<MapCodec<? extends TaskReward>> TASK_REWARDS = DeferredRegister.create(VampirismRegistries.Keys.TASK_REWARD, Bloodlines.MODID);
    public static final DeferredRegister<MapCodec<? extends ITaskRewardInstance>> TASK_REWARD_INSTANCES = DeferredRegister.create(VampirismRegistries.Keys.TASK_REWARD_INSTANCE, Bloodlines.MODID);
    public static final DeferredHolder<MapCodec<? extends TaskUnlocker>, MapCodec<BloodlineUnlocker>> BLOODLINE_UNLOCKER = TASK_UNLOCKER.register("bloodline", () -> BloodlineUnlocker.CODEC);
    public static final DeferredHolder<MapCodec<? extends TaskUnlocker>, MapCodec<MaxPerkUnlocker>> MAX_PERK_UNLOCKER = TASK_UNLOCKER.register("max_perk_unlocker", () -> MaxPerkUnlocker.CODEC);


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
    public static final ResourceKey<Task> BLOODLINE_BLOODKNIGHT_1 = task("bloodline_bloodknight_one");
    public static final ResourceKey<Task> BLOODLINE_BLOODKNIGHT_2 = task("bloodline_bloodknight_two");
    public static final ResourceKey<Task> BLOODLINE_BLOODKNIGHT_3 = task("bloodline_bloodknight_three");
    public static final ResourceKey<Task> BLOODLINE_GRAVEBOUND_1 = task("bloodline_gravebound_one");
    public static final ResourceKey<Task> BLOODLINE_GRAVEBOUND_2 = task("bloodline_gravebound_two");
    public static final ResourceKey<Task> BLOODLINE_GRAVEBOUND_3 = task("bloodline_gravebound_three");
    public static final ResourceKey<Task> BLOODLINE_BLOODKNIGHT_PERK_POINTS = task("bloodline_perk_points_bloodknight_1");
    public static final ResourceKey<Task> BLOODLINE_ECTOTHERM_PERK_POINTS = task("bloodline_perk_points_ectotherm_1");
    public static final ResourceKey<Task> BLOODLINE_NOBLE_PERK_POINTS = task("bloodline_perk_points_noble_1");
    public static final ResourceKey<Task> BLOODLINE_ZEALOT_PERK_POINTS = task("bloodline_perk_points_zealot_1");
    public static final ResourceKey<Task> BLOODLINE_GRAVEBOUND_PERK_POINTS = task("bloodline_perk_points_gravebound_1");
    public static final ResourceKey<Task> BLOODLINE_GRAVEBOUND_PERK_POINTS_2 = task("bloodline_perk_points_gravebound_2");
    public static final ResourceKey<Task> BLOODLINE_GRAVEBOUND_PERK_POINTS_3 = task("bloodline_perk_points_gravebound_3");


    private static ResourceKey<Task> task(String path) {
        return ResourceKey.create(VampirismRegistries.Keys.TASK, Bloodlines.rl(path));
    }
    public static void createTasks(BootstrapContext<Task> context) {
        context.register(BLOODLINE_NOBLE_1, TaskBuilder.builder().setTitle(BLOODLINE_NOBLE_1.location())
                .setReward(new BloodlineRankReward(2, BLOODLINE_NOBLE_1.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineNoble.NOBLE, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 16))
                .addRequirement(new ItemStack(Items.GOLD_BLOCK, 4))
                .addRequirement(new ItemStack(Items.EMERALD, 16))
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 8)
                .build());
        context.register(BLOODLINE_NOBLE_2, TaskBuilder.builder().setTitle(BLOODLINE_NOBLE_2.location())
                .setReward(new BloodlineRankReward(3, BLOODLINE_NOBLE_2.location()))
                .unlockedBy(new BloodlineUnlocker(2, BloodlineNoble.NOBLE, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 24))
                .addRequirement(new ItemStack(Items.GOLD_BLOCK, 8))
                .addRequirement(new ItemStack(Items.EMERALD, 32))
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 16)
                .build());
        context.register(BLOODLINE_NOBLE_3, TaskBuilder.builder().setTitle(BLOODLINE_NOBLE_3.location())
                .setReward(new BloodlineRankReward(4, BLOODLINE_NOBLE_3.location()))
                .unlockedBy(new BloodlineUnlocker(3, BloodlineNoble.NOBLE, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 32))
                .addRequirement(new ItemStack(Items.GOLD_BLOCK, 12))
                .addRequirement(new ItemStack(Items.EMERALD, 64))
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 24)
                .build());

        context.register(BLOODLINE_NOBLE_PERK_POINTS, TaskBuilder.builder().setTitle(BLOODLINE_NOBLE_PERK_POINTS.location())
                .setReward(new BloodlinePerkReward(1, BLOODLINE_NOBLE_PERK_POINTS.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineNoble.NOBLE, false))
                .addRequirement(ModStats.BLOOD_DRUNK.get(), 10000 )
                .addRequirement(Stats.TRADED_WITH_VILLAGER, 20)
                .addRequirement(new ItemStack(Items.EMERALD, 24))
                .build());

        context.register(BLOODLINE_ZEALOT_1, TaskBuilder.builder().setTitle(BLOODLINE_ZEALOT_1.location())
                .setReward(new BloodlineRankReward(2, BLOODLINE_ZEALOT_1.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineZealot.ZEALOT, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 16))
                .addRequirement(new ItemStack(Items.SCULK, 64))
                .addRequirement(new ItemStack(Items.AMETHYST_BLOCK, 16))
                .addRequirement(EntityType.ENDERMAN, 12)
                .build());
        context.register(BLOODLINE_ZEALOT_2, TaskBuilder.builder().setTitle(BLOODLINE_ZEALOT_2.location())
                .setReward(new BloodlineRankReward(3, BLOODLINE_ZEALOT_2.location()))
                .unlockedBy(new BloodlineUnlocker(2, BloodlineZealot.ZEALOT, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 32))
                .addRequirement(new ItemStack(Items.SCULK, 64))
                .addRequirement(new ItemStack(Items.AMETHYST_BLOCK, 24))
                .addRequirement(EntityType.ENDERMAN, 24)
                .build());
        context.register(BLOODLINE_ZEALOT_3, TaskBuilder.builder().setTitle(BLOODLINE_ZEALOT_3.location())
                .setReward(new BloodlineRankReward(4, BLOODLINE_ZEALOT_3.location()))
                .unlockedBy(new BloodlineUnlocker(3, BloodlineZealot.ZEALOT, true))
                .addRequirement(new ItemStack(Items.DIAMOND, 64))
                .addRequirement(new ItemStack(Items.AMETHYST_BLOCK, 48))
                .addRequirement(ModStats.MOTHER_DEFEATED.get(), 1)
                .addRequirement(EntityType.ENDERMAN, 48)
                .build());
        context.register(BLOODLINE_ZEALOT_PERK_POINTS, TaskBuilder.builder().setTitle(BLOODLINE_ZEALOT_PERK_POINTS.location())
                .setReward(new BloodlinePerkReward(1, BLOODLINE_ZEALOT_PERK_POINTS.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineZealot.ZEALOT, false))
                .addRequirement(new ItemStack(Items.DIAMOND, 8))
                .addRequirement(new ItemStack(Items.AMETHYST_BLOCK, 12))
                .addRequirement(ModEntities.ADVANCED_VAMPIRE.get(), 5)
                .addRequirement(EntityType.ENDERMAN, 10)
                .build());
        context.register(BLOODLINE_ECTOTHERM_1, TaskBuilder.builder().setTitle(BLOODLINE_ECTOTHERM_1.location())
                .setReward(new BloodlineRankReward(2, BLOODLINE_ECTOTHERM_1.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineFrost.ECTOTHERM, true))
                .addRequirement(new ItemStack(Items.COD, 32))
                .addRequirement(new ItemStack(Items.HEART_OF_THE_SEA, 1))
                .addRequirement(EntityType.GUARDIAN, 20)
                .build());
        context.register(BLOODLINE_ECTOTHERM_2, TaskBuilder.builder().setTitle(BLOODLINE_ECTOTHERM_2.location())
                .setReward(new BloodlineRankReward(3, BLOODLINE_ECTOTHERM_2.location()))
                .unlockedBy(new BloodlineUnlocker(2, BloodlineFrost.ECTOTHERM, true))
                .addRequirement(new ItemStack(Items.SALMON, 48))
                .addRequirement(new ItemStack(BloodlinesItems.FROZEN_BLOOD_SAMPLE, 10))
                .addRequirement(EntityType.ELDER_GUARDIAN, 3)
                .build());
        context.register(BLOODLINE_ECTOTHERM_3, TaskBuilder.builder().setTitle(BLOODLINE_ECTOTHERM_3.location())
                .setReward(new BloodlineRankReward(4, BLOODLINE_ECTOTHERM_3.location()))
                .unlockedBy(new BloodlineUnlocker(3, BloodlineFrost.ECTOTHERM, true))
                .addRequirement(new ItemStack(Items.HEART_OF_THE_SEA, 2))
                .addRequirement(new ItemStack(BloodlinesItems.FROZEN_BLOOD_SAMPLE, 20))
                .addRequirement(EntityType.ELDER_GUARDIAN, 5)
                .build());
        context.register(BLOODLINE_ECTOTHERM_PERK_POINTS, TaskBuilder.builder().setTitle(BLOODLINE_ECTOTHERM_PERK_POINTS.location())
                .setReward(new BloodlinePerkReward(1, BLOODLINE_ECTOTHERM_PERK_POINTS.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineFrost.ECTOTHERM, false))
                .addRequirement(new ItemStack(BloodlinesItems.FROZEN_BLOOD_SAMPLE, 10))
                .addRequirement(new ItemStack(Items.HEART_OF_THE_SEA, 3))
                .addRequirement(EntityType.ELDER_GUARDIAN, 1)
                .build());
        context.register(BLOODLINE_BLOODKNIGHT_1, TaskBuilder.builder().setTitle(BLOODLINE_BLOODKNIGHT_1.location())
                .setReward(new BloodlineRankReward(2, BLOODLINE_BLOODKNIGHT_1.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineBloodknight.BLOOD_KNIGHT, true))
                .addRequirement(new ItemStack(ModItems.VAMPIRE_BLOOD_BOTTLE.get(), 10))
                .addRequirement(new ItemStack(ModItems.PURE_BLOOD_2.get(), 5))
                .addRequirement(ModEntities.VAMPIRE.get(), 50)
                .build());
        context.register(BLOODLINE_BLOODKNIGHT_2, TaskBuilder.builder().setTitle(BLOODLINE_BLOODKNIGHT_2.location())
                .setReward(new BloodlineRankReward(3, BLOODLINE_BLOODKNIGHT_2.location()))
                .unlockedBy(new BloodlineUnlocker(2, BloodlineBloodknight.BLOOD_KNIGHT, true))
                .addRequirement(new ItemStack(ModItems.VAMPIRE_BLOOD_BOTTLE.get(), 10))
                .addRequirement(new ItemStack(ModItems.PURE_BLOOD_3.get(), 5))
                .addRequirement(new ItemStack(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get(), 10))

                .addRequirement(ModEntities.ADVANCED_VAMPIRE.get(), 25)
                .build());
        context.register(BLOODLINE_BLOODKNIGHT_3, TaskBuilder.builder().setTitle(BLOODLINE_BLOODKNIGHT_3.location())
                .setReward(new BloodlineRankReward(4, BLOODLINE_BLOODKNIGHT_3.location()))
                .unlockedBy(new BloodlineUnlocker(3, BloodlineBloodknight.BLOOD_KNIGHT, true))
                .addRequirement(new ItemStack(ModItems.VAMPIRE_BLOOD_BOTTLE.get(), 20))
                .addRequirement(new ItemStack(ModItems.PURE_BLOOD_4.get(), 8))
                .addRequirement(new ItemStack(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get(), 20))

                .addRequirement(ModEntities.ADVANCED_VAMPIRE.get(), 50)
                .build());
        context.register(BLOODLINE_BLOODKNIGHT_PERK_POINTS, TaskBuilder.builder().setTitle(BLOODLINE_BLOODKNIGHT_PERK_POINTS.location())
                .setReward(new BloodlinePerkReward(1, BLOODLINE_BLOODKNIGHT_PERK_POINTS.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineBloodknight.BLOOD_KNIGHT, false))
                .addRequirement(new ItemStack(ModItems.PURE_BLOOD_3.get(), 4))
                .addRequirement(new ItemStack(ModItems.VAMPIRE_BLOOD_BOTTLE.get(), 4))
                .addRequirement(new ItemStack(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get(), 12))
                .addRequirement(ModEntities.VAMPIRE.get(), 20)
                .build());
        context.register(BLOODLINE_GRAVEBOUND_PERK_POINTS, TaskBuilder.builder().setTitle(BLOODLINE_GRAVEBOUND_PERK_POINTS.location())
                .setReward(new BloodlinePerkReward(1, BLOODLINE_GRAVEBOUND_PERK_POINTS.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineGravebound.GRAVEBOUND, false))
                .unlockedBy(new MaxPerkUnlocker(0, 5))
                .addRequirement(BloodlinesStats.MOBS_SOUL_DEVOURED.get(), 35)
                .addRequirement(ModEntities.ADVANCED_VAMPIRE.get(), 5)
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 3)
                .build());
        context.register(BLOODLINE_GRAVEBOUND_PERK_POINTS_2, TaskBuilder.builder().setTitle(BLOODLINE_GRAVEBOUND_PERK_POINTS_2.location())
                .setReward(new BloodlinePerkReward(1, BLOODLINE_GRAVEBOUND_PERK_POINTS_2.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineGravebound.GRAVEBOUND, false))
                .unlockedBy(new MaxPerkUnlocker(5, 10))
                .addRequirement(BloodlinesStats.MOBS_SOUL_DEVOURED.get(), 100)
                .addRequirement(ModEntities.ADVANCED_HUNTER.get(), 10)
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 8)
                .build());
        context.register(BLOODLINE_GRAVEBOUND_PERK_POINTS_3, TaskBuilder.builder().setTitle(BLOODLINE_GRAVEBOUND_PERK_POINTS_3.location())
                .setReward(new BloodlinePerkReward(1, BLOODLINE_GRAVEBOUND_PERK_POINTS_3.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineGravebound.GRAVEBOUND, false))
                .unlockedBy(new MaxPerkUnlocker(10, 15))
                .addRequirement(BloodlinesStats.MOBS_SOUL_DEVOURED.get(), 200)
                .addRequirement(ModEntities.ADVANCED_VAMPIRE.get(), 20)
                .addRequirement(ModEntities.ADVANCED_HUNTER.get(), 20)
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 10)
                .build());
        context.register(BLOODLINE_GRAVEBOUND_1, TaskBuilder.builder().setTitle(BLOODLINE_GRAVEBOUND_1.location())
                .setReward(new BloodlineRankReward(2, BLOODLINE_GRAVEBOUND_1.location()))
                .unlockedBy(new BloodlineUnlocker(1, BloodlineGravebound.GRAVEBOUND, true))
                .addRequirement(new ItemStack(BloodlinesItems.FROZEN_BLOOD_SAMPLE, 20))
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 10)
                .addRequirement(ModEntities.ADVANCED_VAMPIRE.get(), 10)
                .addRequirement(BloodlinesStats.SOULS_DEVOURED.get(), 250)
                .build());
        context.register(BLOODLINE_GRAVEBOUND_2, TaskBuilder.builder().setTitle(BLOODLINE_GRAVEBOUND_2.location())
                .setReward(new BloodlineRankReward(3, BLOODLINE_GRAVEBOUND_2.location()))
                .unlockedBy(new BloodlineUnlocker(2, BloodlineGravebound.GRAVEBOUND, true))
                .addRequirement(new ItemStack(BloodlinesItems.FROZEN_BLOOD_SAMPLE, 20))
                .addRequirement(new ItemStack(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE, 20))
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 15)
                .addRequirement(BloodlinesStats.SOULS_DEVOURED.get(), 500)
                .build());
        context.register(BLOODLINE_GRAVEBOUND_3, TaskBuilder.builder().setTitle(BLOODLINE_GRAVEBOUND_3.location())
                .setReward(new BloodlineRankReward(4, BLOODLINE_GRAVEBOUND_3.location()))
                .unlockedBy(new BloodlineUnlocker(3, BloodlineGravebound.GRAVEBOUND, true))
                .addRequirement(new ItemStack(BloodlinesItems.FROZEN_BLOOD_SAMPLE, 32))
                .addRequirement(new ItemStack(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE, 32))
                .addRequirement(ModEntities.VAMPIRE_BARON.get(), 25)
                .addRequirement(BloodlinesStats.SOULS_DEVOURED.get(), 1000)
                .build());
    }
}