package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.actions.IActionHandler;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.event.ActionEvent;
import de.teamlapen.vampirism.entity.player.actions.ActionHandler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ActionHandler.class)
public abstract class ActionHandlerMixin<T extends IFactionPlayer<T>> implements IActionHandler<T> {

    @Shadow @Final private T player;

    @Inject(method = "updateActions", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap$Entry;setValue(I)I", ordinal = 2, shift = At.Shift.BY, by = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void updateActions(CallbackInfoReturnable<Boolean> cir, List toRemove, ObjectIterator var2, Object2IntMap.Entry entry, int newtimer, ResourceLocation id, ILastingAction<T> action, ActionEvent.ActionUpdateEvent event) {
        if(newtimer % 10 == 0) {
            if(player.getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_OBSCURED_POWER.get())) {
                Player player = event.getFactionPlayer().asEntity();
                if(BloodlineHelper.lightMatches(CommonConfig.zealotObscuredPowerLightLevel.get(), player.getOnPos().above(), player.getCommandSenderWorld(), true))
                    event.getFactionPlayer().getActionHandler().extendActionTimer((ILastingAction) action, CommonConfig.zealotObscuredPowerTimerIncrease.get());
            }
            if(player.getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_UNDERWATER_DURATION.get()) && player.asEntity().isUnderWater()) {
                event.getFactionPlayer().getActionHandler().extendActionTimer((ILastingAction) action, CommonConfig.ectothermUnderwaterDurationIncrease.get());
            }

        }
    }
}
