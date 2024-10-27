package com.thedrofdoctoring.bloodlines.capabilities;

import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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
