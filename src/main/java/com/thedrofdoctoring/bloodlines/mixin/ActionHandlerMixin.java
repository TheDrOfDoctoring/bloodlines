package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
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
@SuppressWarnings("rawtypes")
@Mixin(ActionHandler.class)
public abstract class ActionHandlerMixin<T extends IFactionPlayer<T>> implements IActionHandler<T> {

    @Shadow @Final private T player;

    @Inject(method = "updateActions", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap$Entry;setValue(I)I", ordinal = 2, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
    private void updateActions(CallbackInfoReturnable<Boolean> cir, List toRemove, ObjectIterator var2, Object2IntMap.Entry entry, int newtimer, ResourceLocation id, ILastingAction<T> action, ActionEvent.ActionUpdateEvent event) {
        if(newtimer % 10 == 0) {
            if(player.getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_OBSCURED_POWER.get())) {
                Player player = event.getFactionPlayer().asEntity();
                if(BloodlineHelper.lightMatches(CommonConfig.zealotObscuredPowerLightLevel.get(), player.getOnPos().above(), player.getCommandSenderWorld(), true))
                    this.extendActionTimer(action, CommonConfig.zealotObscuredPowerTimerIncrease.get() + 1);
            }
            if(player.getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_UNDERWATER_DURATION.get()) && player.asEntity().isUnderWater()) {
                if(action != BloodlineActions.ECTOTHERM_DOLPHIN_LEAP_ACTION.get()) {
                    this.extendActionTimer(action, CommonConfig.ectothermUnderwaterDurationIncrease.get() + 1);
                }
            }

        }
    }
}
