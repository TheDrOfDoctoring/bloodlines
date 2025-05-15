package com.thedrofdoctoring.bloodlines.data;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.core.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

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
                .unlockedBy("has_pure_blood", has(ModTags.Items.PURE_BLOOD))
                .save(pRecipeOutput, Bloodlines.rl("ritual_catalyst")
                );
    }
}
