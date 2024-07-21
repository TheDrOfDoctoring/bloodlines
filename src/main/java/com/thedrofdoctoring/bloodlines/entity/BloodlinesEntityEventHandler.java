package com.thedrofdoctoring.bloodlines.entity;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = Bloodlines.MODID)
public class BloodlinesEntityEventHandler {

    @SubscribeEvent
    public static void onMobSpawn(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Villager villager) {
            villager.goalSelector.addGoal(villager.goalSelector.getAvailableGoals().size() + 1, new MesmeriseGoal(villager));
        }
    }
}
