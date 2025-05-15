package com.thedrofdoctoring.bloodlines.data.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class MatchWeapon implements LootItemCondition {

    public static final MapCodec<MatchWeapon> CODEC = RecordCodecBuilder.mapCodec(
            p -> p.group(
                    ItemPredicate.CODEC.optionalFieldOf("weapon_predicate").forGetter(e -> e.weaponPredicate)
            ).apply(p, MatchWeapon::new)
    );

    Optional<ItemPredicate> weaponPredicate;

    private MatchWeapon(Optional<ItemPredicate> weaponPredicate) {
        this.weaponPredicate = weaponPredicate;
    }

    public MatchWeapon(ItemPredicate.Builder predicateBuilder) {
        this.weaponPredicate = Optional.of(predicateBuilder.build());
    }

    @Override
    public @NotNull LootItemConditionType getType() {
        return BloodlinesLoot.MATCH_WEAPON.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        if(entity instanceof LivingEntity living) {
            ItemStack stack = living.getMainHandItem();
            return (this.weaponPredicate.isEmpty() || this.weaponPredicate.get().test(stack));
        }
        return false;
    }
}
