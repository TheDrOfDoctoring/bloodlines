package com.thedrofdoctoring.bloodlines.data.loot;

import com.mojang.serialization.MapCodec;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class BloodlinesLoot {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Bloodlines.MODID);
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Bloodlines.MODID);

    public static final DeferredHolder<LootItemConditionType, LootItemConditionType> BLOODLINE_CONDITION = LOOT_CONDITION_TYPES.register("bloodline", () -> new LootItemConditionType(BloodlineLootCondition.CODEC));
    public static final DeferredHolder<LootItemConditionType, LootItemConditionType> MATCH_WEAPON = LOOT_CONDITION_TYPES.register("match_weapon", () -> new LootItemConditionType(MatchWeapon.CODEC));

}
