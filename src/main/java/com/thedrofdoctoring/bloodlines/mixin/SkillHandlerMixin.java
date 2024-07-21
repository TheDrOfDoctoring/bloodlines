package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkill;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkillType;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.IBloodlineSkill;
import de.teamlapen.vampirism.VampirismMod;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.entity.player.skills.SkillHandler;
import de.teamlapen.vampirism.entity.player.skills.SkillNode;
import de.teamlapen.vampirism.util.RegUtil;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SkillHandler.class)
public abstract class SkillHandlerMixin<T extends IFactionPlayer<T>> implements ISkillHandler<T> {

    //Bloodlines could probably have their own unique skillhandler, but this works for now.
    @Shadow
    @Final
    private T player;

    @Shadow public abstract boolean isSkillEnabled(ISkill<?> skill);

    @Inject(method = "canSkillBeEnabled", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private void preventBloodlineManualEnable(@NotNull ISkill<T> skill, CallbackInfoReturnable<ISkillHandler.@NotNull Result> cir) {
        if (skill instanceof IBloodlineSkill blSkill) {
                BloodlineManager bl = BloodlineManager.get(player.asEntity());

                if((skill == BloodlineSkills.ECTOTHERM_SLOWNESS_ATTACK.get() || skill == BloodlineSkills.ECTOTHERM_FROZEN_ATTACK.get()) && !this.isSkillEnabled(BloodlineSkills.ECTOTHERM_ICELORD)) {
                    cir.setReturnValue(Result.PARENT_NOT_ENABLED);
                }

                if(bl.getSkillHandler().getRemainingSkillPoints() == 0 && blSkill.requiresBloodlineSkillPoints()) {
                    cir.setReturnValue(Result.NO_POINTS);
                }

                if (CommonConfig.defaultNotManuallyUnlockable.get() && bl.getBloodline() != null) {
                    ModConfigSpec.ConfigValue<List<? extends String>>[] defaultSkills = bl.getBloodline().getDefaultEnabledSkills();
                    for (ModConfigSpec.ConfigValue<List<? extends String>> defaultSkill : defaultSkills) {
                        defaultSkill.get().forEach(str -> {
                            ResourceLocation skillRl = ResourceLocation.parse(str);
                            ISkill<?> iSkill = RegUtil.getSkill(skillRl);
                            if (skill == iSkill) {
                                cir.setReturnValue(Result.OTHER_NODE_SKILL);
                            }

                        });
                    }
                }
        }
    }
    @Inject(method = "enableSkill", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"), remap = false)
    private void checkBloodlineSkillEnables(@NotNull ISkill<T> skill, boolean fromLoading, CallbackInfo ci) {
        if(skill instanceof IBloodlineSkill blSkill) {
            BloodlineManager.getOpt(player.getRepresentingPlayer()).ifPresent(bl -> {
                if (!fromLoading && bl.getBloodline() != null && player.getRepresentingPlayer().tickCount > 1) {
                    bl.updateAttributes(bl.getBloodline());
                    if(blSkill.requiresBloodlineSkillPoints()) {
                        bl.getSkillHandler().setEnabledSkills(bl.getSkillHandler().getEnabledSkills() + 1);
                        bl.sync(true);
                    }
                }
            });
        }
    }
    @Inject(method = "disableSkill", at = @At(value = "INVOKE", target = "Lde/teamlapen/vampirism/api/entity/player/skills/ISkill;onDisable(Lde/teamlapen/vampirism/api/entity/player/IFactionPlayer;)V"), remap = false)
    private void checkBloodlineSkillDisables(@NotNull ISkill<T> skill, CallbackInfo ci) {
        if (skill instanceof IBloodlineSkill blSkill) {
            BloodlineManager.getOpt(player.getRepresentingPlayer()).ifPresent(bl -> {
                if (bl.getBloodline() != null && player.getRepresentingPlayer().tickCount > 1) {
                    bl.updateAttributes(bl.getBloodline());
                    if(blSkill.requiresBloodlineSkillPoints()) {
                        bl.getSkillHandler().setEnabledSkills(bl.getSkillHandler().getEnabledSkills() - 1);
                        bl.sync(true);
                    }
                }
            });
        }
    }
}
