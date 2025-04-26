package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class NobleLeechingAction extends DefaultVampireAction implements ILastingAction<IVampirePlayer> {
    @Override
    public boolean isEnabled() {
        return CommonConfig.leechingEnabled.get();
    }


    public boolean activate(@NotNull IVampirePlayer vampire, IAction.ActivationContext context) {
        activate(vampire);
        return true;
    }
    private void activate(IVampirePlayer vamp) {
        BloodlineManager.removeModifier(vamp.asEntity().getAttribute(ModAttributes.BLOOD_EXHAUSTION), Bloodlines.rl("noble_leeching_exhaustion"));
        IVampSpecialAttributes specialAttributes = (IVampSpecialAttributes) ((VampirePlayer) vamp).getSpecialAttributes();
        int leeching = vamp.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_ENHANCED_LEECHING) ? 2 : 1;
        specialAttributes.bloodlines$setLeeching(leeching);
        if(!vamp.isRemote()) {
            vamp.asEntity().getAttribute(ModAttributes.BLOOD_EXHAUSTION).addPermanentModifier(new AttributeModifier(Bloodlines.rl("noble_leeching_exhaustion"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }
    public boolean showHudCooldown(Player player) {
        return true;
    }

    public boolean showHudDuration(Player player) {
        return true;
    }
    @Override
    public int getDuration(IVampirePlayer iVampirePlayer) {
        return CommonConfig.leechingDuration.get() * 20;
    }
    @Override
    public void onActivatedClient(IVampirePlayer vampire) {
        activate(vampire);
    }
    @Override
    public void onDeactivated(@NotNull IVampirePlayer vampire) {
        BloodlineManager.removeModifier(vampire.asEntity().getAttribute(ModAttributes.BLOOD_EXHAUSTION), Bloodlines.rl("noble_leeching_exhaustion"));
        IVampSpecialAttributes specialAttributes = (IVampSpecialAttributes) ((VampirePlayer) vampire).getSpecialAttributes();
        specialAttributes.bloodlines$setLeeching(0);
    }
    @Override
    public void onReActivated(IVampirePlayer vampire) {
        activate(vampire);
    }

    @Override
    public boolean onUpdate(IVampirePlayer iVampirePlayer) {
        return false;
    }

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.leechingCooldown.get();
    }

}
