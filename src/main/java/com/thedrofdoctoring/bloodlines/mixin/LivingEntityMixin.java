package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.other.IPossessedEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IPossessedEntity {

    @Unique
    public boolean bloodlines$isPossessed;
    @Unique
    public Player bloodlines$possessedBy;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public Player bloodlines$getPossessedPlayer() {
        return bloodlines$possessedBy;
    }

    @Override
    public boolean bloodlines$isPossessed() {
        return bloodlines$isPossessed;
    }

    @Override
    public void bloodlines$setPossessed(Player player) {
        bloodlines$possessedBy = player;
        bloodlines$isPossessed = true;
        LivingEntity entity = (LivingEntity) (Object) this;
        if(entity instanceof Mob mob) {
            mob.setNoAi(true);
        }
    }

    @Override
    public void bloodlines$clearPossession() {
        bloodlines$isPossessed = false;
        bloodlines$possessedBy = null;
        LivingEntity entity = (LivingEntity) (Object) this;
        if(entity instanceof Mob mob) {
            mob.setNoAi(false);
        }
    }


}
