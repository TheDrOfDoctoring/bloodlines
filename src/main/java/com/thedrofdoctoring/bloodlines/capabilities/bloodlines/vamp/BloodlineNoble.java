package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineEventReceiver;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineManager;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesStats;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.core.ModDamageTypes;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.vampire.DrinkBloodContext;
import de.teamlapen.vampirism.items.VampirismItemBloodFoodItem;
import de.teamlapen.vampirism.util.Helper;
import de.teamlapen.vampirism.world.LevelFog;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodlineNoble extends VampireBloodline implements IBloodlineEventReceiver {
    public static final ResourceLocation NOBLE = Bloodlines.rl("noble");
    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, LivingEntity entity, boolean cleanup) {
        int realRank = rank - 1;
        Map<Holder<Attribute>, AttributeModifier> attributes = new HashMap<>();
        attributes.put(Attributes.ATTACK_SPEED, new AttributeModifier(Bloodlines.rl("noble_attack_speed_multiplier"), CommonConfig.nobleAttackSpeedIncrease.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attributes.put(Attributes.MAX_HEALTH, new AttributeModifier(Bloodlines.rl("noble_health_modifier"), CommonConfig.nobleMaxHealthChange.get().get(realRank), AttributeModifier.Operation.ADD_VALUE));
        attributes.put(ModAttributes.BLOOD_EXHAUSTION, new AttributeModifier(Bloodlines.rl("noble_exhaustion_decrease"), CommonConfig.nobleBloodThirstMult.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attributes.put(ModAttributes.NEONATAL_DURATION, new AttributeModifier(Bloodlines.rl("noble_neonatal_modifier"), CommonConfig.nobleNeonatalMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        if(entity instanceof Player player) {
            ISkillHandler<IVampirePlayer> skillHandler =  this.getSkillHandler(player);
            applyConditionalModifier(attributes, BloodlineSkills.NOBLE_FASTER_RESURRECT.get(), ModAttributes.DBNO_DURATION, new AttributeModifier(Bloodlines.rl("noble_resurrection_modifier"), CommonConfig.nobleFasterResurrectionMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), skillHandler, cleanup);
            applyConditionalModifier(attributes, BloodlineSkills.NOBLE_FASTER_MOVEMENT_SPEED.get(), Attributes.MOVEMENT_SPEED, new AttributeModifier(Bloodlines.rl("noble_speed_increase"), CommonConfig.nobleSpeedMultiplier.get().get(realRank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), skillHandler, cleanup);
        } else {
            attributes.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(Bloodlines.rl("noble_mob_speed_increase"), CommonConfig.nobleMobSpeedIncrease.get().get(realRank), AttributeModifier.Operation.ADD_VALUE));
        }
        return attributes;
    }

    @Override
    public void onDealDamage(LivingIncomingDamageEvent event, LivingEntity bloodlineMember, LivingEntity victim, int blRank) {
        if (bloodlineMember instanceof Player player) {
            handleLeeching(player, event.getAmount(), event.getEntity());
            if (!UtilLib.canReallySee(event.getEntity(), bloodlineMember, false) && VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_INTRIGUE)) {
                event.setAmount(event.getAmount() * CommonConfig.nobleIntrigueDamageMultiplier.get().floatValue());
            }
        }
    }

    @Override
    public void onReceiveDamage(LivingIncomingDamageEvent event, LivingEntity bloodlineMember, int blRank) {
        Entity source = event.getSource().getEntity();
        if(source instanceof LivingEntity living && Helper.isVampire(living)) {
            IBloodlineManager sourceBloodline = BloodlineHelper.getBloodlineManager(living);
            if(sourceBloodline != null && sourceBloodline.getBloodline() != BloodlineRegistry.BLOODLINE_NOBLE.get()) {
                event.setAmount(Math.min(event.getAmount() * CommonConfig.nobleIncreasedNonNobleDamage.get().get(blRank).floatValue(), Float.MAX_VALUE));
            }

        }
        if(event.getSource().is(ModDamageTypes.VAMPIRE_ON_FIRE) || event.getSource().is(ModDamageTypes.VAMPIRE_IN_FIRE)) {
            event.setAmount(Math.min(event.getAmount() * CommonConfig.nobleFireDamageMultiplier.get().get(blRank).floatValue(), Float.MAX_VALUE));
        }
    }

    private static void handleLeeching(Player player, float originalAmount, LivingEntity target) {
        VampirePlayer vampirePlayer = VampirePlayer.get(player);
        IVampSpecialAttributes specialAttributes = (IVampSpecialAttributes) vampirePlayer.getSpecialAttributes();
        if(specialAttributes.bloodlines$getLeeching() < 1) return;
        float amt = Math.min(2, originalAmount * CommonConfig.leechingMultiplier.get().floatValue());
        if(target instanceof Player targetPlayer) {
            if(!Helper.isVampire(targetPlayer)) {
                targetPlayer.getFoodData().addExhaustion(amt);
            } else {
                vampirePlayer.addExhaustion(amt);
            }
        } else {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0));
        }
        vampirePlayer.drinkBlood(Math.round(amt), amt * CommonConfig.leechingMultiplier.get().floatValue(), new DrinkBloodContext(target));
        if(specialAttributes.bloodlines$getLeeching() >= 2) {
            player.heal(amt);
        }
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return NOBLE;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return CommonConfig.nobleDefaults;
    }

    @Override
    public String getName() {
        return "Noble";
    }


    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.NOBLE;
    }

    @Override
    public void tick(Player player) {
        if (player.tickCount % 10 != 0) return;

        if (player.isSpectator() || VampirismPlayerAttributes.get(player).getVampSpecial().waterResistance)
            return;

        BlockPos pos = player.getOnPos();
        VampirePlayer vp = VampirePlayer.get(player);
        if (player.level().getBiome(pos).value().getPrecipitationAt(pos) != Biome.Precipitation.RAIN || !player.level().isRaining()) return;
        BlockPos realPos = new BlockPos((int) player.getX(), (int) (player.getY() + Mth.clamp(player.getBbHeight() / 2.0F, 0F, 2F)), (int) player.getZ());
        if (Helper.canBlockSeeSun(player.level(), realPos) && !(LevelFog.get(player.level()).isInsideArtificialVampireFogArea(new BlockPos((int) player.getX(), (int) (player.getY() + 1), (int) player.getZ())))) {
            if (CommonConfig.nobleRainWeakness.get() && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_RANK_3.get())) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));
            }
        }
    }

    @Override
    public void onBloodDrink(BloodDrinkEvent.PlayerDrinkBloodEvent event, int rank, VampirePlayer bloodlinePlayer) {
        //Decrease blood gain from food sources and increase blood gain from mobs for nobles.
        if(event.getBloodSource().getStack().isPresent()) {
            event.setAmount((int) (event.getAmount() * CommonConfig.nobleBloodGainDecreaseMultiplier.get().get(rank).floatValue()));
            event.setSaturationModifier(event.getSaturation() * CommonConfig.nobleBloodGainDecreaseMultiplier.get().get(rank).floatValue());
        }
        else if(event.getBloodSource().getEntity().isPresent()) {
            int amount = event.getAmount();
            int statIncrease = amount * VReference.FOOD_TO_FLUID_BLOOD;
            if(bloodlinePlayer.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_BETTER_BLOOD_DRAIN.get())) {
                statIncrease = (int) (statIncrease * CommonConfig.nobleBloodGainMultiplier.get().get(rank).floatValue());
                amount = (int) (event.getAmount() * CommonConfig.nobleBloodGainMultiplier.get().get(rank).floatValue());
                event.setSaturationModifier(event.getSaturation() * CommonConfig.nobleBloodGainMultiplier.get().get(rank).floatValue());
            }
            event.setAmount(amount);
            bloodlinePlayer.asEntity().awardStat(BloodlinesStats.ENTITY_BLOOD_DRUNK.get(), statIncrease);
        }

    }
}
