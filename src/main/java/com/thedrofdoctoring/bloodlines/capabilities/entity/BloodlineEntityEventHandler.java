package com.thedrofdoctoring.bloodlines.capabilities.entity;

import com.mojang.datafixers.util.Pair;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
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
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@EventBusSubscriber(modid = Bloodlines.MODID)
public class BloodlineEntityEventHandler {

    @SubscribeEvent
    public static void entityJoinWorldEvent(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof VampirismEntity entity && event.getLevel() instanceof ServerLevel level) {
            Optional<BloodlineMobManager> bloodlineMobManager = BloodlineMobManager.getSafe(entity);
            if((bloodlineMobManager.isPresent() && bloodlineMobManager.get().getBloodline() != null) || bloodlineMobManager.isEmpty()) return;

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

            IBloodline targetBloodline = WeightedRandom.getRandomItem(entity.getRandom(), weightVals).map(WeightedEntry.Wrapper::data).orElse(null);
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
            BloodlineMobManager mobManager = bloodlineMobManager.get();
            mobManager.setBloodline(targetBloodline);
            mobManager.setRank(targetRank);
            mobManager.onBloodlineChange(null, 0);

        }
    }
}
