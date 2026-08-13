package com.thedrofdoctoring.bloodlines.advancements;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.advancements.criterion.BloodlineCriterion;
import com.thedrofdoctoring.bloodlines.advancements.criterion.GenericCriterion;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import de.teamlapen.vampirism.api.util.VResourceLocation;
import de.teamlapen.vampirism.core.ModItems;
import net.minecraft.advancements.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BloodlinesAdvancementProvider extends AdvancementProvider {

    public BloodlinesAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new BloodlinesAdvancements()));
    }

    private interface BloodlinesAdvancementSubProvider {
        void generate(@NotNull AdvancementHolder root, @NotNull HolderLookup.Provider holderProvider, @NotNull Consumer<AdvancementHolder> consumer);
    }

    private static class BloodlinesAdvancements implements AdvancementGenerator {

        private final List<BloodlinesAdvancementSubProvider> subProvider = List.of(new MainAdvancements());

        @Override
        public void generate(HolderLookup.@NotNull Provider provider, @NotNull Consumer<AdvancementHolder> consumer, @NotNull ExistingFileHelper existingFileHelper) {
            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(ModItems.PURE_BLOOD_4.get(), Component.translatable("advancement.bloodlines"), Component.translatable("advancement.bloodlines.desc"), VResourceLocation.mod("textures/block/dark_stone_bricks.png"), AdvancementType.TASK, false, true, false)
                    .addCriterion("main", BloodlineCriterion.TriggerInstance.of(1, false))
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(consumer, Bloodlines.MODID + ":main/root");
            this.subProvider.forEach(p -> p.generate(root, provider, consumer));
        }
    }


    private static class MainAdvancements implements BloodlinesAdvancementSubProvider {

        @Override
        public void generate(@NotNull AdvancementHolder root, HolderLookup.@NotNull Provider holderProvider, @NotNull Consumer<AdvancementHolder> consumer) {
            AdvancementHolder rankingUp = Advancement.Builder.advancement()
                    .display(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get(), Component.translatable("advancement.bloodlines.rank_up"), Component.translatable("advancement.bloodlines.rank_up.desc"), null, AdvancementType.TASK, false, true, false)
                    .parent(root)
                    .addCriterion("main", BloodlineCriterion.TriggerInstance.of(2, false))
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(consumer, Bloodlines.MODID + ":main/ranking_up");
            AdvancementHolder elder = Advancement.Builder.advancement()
                    .display(ModItems.VAMPIRE_CLOTHING_CROWN.get(), Component.translatable("advancement.bloodlines.elder"), Component.translatable("advancement.bloodlines.elder.desc"), null, AdvancementType.CHALLENGE, true, true, false)
                    .parent(rankingUp)
                    .addCriterion("main", BloodlineCriterion.TriggerInstance.of(4, true))
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(consumer, Bloodlines.MODID + ":main/elder");
            AdvancementHolder pariah = Advancement.Builder.advancement()
                    .display(ModItems.VAMPIRE_BLOOD_BOTTLE.get(), Component.translatable("advancement.bloodlines.pariah"), Component.translatable("advancement.bloodlines.pariah.desc"), null, AdvancementType.TASK, false, true, false)
                    .parent(root)
                    .addCriterion("main", GenericCriterion.drinkVampireBlood())
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(consumer, Bloodlines.MODID + ":main/pariah");
            AdvancementHolder checkBloodline = Advancement.Builder.advancement()
                    .display(BloodlinesItems.BLOODLINE_TESTER.get(), Component.translatable("advancement.bloodlines.test_bloodline"), Component.translatable("advancement.bloodlines.test_bloodline.desc"), null, AdvancementType.TASK, false, true, false)
                    .parent(root)
                    .addCriterion("main", GenericCriterion.checkBloodline())
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(consumer, Bloodlines.MODID + ":main/check_bloodline");
        }

    }

}
