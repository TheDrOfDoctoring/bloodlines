package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.core.BloodlineAttachments;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import de.teamlapen.vampirism.entity.vampire.BasicVampireEntity;
import de.teamlapen.vampirism.entity.vampire.VampireBaseEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BasicVampireEntity.class)
public class BasicVampireEntityMixin extends VampireBaseEntity {

    public BasicVampireEntityMixin(EntityType<? extends VampireBaseEntity> type, Level world, boolean countAsMonsterForSpawn) {
        super(type, world, countAsMonsterForSpawn);
    }

    @ModifyReturnValue(method = "wantsBlood", at = @At("RETURN"))
    public boolean wantsBloodMod(boolean original) {
        return original || this.getData(BloodlineAttachments.VAMP_EXTENDED_CREATURE.get()).getBlood() <= 53;
    }

}
