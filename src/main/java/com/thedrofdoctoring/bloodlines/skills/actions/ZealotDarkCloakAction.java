package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ZealotDarkCloakAction extends DefaultVampireAction implements ILastingAction<IVampirePlayer> {

    @Override
    public boolean activate(IVampirePlayer vampire, ActivationContext context) {
        if(!BloodlineHelper.lightMatches(CommonConfig.zealotDarkCloakLightLevel.get(), vampire.asEntity().getOnPos().above(), vampire.asEntity().level(), true)) {
            vampire.asEntity().displayClientMessage(Component.translatable("text.bloodlines.current_light").withStyle(ChatFormatting.DARK_PURPLE), true);
            return false;
        }
        activate(vampire);
        return true;
    }

    protected void activate(IVampirePlayer player) {
        player.asEntity().setInvisible(true);
    }   

    @Override
    public int getCooldown(IVampirePlayer player) {
        return CommonConfig.zealotDarkCloakCooldown.get() * 20;
    }

    @Override
    public int getDuration(IVampirePlayer player) {
        return Mth.clamp(CommonConfig.zealotDarkCloakDuration.get(), 10, Integer.MAX_VALUE / 20 - 1) * 20;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void onActivatedClient(IVampirePlayer vampire) {
        ((VampirePlayer) vampire).getSpecialAttributes().invisible = true;
    }

    @Override
    public void onDeactivated(IVampirePlayer vampire) {
        vampire.asEntity().setInvisible(false);
        ((VampirePlayer) vampire).getSpecialAttributes().invisible = false;
    }

    @Override
    public void onReActivated(IVampirePlayer vampire) {
        activate(vampire);
    }

    @Override
    public boolean onUpdate(IVampirePlayer vampire) {
        Player player = vampire.asEntity();
        if(!vampire.isRemote() && player.tickCount % 10 == 0 && !BloodlineHelper.lightMatches(CommonConfig.zealotDarkCloakLightLevel.get(), player.getOnPos().above(), player.level(), true)) {
            vampire.asEntity().displayClientMessage(Component.translatable("text.bloodlines.current_light").withStyle(ChatFormatting.DARK_PURPLE), true);
            return true;
        }

        if (player.isInvisible()) {
            player.setInvisible(true);
        }
        return false;
    }
}
