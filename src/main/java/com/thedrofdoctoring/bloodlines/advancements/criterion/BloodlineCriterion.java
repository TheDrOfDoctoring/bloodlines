package com.thedrofdoctoring.bloodlines.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thedrofdoctoring.bloodlines.advancements.BloodlinesAdvancementTriggers;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BloodlineCriterion extends SimpleCriterionTrigger<BloodlineCriterion.TriggerInstance>{

    public void trigger(@NotNull ServerPlayer player, IBloodline bloodline, int rank) {
        this.trigger(player, (instance -> instance.matches(bloodline, rank)));
    }

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return BloodlineCriterion.TriggerInstance.CODEC;
    }


    public record TriggerInstance(@NotNull Optional<ContextAwarePredicate> player, @NotNull Optional<IBloodline> bloodline, int rank, boolean specificRank) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<BloodlineCriterion.TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf( "player").forGetter(BloodlineCriterion.TriggerInstance::player),
                IBloodline.CODEC.optionalFieldOf("bloodline").forGetter(BloodlineCriterion.TriggerInstance::bloodline),
                Codec.INT.fieldOf("rank").forGetter(BloodlineCriterion.TriggerInstance::rank),
                Codec.BOOL.fieldOf("is_rank_specific").forGetter(BloodlineCriterion.TriggerInstance::specificRank)
        ).apply(inst, BloodlineCriterion.TriggerInstance::new));


        public static @NotNull Criterion<TriggerInstance> of(IBloodline bloodline, int rank, boolean specificRank) {
            return BloodlinesAdvancementTriggers.BLOODLINE_RANK.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.ofNullable(bloodline), rank, specificRank));
        }

        public static @NotNull Criterion<TriggerInstance> of(int rank, boolean specificRank) {
            return BloodlinesAdvancementTriggers.BLOODLINE_RANK.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), rank, specificRank));
        }


        public boolean matches(@NotNull IBloodline bloodline, int rank) {

            if(this.bloodline.isEmpty()) {
                return this.doesMatchRank(rank);
            }

            if(this.bloodline.map(bl -> bl.equals(bloodline)).orElse(false)) {
                return this.doesMatchRank(rank);
            }
            return false;
        }

        private boolean doesMatchRank(int rank) {
            if(specificRank) {
                return rank == this.rank;
            }
            return rank >= this.rank;
        }
    }


}
