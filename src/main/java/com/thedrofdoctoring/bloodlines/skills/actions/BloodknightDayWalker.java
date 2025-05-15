package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BloodknightDayWalker extends DefaultVampireAction implements ILastingAction<IVampirePlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean activate(@NotNull IVampirePlayer vampire, ActivationContext context) {
        int blood = vampire.getBloodStats().getBloodLevel();
        int drain = CommonConfig.bloodknightDaywalkerBaseBloodCost.get();
        if (blood - drain <= 0) {
            vampire.asEntity().displayClientMessage(Component.translatable("skill.bloodlines.bloodknight_not_enough_blood").withStyle(ChatFormatting.DARK_RED), true);
            return false;
        }
        vampire.useBlood(drain, true);
        applyEffect(vampire);
        return true;
    }



    @Override
    public void onActivatedClient(@NotNull IVampirePlayer vampire) {
    }

    @Override
    public void onDeactivated(@NotNull IVampirePlayer vampire) {
        removePotionEffect(vampire, ModEffects.SUNSCREEN);
    }

    @Override
    public void onReActivated(@NotNull IVampirePlayer vampire) {
    }

    @Override
    public boolean onUpdate(IVampirePlayer vampire) {
        if (!vampire.isRemote() && vampire.asEntity().tickCount % 20 == 0) {
            applyEffect(vampire);
        }
        if (!vampire.isRemote() && vampire.asEntity().tickCount % CommonConfig.bloodknightDaywalkerTimePerBloodLoss.get() == 0) {
            int blood = vampire.getBloodStats().getBloodLevel();
            int drain = 2;
            vampire.useBlood(drain, true);
            if (blood - drain <= 0) {
                vampire.asEntity().displayClientMessage(Component.translatable("skill.bloodlines.bloodknight_not_enough_blood").withStyle(ChatFormatting.DARK_RED), true);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.bloodknightDaywalkerCooldown.get() * 20;
    }

    @Override
    public int getDuration(IVampirePlayer iVampirePlayer) {
        return CommonConfig.bloodknightDaywalkerDuration.get() * 20;
    }
    protected void applyEffect(IVampirePlayer vampire) {
        addEffectInstance(vampire, new MobEffectInstance(ModEffects.SUNSCREEN, 22, 3, false, false, false));
    }
    public boolean showHudCooldown(Player player) {
        return true;
    }

    public boolean showHudDuration(Player player) {
        return true;
    }
}
