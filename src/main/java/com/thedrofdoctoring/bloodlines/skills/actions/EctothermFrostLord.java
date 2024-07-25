package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.ISpecialAttributes;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.NotNull;

public class EctothermFrostLord extends DefaultVampireAction implements ILastingAction<IVampirePlayer> {

    private static final ResourceLocation damage = Bloodlines.rl("lord_of_frost_damage");
    public boolean activate(@NotNull IVampirePlayer vampire, IAction.ActivationContext context) {
        activate(vampire);
        return true;
    }

    private void activate(IVampirePlayer vamp) {
        if(vamp.getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_ICELORD.get())) {
            ISpecialAttributes specialAttributes = (ISpecialAttributes) ((VampirePlayer) vamp).getSpecialAttributes();
            specialAttributes.bloodlines$setIcePhasing(true);
        }
        BloodlineManager.removeModifier(vamp.asEntity().getAttribute(Attributes.ATTACK_DAMAGE), damage);
        int rank = BloodlineManager.get(vamp.asEntity()).getRank() - 1;
        if(rank + 1 >= CommonConfig.ectothermLordofFrostIncreasedDamageRank.get()) {
            vamp.asEntity().getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(damage, CommonConfig.ectothermLordOfFrostDamageMultiplier.get().get(rank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }


    public int getCooldown(IVampirePlayer player) {
        return CommonConfig.ectothermIceLordCooldown.get() * 20;
    }

    public int getDuration(@NotNull IVampirePlayer player) {
        int realRank = BloodlineHelper.getBloodlineRank(player.asEntity()) - 1;
        return CommonConfig.ectothermLordOfFrostDuration.get().get(realRank) * 20;
    }

    @Override
    public boolean canBeUsedBy(IVampirePlayer player) {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public void onActivatedClient(IVampirePlayer vampire) {
        activate(vampire);
    }

    public void onDeactivated(@NotNull IVampirePlayer vampire) {
        if(vampire.getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_ICELORD.get())) {
            ISpecialAttributes specialAttributes = (ISpecialAttributes) ((VampirePlayer) vampire).getSpecialAttributes();
            specialAttributes.bloodlines$setIcePhasing(false);
            specialAttributes.bloodlines$setInWall(false);
        }
        BloodlineManager.removeModifier(vampire.asEntity().getAttribute(Attributes.ATTACK_DAMAGE), damage);
    }

    public void onReActivated(IVampirePlayer vampire) {
        activate(vampire);
    }

    public boolean onUpdate(IVampirePlayer vampire) {
        return false;
    }

    public boolean showHudCooldown(Player player) {
        return true;
    }

    public boolean showHudDuration(Player player) {
        return true;
    }



}
