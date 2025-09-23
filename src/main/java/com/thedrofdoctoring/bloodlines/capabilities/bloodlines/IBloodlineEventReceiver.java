package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

public interface IBloodlineEventReceiver {

    default void tick(Player blPlayer) {}

    default void onCrit(CriticalHitEvent event) {}

    default void onLivingDeath(LivingDeathEvent event) {}

    /**
     * @param blRank - Stored Bloodline Rank - 1, for array indexing.
     */

    default void onReceiveDamage(LivingIncomingDamageEvent event, LivingEntity bloodlineMember, int blRank) {}
    /**
     * @param blRank - Stored Bloodline Rank - 1, for array indexing.
     */
    default void onDealDamage(LivingIncomingDamageEvent event, LivingEntity bloodlineMember, LivingEntity victim, int blRank) {}
}
