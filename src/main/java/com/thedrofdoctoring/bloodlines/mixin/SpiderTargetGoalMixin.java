package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.entity.ZealotTargetGoalModifier;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.mixin.accessor.TargetConditionAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Predicate;

@Mixin(Spider.SpiderTargetGoal.class)
public abstract class SpiderTargetGoalMixin<T extends LivingEntity> extends NearestAttackableTargetGoal<T> implements ZealotTargetGoalModifier {
    @Unique
    private static final Predicate<LivingEntity> checkZealotSkill = (entity) -> {
        return !(entity instanceof Player player && VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_SPIDER_FRIEND));
    };

    public SpiderTargetGoalMixin(Mob p_26060_, Class p_26061_, boolean p_26062_) {
        super(p_26060_, p_26061_, p_26062_);
    }

    @Override
    public void ignoreZealotSpiderFriend() {
        Predicate<LivingEntity> predicate = checkZealotSkill;
        if (((TargetConditionAccessor) this.targetConditions).getSelector() != null) {
            predicate = predicate.and(((TargetConditionAccessor) this.targetConditions).getSelector());
        }
        this.targetConditions.selector(predicate);

    }
}
