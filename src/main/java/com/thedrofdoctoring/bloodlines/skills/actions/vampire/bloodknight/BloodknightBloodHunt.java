package com.thedrofdoctoring.bloodlines.skills.actions.vampire.bloodknight;

import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BloodknightBloodHunt extends BloodknightBloodlineAction implements ILastingAction<IVampirePlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean activate(@NotNull IVampirePlayer vampire, ActivationContext context) {
        int blood = vampire.getBloodStats().getBloodLevel();
        int drain = CommonConfig.bloodknightBloodHuntBaseBloodCost.get();
        if(blood - drain <= 0) {
            vampire.asEntity().displayClientMessage(Component.translatable("skill.bloodlines.bloodknight_not_enough_blood").withStyle(ChatFormatting.DARK_RED), true);
            return false;
        }
        vampire.useBlood(drain, true);
        activate(vampire);
        return true;
    }

    protected void activate(@NotNull IVampirePlayer player) {
        player.asEntity().setInvisible(true);
    }


    @Override
    public void onActivatedClient(@NotNull IVampirePlayer vampire) {
        ((VampirePlayer) vampire).getSpecialAttributes().invisible = true;
    }

    @Override
    public void onDeactivated(@NotNull IVampirePlayer vampire) {
        vampire.asEntity().setInvisible(false);
        ((VampirePlayer) vampire).getSpecialAttributes().invisible = false;
    }

    @Override
    public void onReActivated(@NotNull IVampirePlayer vampire) {
        activate(vampire);
    }

    @Override
    public boolean onUpdate(IVampirePlayer vampire) {
        if(!vampire.isRemote() &&  vampire.asEntity().tickCount % CommonConfig.bloodknightBloodHuntTimePerBloodLoss.get() == 0) {
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

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.bloodknightBloodHuntCooldown.get() * 20;
    }
    @Override
    public int getDuration(IVampirePlayer iVampirePlayer) {
        return CommonConfig.bloodknightBloodHuntDuration.get() * 20;
    }
    public boolean showHudCooldown(Player player) {
        return true;
    }

    public boolean showHudDuration(Player player) {
        return true;
    }
}
