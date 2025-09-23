package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.entity.GraveboundTargetModifier;
import de.teamlapen.vampirism.mixin.accessor.TargetConditionAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Predicate;

@Mixin(NearestAttackableTargetGoal.class)
public class NearestAttackableTargetGoalMixin implements GraveboundTargetModifier {

    @Shadow
    protected TargetingConditions targetConditions;
    @Unique
    private static final Predicate<LivingEntity> bloodlines$nonGraveboundCheck = entity -> entity instanceof Player player && !BloodlinesPlayerAttributes.get(player).getGraveboundData().undeadLord;

    @Override
    public void bloodlines$ignoreGravebound() {
        Predicate<LivingEntity> predicate = bloodlines$nonGraveboundCheck;
        if (((TargetConditionAccessor) this.targetConditions).getSelector() != null) {
            predicate = predicate.and(((TargetConditionAccessor) this.targetConditions).getSelector());
        }
        this.targetConditions.selector(predicate);
    }
}
