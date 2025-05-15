package com.thedrofdoctoring.bloodlines.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType, unused")
public class BloodlineLootCondition implements LootItemCondition {
    public static final MapCodec<BloodlineLootCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            IBloodline.CODEC.fieldOf("bloodline").forGetter(e -> e.bloodline),
            Codec.BOOL.fieldOf("entity_is_attacker").forGetter(e -> e.entityIsAttacker),
            Codec.INT.optionalFieldOf("min_rank").forGetter(e -> e.minRank),
            Codec.INT.optionalFieldOf("max_rank").forGetter(e -> e.maxRank)
    ).apply(inst, BloodlineLootCondition::new));

    private final IBloodline bloodline;
    private final Optional<Integer> maxRank;
    private final Optional<Integer> minRank;
    private final boolean entityIsAttacker;

    private BloodlineLootCondition(IBloodline bloodline, boolean entityIsAttacker, Optional<Integer> maxRank, Optional<Integer> minRank) {
        this.bloodline = bloodline;
        this.maxRank = maxRank;
        this.minRank = minRank;
        this.entityIsAttacker = entityIsAttacker;
    }
    public BloodlineLootCondition(IBloodline bloodline, boolean entityIsAttacker, int maxRank, int minRank) {
        this.bloodline = bloodline;
        this.maxRank = Optional.of(maxRank);
        this.minRank = Optional.of(minRank);
        this.entityIsAttacker = entityIsAttacker;

    }
    public BloodlineLootCondition(IBloodline bloodline, boolean entityIsAttacker,  int minRank) {
        this.bloodline = bloodline;
        this.maxRank = Optional.empty();
        this.minRank = Optional.of(minRank);
        this.entityIsAttacker = entityIsAttacker;

    }
    public BloodlineLootCondition(IBloodline bloodline, boolean entityIsAttacker) {
        this.bloodline = bloodline;
        this.maxRank = Optional.empty();
        this.minRank = Optional.empty();
        this.entityIsAttacker = entityIsAttacker;

    }


    @Override
    public @NotNull LootItemConditionType getType() {
        return BloodlinesLoot.BLOODLINE_CONDITION.get();
    }
    @Override
    public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.THIS_ENTITY, LootContextParams.ATTACKING_ENTITY);
    }
    @Override
    public boolean test(LootContext lootContext) {
        Entity entity;
        entity = entityIsAttacker ? lootContext.getParamOrNull(LootContextParams.ATTACKING_ENTITY) : lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof LivingEntity living) {
            Optional<IBloodlineManager> optManager = BloodlineHelper.getBloodlineData(living);
            if(optManager.isPresent()) {
                IBloodlineManager manager = optManager.get();
                int rank = manager.getRank();
                return bloodline == manager.getBloodline() && !this.minRank.map(minRank -> rank < minRank).orElse(false) && !this.maxRank.map(maxRank -> rank > maxRank).orElse(false);
            }
        }
        return false;
    }
}
