package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.entity.BloodlineMobManager;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import java.util.Optional;

public class BloodlineHelper {
    public static int getBloodlineRank(Player player) {
        return BloodlineManager.getOpt(player).map(BloodlineManager::getRank).orElse(0);
    }

    public static IBloodline getBloodlineById(ResourceLocation id) {
        if(BloodlineRegistry.BLOODLINE_REGISTRY.containsKey(id)) {
            return BloodlineRegistry.BLOODLINE_REGISTRY.get(id);
        }
        return null;
    }

    /**
     * @return Optional containing bloodline manager if the entity has one with a non-null Bloodline
     *         Returns empty optional otherwise
     */
    public static Optional<IBloodlineManager> getBloodlineData(LivingEntity entity) {
        if (entity instanceof PathfinderMob pathfinderMob) {
            return BloodlineMobManager.getSafe(pathfinderMob)
                    .filter(blManager -> blManager.getBloodline() != null)
                    .map(blManager -> blManager);
        } else if (entity instanceof Player player) {
            BloodlineManager blManager = BloodlineManager.get(player);
            return blManager.getBloodline() != null ? Optional.of(blManager) : Optional.empty();
        }
        return Optional.empty();
    }
    public static IBloodlineManager getBloodlineManager(LivingEntity entity) {
        if (entity instanceof PathfinderMob pathfinderMob) {
            return BloodlineMobManager.get(pathfinderMob);
        } else if (entity instanceof Player player) {
            return BloodlineManager.get(player);
        }
        return null;
    }


    public static boolean lightMatches(int requiredLight, BlockPos pos, Level level, boolean includeShade) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        int highestLight = Math.max(blockLight, skyLight);

        if(highestLight > requiredLight && !includeShade) {
            return false;
        }
        if(level.isNight() && includeShade && blockLight < requiredLight && skyLight <= 13) {
            return true;
        } else return highestLight < requiredLight;
    }
}
