package com.thedrofdoctoring.bloodlines.capabilities.other;

import net.minecraft.world.entity.player.Player;

public interface IPossessedEntity {
    Player bloodlines$getPossessedPlayer();
    boolean bloodlines$isPossessed();
    void bloodlines$setPossessed(Player player);

    void bloodlines$clearPossession();

}
