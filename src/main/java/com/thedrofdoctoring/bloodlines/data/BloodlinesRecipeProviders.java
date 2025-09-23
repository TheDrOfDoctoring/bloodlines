package com.thedrofdoctoring.bloodlines.data;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.core.ModTags;
import de.teamlapen.vampirism.data.recipebuilder.AlchemyTableRecipeBuilder;
import de.teamlapen.vampirism.entity.player.hunter.skills.HunterSkills;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class BloodlinesRecipeProviders extends RecipeProvider {
    public BloodlinesRecipeProviders(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BloodlinesItems.LORDSLAYER_INJECTION.get())
                .define('A', ModItems.INJECTION_SANGUINARE)
                .define('B', BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .unlockedBy("has_corrupted_blood", has(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get()))
                .save(pRecipeOutput, Bloodlines.rl("lordslayer_injection")
                );
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BloodlinesBlocks.PHYLACTERY.asItem())
                .define('A', Items.OBSIDIAN)
                .define('B', Items.TOTEM_OF_UNDYING)
                .define('C', Items.END_CRYSTAL)
                .define('D', Items.BONE_BLOCK)
                .pattern(" B ")
                .pattern("DCD")
                .pattern("AAA")
                .unlockedBy("has_totem", has(Items.TOTEM_OF_UNDYING))
                .save(pRecipeOutput, Bloodlines.rl("phylactery")
                );
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BloodlinesItems.SOUL_BINDER_REGULAR.get())
                .define('A', Items.BONE)
                .define('B', Items.GOLD_INGOT)
                .define('C', Items.SOUL_LANTERN)
                .pattern(" BC")
                .pattern(" A ")
                .pattern("A  ")
                .unlockedBy("has_obsidian", has(Items.OBSIDIAN))
                .save(pRecipeOutput, Bloodlines.rl("soul_binder")
                );
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BloodlinesItems.HEINOUS_ELIXIR.get())
                .requires(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get(), 6)
                .requires(ModTags.Items.PURE_BLOOD)
                .requires(ModItems.VAMPIRE_BLOOD_BOTTLE, 2)
                .unlockedBy("has_corrupted_blood", has(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get()))
                .save(pRecipeOutput, Bloodlines.rl("heinous_elixir")
                );
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BloodlinesItems.COLD_ELIXIR.get())
                .requires(BloodlinesItems.FROZEN_BLOOD_SAMPLE.get(), 6)
                .requires(ModTags.Items.PURE_BLOOD)
                .unlockedBy("has_frozen_blood", has(BloodlinesItems.FROZEN_BLOOD_SAMPLE.get()))
                .save(pRecipeOutput, Bloodlines.rl("frozen_elixir")
                );
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BloodlinesItems.ZEALOT_RITUAL_CATALYST.get())
                .requires(ModTags.Items.PURE_BLOOD)
                .requires(Items.AMETHYST_SHARD, 2)
                .requires(Items.ECHO_SHARD, 3)
                .requires(Items.EMERALD, 1)
                .requires(Items.PAPER, 1)
                .unlockedBy("has_pure_blood", has(ModTags.Items.PURE_BLOOD))
                .save(pRecipeOutput, Bloodlines.rl("ritual_catalyst")
                );
        AlchemyTableRecipeBuilder
                .builder(new ItemStack(BloodlinesItems.RENDING_ELIXIR))
                .ingredient(Ingredient.of(BloodlinesItems.FROZEN_BLOOD_SAMPLE.get()))
                .input(Ingredient.of(ModItems.PURE_BLOOD_4.get()))
                .withSkills(HunterSkills.MASTER_BREWER.get())
                .save(pRecipeOutput, Bloodlines.rl("rending_elixir"));
    }
}
