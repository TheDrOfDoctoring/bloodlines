package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.capabilities.other.VampExtendedCreature;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import de.teamlapen.vampirism.entity.VampirismEntity;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BloodknightBloodExtraction extends DefaultVampireAction {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected boolean activate(IVampirePlayer iVampirePlayer, ActivationContext activationContext) {
        ItemStack stack = iVampirePlayer.asEntity().getOffhandItem();
        Item offhand = iVampirePlayer.asEntity().getOffhandItem().getItem();
        if(offhand != Items.GLASS_BOTTLE) {
            iVampirePlayer.asEntity().displayClientMessage(Component.translatable("bloodlines.blood_extraction.no_bottle").withStyle(ChatFormatting.DARK_RED), true);
            return false;
        }
        Optional<Entity> entity = activationContext.targetEntity();
        if(entity.isPresent() && entity.get() instanceof VampirismEntity ve) {
            @NotNull Optional<ExtendedCreature> creature = ExtendedCreature.getSafe(ve);
            if(Helper.isVampire(ve) && creature.isPresent()) {
                ExtendedCreature ec = creature.get();
                if(!ec.canBeBitten(iVampirePlayer)) return false;
                stack.shrink(1);
                ec.setBlood(ec.getBlood() - 3);
                iVampirePlayer.asEntity().addItem(new ItemStack(ModItems.VAMPIRE_BLOOD_BOTTLE.get()));
                return true;
            }
        }

        return false;
    }

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.bloodknightBloodExtractionCooldown.get() * 20;
    }
}
