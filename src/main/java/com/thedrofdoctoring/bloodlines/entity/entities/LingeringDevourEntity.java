package com.thedrofdoctoring.bloodlines.entity.entities;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LingeringDevourEntity extends AreaEffectCloud {
    public LingeringDevourEntity(Level pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    public LingeringDevourEntity(EntityType<? extends LingeringDevourEntity> entityEntityType, Level level) {
        super(entityEntityType, level);
    }


    @Override
    public void tick() {
        super.tick();
        if(this.getOwner() != null && !this.victims.isEmpty() && this.getOwner() instanceof Player owner) {
            IBloodline bloodline = BloodlinesPlayerAttributes.get(owner).bloodline;

            if(bloodline instanceof BloodlineGravebound gravebound) {
                this.victims.forEach((entity, i) -> {

                    if(owner != entity && gravebound.canDevour(entity, owner, true, false)) {
                        gravebound.devour((LivingEntity) entity, owner);
                    }

                });
            }
        }

    }
}
