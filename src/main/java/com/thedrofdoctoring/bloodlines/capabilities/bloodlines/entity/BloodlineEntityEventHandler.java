package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.entity;

import com.mojang.datafixers.util.Pair;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineManager;
import com.thedrofdoctoring.bloodlines.data.BloodlineSelector;
import com.thedrofdoctoring.bloodlines.data.BloodlinesData;
import com.thedrofdoctoring.bloodlines.data.spawn_modifiers.BloodlineRankDistribution;
import com.thedrofdoctoring.bloodlines.data.spawn_modifiers.BloodlineSpawnModifier;
import de.teamlapen.vampirism.entity.VampirismEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@EventBusSubscriber(modid = Bloodlines.MODID)
public class BloodlineEntityEventHandler {

    @SubscribeEvent
    public static void entityJoinWorldEvent(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof VampirismEntity entity && event.getLevel() instanceof ServerLevel level) {
            Optional<BloodlineMobManager> bloodlineMobManager = BloodlineMobManager.getSafe(entity);
            if(bloodlineMobManager.isEmpty()) return;
            BloodlineMobManager mobManager = bloodlineMobManager.get();
            if(mobManager.getBloodline() != null) {
                handleAIGoals(entity, mobManager.getBloodline(), level);
                return;
            }
            Optional<Registry<BloodlineSpawnModifier>> optSpawnModifiers = level.registryAccess().registry(BloodlinesData.BLOODLINE_SPAWN_MODIFIERS);
            if(optSpawnModifiers.isEmpty()) return;

            Registry<BloodlineSpawnModifier> spawnModifiersRegistry = optSpawnModifiers.get();
            List<WeightedEntry.Wrapper<IBloodline>> weightVals = new ArrayList<>();
            Holder<Biome> entityBiome = entity.level().getBiome(entity.blockPosition());

            for(Map.Entry<ResourceKey<BloodlineSpawnModifier>, BloodlineSpawnModifier> entries : spawnModifiersRegistry.entrySet()) {
                BloodlineSpawnModifier modifier = entries.getValue();
                if(!modifier.targetEntities().contains(event.getEntity().getType())) continue;

                int totalWeight = 0;

                for(Pair<HolderSet<Biome>, Integer> pairs : modifier.weightPairs()) {
                    if(pairs.getFirst().contains(entityBiome)) {
                        totalWeight += pairs.getSecond();
                    }
                }
                if(entity.blockPosition().getY() <= modifier.yLevelWeightPair().getFirst()) {
                    totalWeight += modifier.yLevelWeightPair().getSecond();
                }

                if(totalWeight >= 0) weightVals.add(WeightedEntry.wrap(modifier.bloodline(), totalWeight));

            }
            if(weightVals.isEmpty()) return;

            IBloodline targetBloodline = WeightedRandom.getRandomItem(level.getRandom(), weightVals).map(WeightedEntry.Wrapper::data).orElse(null);
            if(targetBloodline == null) return;

            Optional<Registry<BloodlineRankDistribution>> optRankDistributions = level.registryAccess().registry(BloodlinesData.BLOODLINE_RANK_DISTRIBUTION);
            if(optRankDistributions.isEmpty()) {
                Bloodlines.LOGGER.error("Couldn't access Bloodline Rank Distributions. Mobs will likely not receive bloodlines correctly.");
                return;
            }

            Registry<BloodlineRankDistribution> rankDistributions = optRankDistributions.get();

            BloodlineRankDistribution distribution = rankDistributions.get(targetBloodline.getBloodlineId());
            if(distribution == null) {
                Bloodlines.LOGGER.error("Bloodline chosen for entity {} but no rank distribution found, how?", event.getEntity());
                return;
            }

            List<Float> percentages = distribution.rankDistributions();
            float random = event.getEntity().getRandom().nextFloat();
            double cumulative = 0;
            int targetRank = 0;

            for (int i = 0; i < percentages.size(); i++) {
                cumulative += percentages.get(i);
                if (random <= cumulative) {
                    targetRank = i;
                    break;
                }
            }
            if(targetRank == 0) return;

            mobManager.setBloodline(targetBloodline);
            mobManager.setRank(targetRank);
            mobManager.onBloodlineChange(null, 0);
            handleAIGoals(entity, targetBloodline, level);

        }
    }
    private static void handleAIGoals(VampirismEntity entity, IBloodline bloodline, Level level) {
        Optional<Registry<BloodlineSelector>> AISelectors = level.registryAccess().registry(BloodlinesData.BLOODLINE_AI_SELECTORS);
        if(AISelectors.isEmpty()) return;
        Registry<BloodlineSelector> selectors = AISelectors.get();
        BloodlineSelector selector = selectors.get(bloodline.getBloodlineId());
        if(selector == null) return;
        Predicate<LivingEntity> predicate = getLivingEntityPredicate(selector);
        entity.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(entity, LivingEntity.class, true, predicate));
    }

    private static @NotNull Predicate<LivingEntity> getLivingEntityPredicate(BloodlineSelector selector) {
        List<IBloodline> enemyBloodlines = selector.targetBloodlines();
        boolean shouldAttackNonBloodlined = selector.targetNoBloodline();
        return entity -> {
            if(entity.isInvisible()) return false;

            if(entity instanceof Player || (entity instanceof VampirismEntity && !selector.playerOnly())) {
                Optional<IBloodlineManager> optManager = BloodlineHelper.getBloodlineData(entity);
                if(optManager.isPresent()) {
                    IBloodlineManager manager = optManager.get();
                    return enemyBloodlines.contains(manager.getBloodline());
                } else return shouldAttackNonBloodlined;
            }

            return false;
        };
    }

}
