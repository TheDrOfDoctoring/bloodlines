package com.thedrofdoctoring.bloodlines.entity;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

//cant really extend TemptGoal annoyingly
public class MesmeriseGoal extends Goal {
    private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(15.0D).ignoreLineOfSight();
    private final TargetingConditions targetingConditions;
    protected final PathfinderMob mob;
    private Player player;
    private double px;
    private double py;
    private double pz;
    private double pRotX;
    private double pRotY;
    public MesmeriseGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);
    }
    private boolean shouldFollow(LivingEntity mob) {
        //not too bad i would hope
        if(mob instanceof Player p && Helper.isVampire(mob)) {
            IVampSpecialAttributes specialAttributes = (IVampSpecialAttributes) VampirePlayer.get(p).getSpecialAttributes();
            return specialAttributes.bloodlines$getMesmerise();
        }
        return false;
    }

    @Override
    public boolean canUse() {
        this.player = this.mob.level().getNearestPlayer(this.targetingConditions, this.mob);
        return this.player != null;
    }
    public boolean canContinueToUse() {
            if (this.mob.distanceToSqr(this.player) < 40.0D) {
                if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.01D) {
                    return false;
                }

                if (Math.abs((double)this.player.getXRot() - this.pRotX) > 5.0D || Math.abs((double)this.player.getYRot() - this.pRotY) > 5.0D) {
                    return false;
                }
            } else {
                this.px = this.player.getX();
                this.py = this.player.getY();
                this.pz = this.player.getZ();
            }

            this.pRotX = this.player.getXRot();
            this.pRotY = this.player.getYRot();

        return this.canUse();
    }


    public void start() {
        this.px = this.player.getX();
        this.py = this.player.getY();
        this.pz = this.player.getZ();
    }

    public void stop() {
        this.player = null;
        this.mob.getNavigation().stop();
    }

    public void tick() {
        this.mob.getLookControl().setLookAt(this.player, (float)(this.mob.getMaxHeadYRot() + 20), (float)this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.player) < 2.25D) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.player, 0.6D);
        }

    }
}
