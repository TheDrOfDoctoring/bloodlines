package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.entity.BloodlineMobManager;
import de.teamlapen.vampirism.api.entity.IEntityLeader;
import de.teamlapen.vampirism.entity.ai.goals.FollowAdvancedVampireGoal;
import de.teamlapen.vampirism.entity.vampire.BasicVampireEntity;
import de.teamlapen.vampirism.entity.vampire.VampireBaseEntity;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FollowAdvancedVampireGoal.class)
public class FollowAdvancedVampireGoalMixin {

    @Shadow @Final protected BasicVampireEntity entity;



    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true, remap = false)
    private void setCanUse(CallbackInfoReturnable<Boolean> cir) {
        BloodlineMobManager manager = BloodlineMobManager.get(entity);
        if(manager.getBloodline() != null) {
            IEntityLeader leader = entity.getAdvancedLeader();
            int bloodlines$DIST = 20;
            if (leader != null) {
                if(leader instanceof PathfinderMob mob) {
                    BloodlineMobManager leaderManager = BloodlineMobManager.get(mob);
                    IBloodline leaderBl = leaderManager.getBloodline();
                    if(leaderBl != manager.getBloodline()) {
                        entity.setAdvancedLeader(null);
                        cir.setReturnValue(false);
                    }

                } else {
                    cir.setReturnValue(leader.getRepresentingEntity().isAlive() && this.entity.distanceToSqr(leader.getRepresentingEntity()) > bloodlines$DIST);
                }
            }

            List<VampireBaseEntity> list = this.entity.getCommandSenderWorld().getEntitiesOfClass(VampireBaseEntity.class, this.entity.getBoundingBox().inflate(8, 4, 8), IEntityLeader.class::isInstance);


            int d0 = 1;

            for (VampireBaseEntity entity1 : list) {
                IEntityLeader leader1 = ((IEntityLeader) entity1);
                BloodlineMobManager leaderManager = BloodlineMobManager.get(entity1);
                IBloodline leaderBl = leaderManager.getBloodline();
                if (leaderBl == manager.getBloodline() && entity1.isAlive() && leader1.getFollowingCount() < leader1.getMaxFollowerCount()) {
                    int d1 = leaderManager.getRank();

                    if (d1 >= d0) {
                        d0 = d1;
                        leader = leader1;
                    }
                }
            }

            if (leader == null) {
                cir.setReturnValue(false);
            } else {
                entity.setAdvancedLeader(leader);
                leader.increaseFollowerCount();
                cir.setReturnValue(this.entity.distanceToSqr(leader.getRepresentingEntity()) > bloodlines$DIST);
            }
            cir.setReturnValue(false);
        }

    }

}
