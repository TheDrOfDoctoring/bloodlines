package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesAttachments;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.core.ModRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.Objects;

public class GraveboundMistFormAction extends DefaultGraveboundAction implements ILastingAction<IHunterPlayer> {


    /**
     * Based on Vampirism's Bat Mode Action
     */

    public final static float MISTFORM_EYE_HEIGHT = 0.3125F * 0.8f;
    public static final EntityDimensions MISTFORM_SIZE = EntityDimensions.fixed(0.3125F, 0.3125F).withEyeHeight(MISTFORM_EYE_HEIGHT);

    private static final float PLAYER_HEIGHT = 1.8F;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean canBeUsedBy(IHunterPlayer player) {
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player.asEntity());
        return atts.getGraveboundData().mistForm;
    }

    /**
     *
     * @param blRank - Given rank must be between 0 and 3. (Stored Rank - 1)
     */
    public static int getRequiredSouls(Player player, int blRank) {

        if(BloodlinesPlayerAttributes.get(player).getGraveboundData().cheaperResurrection) {
            return HunterBloodlinesConfig.reducedMistFormSoulRequirement.get().get(blRank);
        }

        return HunterBloodlinesConfig.mistFormSoulRequirement.get().get(blRank);
    }

    private static void setMistform(IHunterPlayer factionPlayer, boolean mistForm, boolean sync) {
        Player player = factionPlayer.asEntity();
        BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(player);

        if(state != null && sync) {
            BloodlinesPlayerAttributes.get(player).getGraveboundData().mistForm = mistForm;
            state.setMistForm(mistForm);
            BloodlineManager.get(player).sync(true);
        }

        player.setForcedPose(mistForm ? Pose.CROUCHING : null);
        player.refreshDimensions();

        if(mistForm) {
            player.setPos(player.getX(), player.getY() + (PLAYER_HEIGHT - MISTFORM_SIZE.height()), player.getZ());
        } else {
            BloodlinesPlayerAttributes.get(player).getGraveboundData().lastDamageSource = null;
        }
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.35f, 1);
        ModParticles.spawnParticlesServer(player.level(), ParticleTypes.SOUL, player.getX(), player.getY(), player.getZ(), 50, 0f, 0.5f, 0f, 0.1f);


    }
    private void setAttributes(Player player, boolean mistForm) {
        ResourceLocation key = ModRegistries.ACTIONS.getKey(this);
        if (key == null) {
            return;
        }
        if (mistForm) {
            AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
            if (armor != null && !armor.hasModifier(key)) {
                armor.addPermanentModifier(new AttributeModifier(key, -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
            AttributeInstance armorToughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
            if (armorToughness != null && !armorToughness.hasModifier(key)) {
                armorToughness.addPermanentModifier(new AttributeModifier(key, -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
            AttributeInstance fly = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
            if (fly != null && !fly.hasModifier(key)) {
                fly.addPermanentModifier(new AttributeModifier(key, 1, AttributeModifier.Operation.ADD_VALUE));
            }
            AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
            if (health != null && !health.hasModifier(key)) {
                health.addPermanentModifier(new AttributeModifier(key, -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }

            player.getAbilities().setFlyingSpeed(HunterBloodlinesConfig.mistFormFlightSpeed.get().floatValue());
        } else {
            Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).removeModifier(key);
            Objects.requireNonNull(player.getAttribute(Attributes.ARMOR_TOUGHNESS)).removeModifier(key);
            Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).removeModifier(key);
            Objects.requireNonNull(player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)).removeModifier(key);

            player.getAbilities().setFlyingSpeed(0.05F);
        }
        player.onUpdateAbilities();

    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {

        setMistform(iHunterPlayer, true, true);
        setAttributes(iHunterPlayer.asEntity(), true);
        return true;
    }





    @Override
    public void onActivatedClient(IHunterPlayer iHunterPlayer) {
        if(!BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().mistForm) {
            setMistform(iHunterPlayer, true, false);
            setAttributes(iHunterPlayer.asEntity(), true);

        }
    }

    @Override
    public void onDeactivated(IHunterPlayer iHunterPlayer) {
        Player player = iHunterPlayer.asEntity();
        BloodlineManager manager = BloodlineManager.get(player);
        BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(player);
        player.removeData(BloodlinesAttachments.MIST_FORM);
        int requiredSouls = getRequiredSouls(player, manager.getRank() - 1);
        if(state != null && state.getSouls() > requiredSouls) {
            state.addSouls(-requiredSouls);
            setMistform(iHunterPlayer, false, true);
            setAttributes(iHunterPlayer.asEntity(), false);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.35f, 1);
            ModParticles.spawnParticlesServer(player.level(), ParticleTypes.SOUL, player.getX(), player.getY(), player.getZ(), 50, 0f, 0.5f, 0f, 0.1f);
        } else {

            DamageSource killingDamageSource = BloodlinesPlayerAttributes.get(player).getGraveboundData().lastDamageSource;
            BloodlinesPlayerAttributes.get(player).getGraveboundData().mistForm = false;
            if(killingDamageSource != null) {
                player.hurt(killingDamageSource, 1000f);
            } else {
                player.kill();
            }

        }

    }

    @Override
    public void onReActivated(IHunterPlayer iHunterPlayer) {
        setMistform(iHunterPlayer, true, false);
        setAttributes(iHunterPlayer.asEntity(), true);

    }

    @Override
    public int getDuration(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.mistFormDuration.get() * 20;
    }

    @Override
    public boolean onUpdate(IHunterPlayer iHunterPlayer) {
        if(iHunterPlayer.asEntity().tickCount % 10 == 0) {
            if(!BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().mistForm) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean showInSelectAction(Player player) {
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
        return atts.getGraveboundData().mistForm;
    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {

        if(BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().fasterResurrection) {
            return HunterBloodlinesConfig.reducedMistFormCooldown.get() * 20;
        }

        return HunterBloodlinesConfig.mistFormCooldown.get() * 20;
    }
    public boolean showHudCooldown(Player player) {
        return true;
    }
}
