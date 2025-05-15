package com.thedrofdoctoring.bloodlines.data.loot;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class BloodlinesLootProvider {
    private static final Set<ResourceKey<LootTable>> LOOT_TABLES = Sets.newHashSet();


    public static final ResourceKey<LootTable> CORRUPTED_BLOOD_CHEST_TABLE = register("chests/corrupted_blood");
    public static final ResourceKey<LootTable> CORRUPTED_BLOOD_ENTITY_TABLE = register("entities/corrupted_blood");
    public static final ResourceKey<LootTable> FROZEN_BLOOD_ENTITY_TABLE = register("entities/frozen_blood");

    public static @NotNull Set<ResourceKey<LootTable>> getLootTables() {
        return ImmutableSet.copyOf(LOOT_TABLES);
    }

    static @NotNull ResourceKey<LootTable> register(@NotNull String resourceName) {
        return register(Bloodlines.rl(resourceName));
    }

    static @NotNull ResourceKey<LootTable> register(@NotNull ResourceLocation resourceLocation) {
        ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, resourceLocation);
        LOOT_TABLES.add(key);
        return key;
    }


    public static LootTableProvider getProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProviderFuture) {
        return new LootTableProvider(output, getLootTables(),
                List.of(
                        new LootTableProvider.SubProviderEntry(BloodlinesGenericLootTables::new, LootContextParamSets.CHEST),
                        new LootTableProvider.SubProviderEntry(BloodlinesInjectEntityLootTables::new, LootContextParamSets.ENTITY)
                ),
                lookupProviderFuture);
    }

    private record BloodlinesInjectEntityLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {


        @Override
        public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> builder) {
            builder.accept(CORRUPTED_BLOOD_ENTITY_TABLE, LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0f, 2f))
                            .add(LootItem.lootTableItem(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get()).setWeight(10))
                            .add(EmptyLootItem.emptyItem().setWeight(60)))
            );
            builder.accept(FROZEN_BLOOD_ENTITY_TABLE, LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0f, 1f))
                            .add(LootItem.lootTableItem(BloodlinesItems.FROZEN_BLOOD_SAMPLE.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                            .add(EmptyLootItem.emptyItem().setWeight(25)))
            );
        }
    }

    private record BloodlinesGenericLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {


        @Override
            public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> builder) {
                builder.accept(CORRUPTED_BLOOD_CHEST_TABLE, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0f, 3f))
                                .add(LootItem.lootTableItem(BloodlinesItems.CORRUPTED_BLOOD_SAMPLE.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 4.0F))))
                                .add(EmptyLootItem.emptyItem().setWeight(5)))
                );
            }
        }
}
