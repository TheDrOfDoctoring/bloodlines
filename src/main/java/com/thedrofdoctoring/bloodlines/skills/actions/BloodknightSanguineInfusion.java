package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesEffects;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkill;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.config.VampirismConfig;
import de.teamlapen.vampirism.entity.player.skills.SkillHandler;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class BloodknightSanguineInfusion extends DefaultVampireAction implements ILastingAction<IVampirePlayer> {

    private static final ResourceLocation SPEED = Bloodlines.rl("sanguine_speed");
    private static final ResourceLocation JUMP_HEIGHT = Bloodlines.rl("sanguine_jump");
    private static final ResourceLocation SAFE_FALL = Bloodlines.rl("sanguine_safe_fall");
    private static final ResourceLocation STEP_ASSIST = Bloodlines.rl("sanguine_step_assist");
    private static final ResourceLocation HASTE = Bloodlines.rl("sanguine_haste");


    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean activate(IVampirePlayer iVampirePlayer, ActivationContext activationContext) {
        int blood = iVampirePlayer.getBloodStats().getBloodLevel();
        int drain = CommonConfig.bloodknightSanguineInfusionBaseBloodCost.get();
        if(blood - drain <= 0) {
            iVampirePlayer.asEntity().displayClientMessage(Component.translatable("skill.bloodlines.bloodknight_not_enough_blood").withStyle(ChatFormatting.DARK_RED), true);
            return false;
        }
        activate(iVampirePlayer);
        iVampirePlayer.useBlood(drain, true);
        return true;
    }
    private void activate(IVampirePlayer vampire) {
        Player player = vampire.asEntity();
        BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), SPEED);
        BloodlineManager.removeModifier(player.getAttribute(Attributes.JUMP_STRENGTH), JUMP_HEIGHT);
        BloodlineManager.removeModifier(player.getAttribute(Attributes.SAFE_FALL_DISTANCE), SAFE_FALL);
        BloodlineManager.removeModifier(player.getAttribute(Attributes.STEP_HEIGHT), STEP_ASSIST);
        BloodlineManager.removeModifier(player.getAttribute(Attributes.MINING_EFFICIENCY), HASTE);

        player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(SPEED, CommonConfig.bloodknightSanguineInfusionSpeedMult.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        player.getAttribute(Attributes.JUMP_STRENGTH).addPermanentModifier(new AttributeModifier(JUMP_HEIGHT, CommonConfig.bloodknightSanguineInfusionJumpHeightMult.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        player.getAttribute(Attributes.SAFE_FALL_DISTANCE).addPermanentModifier(new AttributeModifier(SAFE_FALL, 5, AttributeModifier.Operation.ADD_VALUE));
        ISkillHandler<IVampirePlayer> skill = vampire.getSkillHandler();
        if(skill.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_INFUSION_STEP_ASSIST)) {
            player.getAttribute(Attributes.STEP_HEIGHT).addPermanentModifier(new AttributeModifier(STEP_ASSIST, 0.75, AttributeModifier.Operation.ADD_VALUE));
        }
        if(skill.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_INFUSION_HASTE)) {
            player.getAttribute(Attributes.MINING_EFFICIENCY).addPermanentModifier(new AttributeModifier(HASTE, CommonConfig.bloodknightSanguineInfusionMiningSpeedMult.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        }
        VampirePlayer vp = (VampirePlayer) vampire;
        if(vp.getSpecialAttributes().bat && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.BLOODKNIGHT_BAT_FRENZY.get())) {
            player.getAbilities().setFlyingSpeed(VampirismConfig.BALANCE.vaBatFlightSpeed.get().floatValue() * CommonConfig.bloodknightBatSpeedMultiplier.get().floatValue());
        }
    }


    @Override
    public void onActivatedClient(IVampirePlayer iVampirePlayer) {

    }

    @Override
    public void onDeactivated(IVampirePlayer iVampirePlayer) {
        Player player = iVampirePlayer.asEntity();
        BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), SPEED);
        BloodlineManager.removeModifier(player.getAttribute(Attributes.JUMP_STRENGTH), JUMP_HEIGHT);
        BloodlineManager.removeModifier(player.getAttribute(Attributes.SAFE_FALL_DISTANCE), SAFE_FALL);
        BloodlineManager.removeModifier(player.getAttribute(Attributes.STEP_HEIGHT), STEP_ASSIST);
        BloodlineManager.removeModifier(player.getAttribute(Attributes.MINING_EFFICIENCY), HASTE);

        VampirePlayer vp = (VampirePlayer) iVampirePlayer;
        if(vp.getSpecialAttributes().bat && player.getEffect(BloodlinesEffects.BLOOD_FRENZY) == null) {
            player.getAbilities().setFlyingSpeed(VampirismConfig.BALANCE.vaBatFlightSpeed.get().floatValue());
        }
    }


    @Override
    public void onReActivated(IVampirePlayer iVampirePlayer) {
        activate(iVampirePlayer);
    }


    @Override
    public boolean onUpdate(IVampirePlayer vampire) {
        if(!vampire.isRemote() && vampire.asEntity().tickCount % CommonConfig.bloodknightSanguineInfusionTimePerBloodLoss.get() == 0) {
            int blood = vampire.getBloodStats().getBloodLevel();
            int drain = 2;
            vampire.useBlood(drain, true);
            if(blood - drain <= 0) {
                vampire.asEntity().displayClientMessage(Component.translatable("skill.bloodlines.bloodknight_not_enough_blood").withStyle(ChatFormatting.DARK_RED), true);
                return true;
            }
        }
        return false;
    }

    public boolean showHudCooldown(Player player) {
        return true;
    }

    public boolean showHudDuration(Player player) {
        return true;
    }
    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.bloodknightSanguineInfusionCooldown.get() * 20;
    }
    @Override
    public int getDuration(IVampirePlayer iVampirePlayer) {
        return CommonConfig.bloodknightSanguineInfusionDuration.get() * 20;
    }
}
