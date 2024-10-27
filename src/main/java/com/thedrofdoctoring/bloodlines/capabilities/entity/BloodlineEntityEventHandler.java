package com.thedrofdoctoring.bloodlines.capabilities.entity;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

@EventBusSubscriber(modid = Bloodlines.MODID)
public class BloodlineEntityEventHandler {

    @SubscribeEvent
    public static void livingFallEvent(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player && Helper.isVampire(player)) {
            if (player.getBlockStateOn().is(BloodlinesTagsProviders.BloodlinesBlockTagProvider.ZEALOT_STONE) && VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_FALL_DAMAGE.get())) {
                event.setCanceled(true);
            }
        }
    }
}
