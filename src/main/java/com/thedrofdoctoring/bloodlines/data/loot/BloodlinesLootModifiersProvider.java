package com.thedrofdoctoring.bloodlines.data.loot;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import de.teamlapen.vampirism.core.ModLootTables;
import de.teamlapen.vampirism.core.ModTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class BloodlinesLootModifiersProvider extends GlobalLootModifierProvider {


    public BloodlinesLootModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Bloodlines.MODID);
    }

    @Override
    protected void start() {

        this.add(
                "bloodlines_corrupted_blood_modifier",
                new AddTableLootModifier(new LootItemCondition[] {

                        new MatchWeapon(ItemPredicate.Builder.item().of(BloodlinesTagsProviders.BloodlinesItemTagProvider.HEART_SEEKER_STRIKER)),
                        new BloodlineLootCondition(BloodlineRegistry.BLOODLINE_BLOODKNIGHT.get(), false)

                }, BloodlinesLootProvider.CORRUPTED_BLOOD_ENTITY_TABLE)
        );
        this.add(
                "bloodlines_frozen_blood_modifier",
                new AddTableLootModifier(new LootItemCondition[] {

                        new MatchWeapon(ItemPredicate.Builder.item().of(BloodlinesTagsProviders.BloodlinesItemTagProvider.HEART_SEEKER_STRIKER)),
                        new BloodlineLootCondition(BloodlineRegistry.BLOODLINE_ECTOTHERM.get(), false)

                }, BloodlinesLootProvider.FROZEN_BLOOD_ENTITY_TABLE)
        );
        this.add(
                "bloodlines_corrupted_blood_crypt_loot_modifier",
                new AddTableLootModifier(new LootItemCondition[] {
                        LootTableIdCondition.builder(ModLootTables.CHEST_CRYPT.location()).build()
                }, BloodlinesLootProvider.CORRUPTED_BLOOD_CHEST_TABLE)
        );
    }
}
