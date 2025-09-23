package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineEventReceiver;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesEffects;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.vampire.DrinkBloodContext;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BloodlineBloodknight extends VampireBloodline implements IBloodlineEventReceiver {
    public static final ResourceLocation BLOOD_KNIGHT = Bloodlines.rl("bloodknight");

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, LivingEntity entity, boolean cleanup) {
        int realRank = rank - 1;
        Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();
        attributes.put(ModAttributes.BLOOD_EXHAUSTION, new AttributeModifier(Bloodlines.rl("bloodknight_exhaustion_decrease"), CommonConfig.bloodknightBloodThirstChange.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Bloodlines.rl("bloodknight_damage_increase"), CommonConfig.bloodknightDamageIncrease.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));


        return attributes;
    }


    @Override
    public ResourceLocation getBloodlineId() {
        return BLOOD_KNIGHT;
    }

    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.BLOOD_KNIGHT;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return CommonConfig.bloodknightDefaults;
    }

    @Override
    public String getName() {
        return "Bloodknight";
    }

    @Override
    public void onCrit(CriticalHitEvent event) {
        if(event.isVanillaCritical()) {
            VampirePlayer vp = VampirePlayer.get(event.getEntity());
            if(vp.asEntity().isCrouching() && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.BLOODKNIGHT_HIDDEN_STRIKE) && vp.getActionHandler().isActionActive(BloodlineActions.BLOODKNIGHT_BLOOD_HUNT.get())) {
                if(event.getTarget() instanceof Player player) {
                    int slownessDuration = CommonConfig.bloodknightHiddenStrikeSlownessDurationPlayer.get();
                    int weaknessDuration = CommonConfig.bloodknightHiddenStrikeWeaknessDurationPlayer.get();
                    player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, weaknessDuration, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slownessDuration, 0));


                } else if(event.getTarget() instanceof LivingEntity living) {
                    int slownessDuration = CommonConfig.bloodknightHiddenStrikeSlownessDurationMob.get();
                    int weaknessDuration = CommonConfig.bloodknightHiddenStrikeWeaknessDurationMob.get();
                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, weaknessDuration, 0));
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slownessDuration, 0));

                }
                vp.getActionHandler().deactivateAction(BloodlineActions.BLOODKNIGHT_BLOOD_HUNT.get());
            }
            if(vp.getSkillHandler().isSkillEnabled(BloodlineSkills.BLOODKNIGHT_SAPPING_STRIKE) && event.getTarget() instanceof LivingEntity living && Helper.isVampire(living)) {
                float saturation = CommonConfig.bloodknightSappingStrikeSaturation.get().floatValue();
                DrinkBloodContext emptyCon = DrinkBloodContext.none();
                if(living instanceof Player player) {
                    VampirePlayer targetVP = VampirePlayer.get(player);
                    int amt = CommonConfig.bloodknightSappingStrikePlayerDrain.get();
                    targetVP.useBlood(amt, true);
                    vp.drinkBlood(amt, saturation, emptyCon);
                    addFrenzy(vp);
                } else {
                    @NotNull Optional<ExtendedCreature> c = ExtendedCreature.getSafe(living);
                    if(c.isPresent()) {
                        ExtendedCreature creature = c.get();
                        int amt = CommonConfig.bloodknightSappingStrikeMobDrain.get();
                        creature.setBlood(creature.getBlood() - amt);
                        vp.drinkBlood(amt, saturation, emptyCon);
                        addFrenzy(vp);
                    }

                }
            }
        }
    }
    private static void addFrenzy(VampirePlayer vp) {
        ISkillHandler<IVampirePlayer> skillHandler = vp.getSkillHandler();
        Player player = vp.asEntity();

        int duration = 30 * 20;

        if(skillHandler.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEEDING_FRENZY_1.get()) && !skillHandler.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEEDING_FRENZY_2.get())) {
            player.addEffect(new MobEffectInstance(BloodlinesEffects.BLOOD_FRENZY, duration, 0, false, true, true));
        } else if(skillHandler.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEEDING_FRENZY_2.get())) {
            player.addEffect(new MobEffectInstance(BloodlinesEffects.BLOOD_FRENZY, duration, 1, false, true, true));
        }

    }

    @Override
    public void onDealDamage(LivingIncomingDamageEvent event, LivingEntity bloodlineMember, LivingEntity victim, int blRank) {
        if(Helper.isVampire(victim)) {
            event.setAmount(event.getAmount() * CommonConfig.bloodknightVampireDamageMult.get().get(blRank).floatValue());
        }
    }

    @Override
    public void onReceiveDamage(LivingIncomingDamageEvent event, LivingEntity bloodlineMember, int blRank) {
        if(event.getSource().getEntity() instanceof LivingEntity entity && Helper.isHunter(entity)) {
            event.setAmount(Math.min(Float.MAX_VALUE, event.getAmount() * CommonConfig.bloodknightHunterDamageMult.get().get(blRank).floatValue()));
        }
    }

    @Override
    public void onBloodDrink(BloodDrinkEvent.PlayerDrinkBloodEvent event, int rank, VampirePlayer vp) {

        if(event.getBloodSource().getStack().isPresent()) {
            List<Item> allowed = List.of(ModItems.VAMPIRE_BLOOD_BOTTLE.get(), ModItems.BLOOD_BOTTLE.get());
            ItemStack stack = event.getBloodSource().getStack().get();
            if(!allowed.contains(stack.getItem())) {
                event.setAmount((int) (event.getAmount() * CommonConfig.bloodknightOtherSourceBloodDecrease.get().get(rank).floatValue()));
                event.setSaturationModifier(event.getSaturation() * CommonConfig.bloodknightOtherSourceBloodDecrease.get().get(rank).floatValue());
            }
            if(stack.is(ModItems.VAMPIRE_BLOOD_BOTTLE.get()) &&  CommonConfig.bloodBottleFrenzy.get()) {
                int duration = CommonConfig.bloodBottleFrenzyDuration.get().get(rank) * 20;
                ISkillHandler<IVampirePlayer> skillHandler = vp.getSkillHandler();
                Player player = vp.asEntity();
                if(skillHandler.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEEDING_FRENZY_1.get()) && !skillHandler.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEEDING_FRENZY_2.get())) {
                    player.addEffect(new MobEffectInstance(BloodlinesEffects.BLOOD_FRENZY, duration, 0, false, true, true));
                } else if(skillHandler.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEEDING_FRENZY_2.get())) {
                    player.addEffect(new MobEffectInstance(BloodlinesEffects.BLOOD_FRENZY, duration, 1, false, true, true));
                }
            }
        }
        if(event.getBloodSource().getEntity().isPresent()) {
            LivingEntity entity = event.getBloodSource().getEntity().get();
            if(!Helper.isVampire(entity)) {
                event.setAmount((int) (event.getAmount() * CommonConfig.bloodknightOtherSourceBloodDecrease.get().get(rank).floatValue()));
                event.setSaturationModifier(event.getSaturation() * CommonConfig.bloodknightOtherSourceBloodDecrease.get().get(rank).floatValue());
            } else {
                ISkillHandler<IVampirePlayer> skillHandler = vp.getSkillHandler();
                Player player = vp.asEntity();

                int duration = CommonConfig.bloodknightBloodFrenzyDurationPerRank.get().get(rank) * 20;
                if(skillHandler.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEEDING_FRENZY_1.get()) && !skillHandler.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEEDING_FRENZY_2.get())) {
                    player.addEffect(new MobEffectInstance(BloodlinesEffects.BLOOD_FRENZY, duration, 0, false, true, true));
                } else if(skillHandler.isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEEDING_FRENZY_2.get())) {
                    player.addEffect(new MobEffectInstance(BloodlinesEffects.BLOOD_FRENZY, duration, 1, false, true, true));
                }
                if(!(entity instanceof Player)) {
                    event.setAmount((int) (event.getAmount() * CommonConfig.bloodknightVampireBonusBloodMult.get().get(rank).floatValue()));
                    event.setSaturationModifier(event.getSaturation() * CommonConfig.bloodknightVampireBonusSaturationMult.get().get(rank).floatValue());
                }

            }
        }
    }
}
