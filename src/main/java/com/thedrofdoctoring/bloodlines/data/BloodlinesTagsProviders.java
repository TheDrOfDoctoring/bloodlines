package com.thedrofdoctoring.bloodlines.data;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import com.thedrofdoctoring.bloodlines.core.BloodlinesDamageTypes;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.tasks.BloodlineTasks;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.task.Task;
import de.teamlapen.vampirism.api.util.VResourceLocation;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.core.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
@SuppressWarnings("unused")
public class BloodlinesTagsProviders {

    public static void register(DataGenerator gen, GatherDataEvent event, PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper existingFileHelper) {
        BloodlinesBlockTagProvider blockTagProvider = new BloodlinesBlockTagProvider(output, future, existingFileHelper);
        gen.addProvider(event.includeServer(), blockTagProvider);
        gen.addProvider(event.includeServer(), new BloodlinesItemTagProvider(output, future, blockTagProvider.contentsGetter(), existingFileHelper));
        gen.addProvider(event.includeServer(), new BloodlinesSkillTreeProvider(output, future, existingFileHelper));
        gen.addProvider(event.includeServer(), new BloodlinesEntityTypeTagsProvider(output, future, existingFileHelper));
        gen.addProvider(event.includeServer(), new BloodlinesTasksTagProvider(output, future, existingFileHelper));
        gen.addProvider(event.includeServer(), new BloodlinesBiomeTagProvider(output, future, existingFileHelper));
        gen.addProvider(event.includeServer(), new BloodlinesDamageTypesProvider(output, future, existingFileHelper));


    }
    private static class BloodlinesTasksTagProvider extends TagsProvider<Task> {
        public BloodlinesTasksTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
            super(output, VampirismRegistries.Keys.TASK, lookupProvider, Bloodlines.MODID, existingFileHelper);
        }
        @Override
        protected void addTags(HolderLookup.@NotNull Provider pProvider) {
            this.tag(ModTags.Tasks.IS_UNIQUE).add(BloodlineTasks.BLOODLINE_NOBLE_1).add(BloodlineTasks.BLOODLINE_NOBLE_2).add(BloodlineTasks.BLOODLINE_NOBLE_3).add(BloodlineTasks.BLOODLINE_NOBLE_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_ZEALOT_1).add(BloodlineTasks.BLOODLINE_ZEALOT_2).add(BloodlineTasks.BLOODLINE_ZEALOT_3).add(BloodlineTasks.BLOODLINE_ZEALOT_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_ECTOTHERM_1).add(BloodlineTasks.BLOODLINE_ECTOTHERM_2).add(BloodlineTasks.BLOODLINE_ECTOTHERM_3).add(BloodlineTasks.BLOODLINE_ECTOTHERM_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_BLOODKNIGHT_1).add(BloodlineTasks.BLOODLINE_BLOODKNIGHT_2).add(BloodlineTasks.BLOODLINE_BLOODKNIGHT_3).add(BloodlineTasks.BLOODLINE_BLOODKNIGHT_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_GRAVEBOUND_1).add(BloodlineTasks.BLOODLINE_GRAVEBOUND_2).add(BloodlineTasks.BLOODLINE_GRAVEBOUND_3)
                    .add(BloodlineTasks.BLOODLINE_GRAVEBOUND_PERK_POINTS).add(BloodlineTasks.BLOODLINE_GRAVEBOUND_PERK_POINTS_2).add(BloodlineTasks.BLOODLINE_GRAVEBOUND_PERK_POINTS_3)
                    .replace(false);

            this.tag(ModTags.Tasks.IS_VAMPIRE).add(BloodlineTasks.BLOODLINE_NOBLE_1).add(BloodlineTasks.BLOODLINE_NOBLE_2).add(BloodlineTasks.BLOODLINE_NOBLE_3).add(BloodlineTasks.BLOODLINE_NOBLE_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_ZEALOT_1).add(BloodlineTasks.BLOODLINE_ZEALOT_2).add(BloodlineTasks.BLOODLINE_ZEALOT_3).add(BloodlineTasks.BLOODLINE_ZEALOT_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_ECTOTHERM_1).add(BloodlineTasks.BLOODLINE_ECTOTHERM_2).add(BloodlineTasks.BLOODLINE_ECTOTHERM_3).add(BloodlineTasks.BLOODLINE_ECTOTHERM_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_BLOODKNIGHT_1).add(BloodlineTasks.BLOODLINE_BLOODKNIGHT_2).add(BloodlineTasks.BLOODLINE_BLOODKNIGHT_3).add(BloodlineTasks.BLOODLINE_BLOODKNIGHT_PERK_POINTS)
                    .replace(false);
            this.tag(ModTags.Tasks.IS_HUNTER)
                    .add(BloodlineTasks.BLOODLINE_GRAVEBOUND_1).add(BloodlineTasks.BLOODLINE_GRAVEBOUND_2).add(BloodlineTasks.BLOODLINE_GRAVEBOUND_3)
                    .add(BloodlineTasks.BLOODLINE_GRAVEBOUND_PERK_POINTS).add(BloodlineTasks.BLOODLINE_GRAVEBOUND_PERK_POINTS_2).add(BloodlineTasks.BLOODLINE_GRAVEBOUND_PERK_POINTS_3)
                    .replace(false);
        }
        private static @NotNull TagKey<Task> tag(@NotNull String name) {
            return TagKey.create(VampirismRegistries.Keys.TASK, Bloodlines.rl(name));
        }
    }

    public static class BloodlinesSkillTreeProvider extends TagsProvider<ISkillTree> {
        public static final TagKey<ISkillTree> BLOODLINE = TagKey.create(VampirismRegistries.Keys.SKILL_TREE, Bloodlines.rl("type/bloodline"));
        protected BloodlinesSkillTreeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, VampirismRegistries.Keys.SKILL_TREE, provider, Bloodlines.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider pProvider) {

            this.tag(ModTags.SkillTrees.VAMPIRE).add(BloodlineSkills.Trees.NOBLE, BloodlineSkills.Trees.ZEALOT, BloodlineSkills.Trees.ECTOTHERM, BloodlineSkills.Trees.BLOOD_KNIGHT).replace(false);
            this.tag(BLOODLINE).add(BloodlineSkills.Trees.NOBLE, BloodlineSkills.Trees.ZEALOT, BloodlineSkills.Trees.ECTOTHERM, BloodlineSkills.Trees.BLOOD_KNIGHT, BloodlineSkills.Trees.GRAVEBOUND);
            this.tag(ModTags.SkillTrees.HUNTER).add(BloodlineSkills.Trees.GRAVEBOUND);
        }
    }
    public static class BloodlinesBlockTagProvider extends BlockTagsProvider {
        public static final TagKey<Block> ZEALOT_STONE = BlockTags.create(Bloodlines.rl("zealot_stone"));
        public static final TagKey<Block> ECTOTHERM_ICE = BlockTags.create(Bloodlines.rl("ectotherm_ice"));
        public static final TagKey<Block> GHOSTWALK_BLACKLIST = BlockTags.create(Bloodlines.rl("ghostwalk_blacklist"));


        public BloodlinesBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @org.jetbrains.annotations.Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Bloodlines.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            tag(ZEALOT_STONE).addTag(Tags.Blocks.STONES).addTag(Tags.Blocks.COBBLESTONES).add(Blocks.DRIPSTONE_BLOCK).addTag(Tags.Blocks.COBBLESTONES_DEEPSLATE);
            tag(ECTOTHERM_ICE).add(Blocks.ICE, Blocks.BLUE_ICE, Blocks.PACKED_ICE, BloodlinesBlocks.ICE_BLOCK.get());
            tag(GHOSTWALK_BLACKLIST).add(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.BEDROCK, Blocks.BARRIER, Blocks.END_STONE, Blocks.END_PORTAL_FRAME).addTag(BlockTags.AIR);

            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BloodlinesBlocks.ZEALOT_ALTAR.get(), BloodlinesBlocks.PHYLACTERY.get());
            tag(BlockTags.NEEDS_DIAMOND_TOOL).add(BloodlinesBlocks.ZEALOT_ALTAR.get(), BloodlinesBlocks.PHYLACTERY.get());
        }
    }
    public static class BloodlinesItemTagProvider extends ItemTagsProvider {
        public static final TagKey<Item> ECTOTHERM_RAW_FISH = ItemTags.create(Bloodlines.rl("ectotherm_raw_fish"));
        public static final TagKey<Item> VAMPIRE_SAMPLE_OBTAINING_WEAPONS = ItemTags.create(Bloodlines.rl("vampire_sample_obtaining_weapons"));

        public BloodlinesItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagsProvider, ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTagsProvider, Bloodlines.MODID, existingFileHelper);
        }
        @Override
        public @NotNull String getName() {
            return Bloodlines.MODID + " " + super.getName();
        }



        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            tag(ECTOTHERM_RAW_FISH).add(Items.COD, Items.SALMON, Items.TROPICAL_FISH);
            tag(VAMPIRE_SAMPLE_OBTAINING_WEAPONS).add(ModItems.STAKE.get(), ModItems.HEART_SEEKER_NORMAL.get(), ModItems.HEART_SEEKER_ENHANCED.get(), ModItems.HEART_SEEKER_ULTIMATE.get(), ModItems.HEART_STRIKER_NORMAL.get(), ModItems.HEART_STRIKER_ENHANCED.get(), ModItems.HEART_STRIKER_ULTIMATE.get());
        }
    }

    public static class BloodlinesBiomeTagProvider extends BiomeTagsProvider {

        public BloodlinesBiomeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider,ExistingFileHelper existingFileHelper) {
            super(pOutput, pProvider, Bloodlines.MODID, existingFileHelper);
        }

        public static final TagKey<Biome> NOBLE_BIOMES = tag("is_noble_biome");

        private static @NotNull TagKey<Biome> tag(@NotNull String name) {
            return TagKey.create(Registries.BIOME, Bloodlines.rl(name));
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            tag(NOBLE_BIOMES).addOptional(ResourceLocation.fromNamespaceAndPath("vampirism", "vampire_forest"));
        }
    }
    public static class BloodlinesDamageTypesProvider extends TagsProvider<DamageType> {

        public BloodlinesDamageTypesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, Registries.DAMAGE_TYPE, provider, Bloodlines.MODID, existingFileHelper);
        }

        public static final TagKey<DamageType> GRAVEBOUND_VULNERABLE = tag("gravebound_vulnerable");

        private static @NotNull TagKey<DamageType> tag(@NotNull String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, Bloodlines.rl(name));
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider pProvider) {
            this.tag(DamageTypeTags.BYPASSES_ARMOR).add(BloodlinesDamageTypes.DEVOUR_SOUL);
            this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(BloodlinesDamageTypes.DEVOUR_SOUL);
            this.tag(DamageTypeTags.NO_KNOCKBACK).add(BloodlinesDamageTypes.DEVOUR_SOUL);
            this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(BloodlinesDamageTypes.DEVOUR_SOUL);

            // missing references to vampirism. okay. whatever.
            this.tag(GRAVEBOUND_VULNERABLE).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC).addTag(DamageTypeTags.BYPASSES_INVULNERABILITY).addOptional(VResourceLocation.mod("holy_water"));
        }
    }

    public static class BloodlinesEntityTypeTagsProvider extends EntityTypeTagsProvider {
        public BloodlinesEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Bloodlines.MODID, existingFileHelper);
        }

        public static final TagKey<EntityType<?>> POSSESSION_WHITELIST = tag(Bloodlines.rl("possession_whitelist"));


        private static @NotNull TagKey<EntityType<?>> tag(@NotNull ResourceLocation resourceLocation) {
            return TagKey.create(Registries.ENTITY_TYPE, resourceLocation);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider holderLookup) {
            tag(POSSESSION_WHITELIST).add(
                    EntityType.ZOMBIE, EntityType.HUSK, EntityType.DROWNED,
                    EntityType.SKELETON, EntityType.BOGGED, EntityType.SILVERFISH,
                    EntityType.WITHER_SKELETON, EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER,
                    EntityType.CREEPER, EntityType.WOLF, EntityType.ENDERMAN,
                    EntityType.PHANTOM, EntityType.STRAY
            );
        }
    }
}
