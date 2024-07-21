package com.thedrofdoctoring.bloodlines.data;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.tasks.BloodlineTasks;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.task.Task;
import de.teamlapen.vampirism.core.ModBlocks;
import de.teamlapen.vampirism.core.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.internal.NeoForgeBlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BloodlinesTagsProviders {

    public static void register(DataGenerator gen, GatherDataEvent event, PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper existingFileHelper) {
        BloodlinesBlockTagProvider blockTagProvider = new BloodlinesBlockTagProvider(output, future, existingFileHelper);
        gen.addProvider(event.includeServer(), blockTagProvider);
        gen.addProvider(event.includeServer(), new BloodlinesSkillTreeProvider(output, future, existingFileHelper));
        gen.addProvider(event.includeServer(), new BloodlinesTasksTagProvider(output, future, existingFileHelper));
        gen.addProvider(event.includeServer(), new BloodlinesItemTagProvider(output, future, blockTagProvider.contentsGetter(), existingFileHelper));
    }
    private static class BloodlinesTasksTagProvider extends TagsProvider<Task> {
        public BloodlinesTasksTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
            super(output, VampirismRegistries.Keys.TASK, lookupProvider, Bloodlines.MODID, existingFileHelper);
        }
        @Override
        protected void addTags(HolderLookup.@NotNull Provider pProvider) {
            this.tag(ModTags.Tasks.IS_UNIQUE).add(BloodlineTasks.BLOODLINE_NOBLE_1).add(BloodlineTasks.BLOODLINE_NOBLE_2).add(BloodlineTasks.BLOODLINE_NOBLE_3).add(BloodlineTasks.BLOODLINE_NOBLE_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_ZEALOT_1).add(BloodlineTasks.BLOODLINE_ZEALOT_2).add(BloodlineTasks.BLOODLINE_ZEALOT_3).add(BloodlineTasks.BLOODLINE_ZEALOT_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_ECTOTHERM_1).add(BloodlineTasks.BLOODLINE_ECTOTHERM_PERK_POINTS).replace(false)
                    ;
            this.tag(ModTags.Tasks.IS_VAMPIRE).add(BloodlineTasks.BLOODLINE_NOBLE_1).add(BloodlineTasks.BLOODLINE_NOBLE_2).add(BloodlineTasks.BLOODLINE_NOBLE_3).add(BloodlineTasks.BLOODLINE_NOBLE_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_ZEALOT_1).add(BloodlineTasks.BLOODLINE_ZEALOT_2).add(BloodlineTasks.BLOODLINE_ZEALOT_3).add(BloodlineTasks.BLOODLINE_ZEALOT_PERK_POINTS)
                    .add(BloodlineTasks.BLOODLINE_ECTOTHERM_1).add(BloodlineTasks.BLOODLINE_ECTOTHERM_PERK_POINTS).replace(false)
            ;
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

            this.tag(ModTags.SkillTrees.VAMPIRE).add(BloodlineSkills.Trees.NOBLE, BloodlineSkills.Trees.ZEALOT, BloodlineSkills.Trees.ECTOTHERM).replace(false);
            this.tag(BLOODLINE).add(BloodlineSkills.Trees.NOBLE, BloodlineSkills.Trees.ZEALOT, BloodlineSkills.Trees.ECTOTHERM);
        }
    }
    public static class BloodlinesBlockTagProvider extends BlockTagsProvider {
        public static final TagKey<Block> ZEALOT_STONE = BlockTags.create(Bloodlines.rl("zealot_stone"));
        public static final TagKey<Block> ECTOTHERM_ICE = BlockTags.create(Bloodlines.rl("ectotherm_ice"));

        public BloodlinesBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @org.jetbrains.annotations.Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Bloodlines.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            tag(ZEALOT_STONE).addTag(Tags.Blocks.STONES).addTag(Tags.Blocks.COBBLESTONES).add(Blocks.DRIPSTONE_BLOCK);
            tag(ECTOTHERM_ICE).add(Blocks.ICE, Blocks.BLUE_ICE, Blocks.PACKED_ICE, BloodlinesBlocks.ICE_BLOCK.get());
        }
    }
    public static class BloodlinesItemTagProvider extends ItemTagsProvider {
        public static final TagKey<Item> ECTOTHERM_RAW_FISH = ItemTags.create(Bloodlines.rl("ectotherm_raw_fish"));

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
        }
    }
}
