package com.thedrofdoctoring.bloodlines.capabilities;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineZealot;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import com.thedrofdoctoring.bloodlines.entity.ZealotTargetGoalModifier;
import com.thedrofdoctoring.bloodlines.networking.ServerboundIcePacket;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.event.ActionEvent;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import de.teamlapen.vampirism.core.ModDamageTypes;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.InvisibilityVampireAction;
import de.teamlapen.vampirism.entity.player.vampire.skills.VampireSkills;
import de.teamlapen.vampirism.entity.vampire.DrinkBloodContext;
import de.teamlapen.vampirism.items.VampirismItemBloodFoodItem;
import de.teamlapen.vampirism.mixin.accessor.GoalSelectorAccessor;
import de.teamlapen.vampirism.mixin.accessor.NearestAttackableTargetGoalAccessor;
import de.teamlapen.vampirism.particle.GenericParticleOptions;
import de.teamlapen.vampirism.util.Helper;
import de.teamlapen.vampirism.world.LevelFog;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static com.mojang.text2speech.Narrator.LOGGER;

@EventBusSubscriber(modid = Bloodlines.MODID)
public class BloodlineEventHandler {

    @SubscribeEvent
    public static void onChangeFaction(PlayerFactionEvent.FactionLevelChanged event) {
        if(event.getPlayer().asEntity().getCommandSenderWorld().isClientSide) return;

        if(event.getNewLevel() == 0 || (event.getCurrentFaction() != event.getOldFaction() && event.getOldFaction() != null)) {
            BloodlineManager.getOpt(event.getPlayer().asEntity()).ifPresent(bl -> {
                IBloodline bloodline = bl.getBloodline();
                int rank = bl.getRank();
                bl.setBloodline(null);
                bl.setRank(0);
                bl.onBloodlineChange(bloodline, rank);
            });
        }
    }

    @SubscribeEvent
    public static void onPotionRemove(MobEffectEvent.Remove event) {
        if(event.getEffect() == ModEffects.OBLIVION.get() && event.getEntity() instanceof Player player) {
            BloodlineManager.getOpt(player).ifPresent(bl -> {
                if(bl.getBloodline() != null) {
                    bl.getBloodline().onBloodlineChange(player, bl.getRank());
                }
            });
        }
    }
    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        if(event.getEntity() instanceof Player player && Helper.isVampire(player) && event.getTarget() instanceof LivingEntity living) {
            VampirePlayer vp = VampirePlayer.get(player);
            if(vp.getActionHandler().isActionActive(BloodlineActions.ECTOTHERM_FROST_LORD_ACTION.get())) {

                if(vp.getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_FROZEN_ATTACK.get())) {
                    living.setTicksFrozen(CommonConfig.ectothermFrozenAttackFreezingAmount.get());
                }
                if(vp.getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_SLOWNESS_ATTACK.get())) {
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, CommonConfig.ectothermSlowingAttackSlownessDuration.get(), 0));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBloodDrink(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        if(event.getVampire() instanceof VampirePlayer vp) {
            BloodlineManager.getOpt(vp.getRepresentingPlayer()).ifPresent(bl -> {
                if(bl.getBloodline() instanceof BloodlineNoble) {
                    //Decrease blood gain from food sources and increase blood gain from mobs for nobles.
                    int rank = bl.getRank() - 1;
                    if(event.getBloodSource().getStack().isPresent() && event.getBloodSource().getStack().get().getItem() instanceof VampirismItemBloodFoodItem) {
                        event.setAmount((int) ((float) event.getAmount() * CommonConfig.nobleBloodGainDecreaseMultiplier.get().get(rank)));
                        event.setSaturationModifier(event.getSaturation() * CommonConfig.nobleBloodGainDecreaseMultiplier.get().get(rank).floatValue());
                    }
                    else if(event.getBloodSource().getEntity().isPresent() && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_BETTER_BLOOD_DRAIN.get())) {
                        event.setAmount((int) ((float) event.getAmount() * CommonConfig.nobleBloodGainMultiplier.get().get(rank)));
                        event.setSaturationModifier(event.getSaturation() * CommonConfig.nobleBloodGainMultiplier.get().get(rank).floatValue());
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onTick(PlayerTickEvent.Post event) {
        //yikes
        Player player = event.getEntity();
        if(player.tickCount % 5 == 0) {
            if(BloodlineManager.get(player).getBloodline() instanceof BloodlineFrost) {
                player.setTicksFrozen(0);
            }
        }

        if (player.tickCount % 10 == 0) {


            if (player.isSpectator() || !Helper.isVampire(player) || VampirePlayer.getOpt(player).map(vp -> vp.getSpecialAttributes().waterResistance).orElse(false))
                return;

            BlockPos pos = player.getOnPos();
            if (player.level().getBiome(pos).value().getPrecipitationAt(pos) != Biome.Precipitation.RAIN || !player.level().isRaining()) return;
            BlockPos realPos = new BlockPos((int) player.getX(), (int) (player.getY() + Mth.clamp(player.getBbHeight() / 2.0F, 0F, 2F)), (int) player.getZ());
            if (Helper.canBlockSeeSun(player.level(), realPos) && !(LevelFog.getOpt(player.level())).map(vw -> vw.isInsideArtificialVampireFogArea(new BlockPos((int) player.getX(), (int) (player.getY() + 1), (int) player.getZ()))).orElse(false)) {
                if (CommonConfig.nobleRainWeakness.get() && VampirePlayer.getOpt(player).map(vp -> vp.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_RANK_3.get())).orElse(false)) {
                    player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));
                }
            }
        }

    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide) return;

        if (event.getSource().getDirectEntity() instanceof LivingEntity source && Helper.isVampire(source)) {
            if (source instanceof Player player) {
                handleLeeching(player, event.getAmount(), event.getEntity());
                if (!UtilLib.canReallySee(event.getEntity(), source, false) && VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_INTRIGUE)) {
                    event.setAmount(event.getAmount() * CommonConfig.nobleIntrigueDamageMultiplier.get().floatValue());
                }
                if(player.level().getBiome(player.getOnPos()).is(Tags.Biomes.IS_COLD)) {

                }
            }

            if (event.getEntity() instanceof Player vampireTarget && Helper.isVampire(vampireTarget)) {
                BloodlineManager bl = BloodlineManager.get(vampireTarget);
                if (bl.getBloodlineId() == BloodlineNoble.NOBLE) {
                    event.setAmount(event.getAmount() * CommonConfig.nobleIncreasedNonNobleDamage.get().get(bl.getRank() - 1).floatValue());
                }
            }
        }

        if (event.getEntity() instanceof Player vampireTarget && Helper.isVampire(vampireTarget)) {
            BloodlineManager bl = BloodlineManager.get(vampireTarget);
            VampirePlayer vamp = VampirePlayer.get(vampireTarget);
            ISpecialAttributes specialAttributes = (ISpecialAttributes) vamp.getSpecialAttributes();
            int rank = bl.getRank() - 1;
            if (bl.getBloodline() instanceof BloodlineNoble && (event.getSource().is(ModDamageTypes.VAMPIRE_ON_FIRE) || event.getSource().is(ModDamageTypes.VAMPIRE_IN_FIRE))) {
                event.setAmount(event.getAmount() * CommonConfig.nobleFireDamageMultiplier.get().get(rank).floatValue());
            }

            if (bl.getBloodline() instanceof BloodlineZealot) {
                if(specialAttributes.bloodlines$getShadowArmour() && vampireTarget.getCommandSenderWorld().getRawBrightness(vampireTarget.getOnPos().above(), 0) <= CommonConfig.zealotShadowArmourLightLevel.get()) {
                    event.setAmount(event.getAmount() * CommonConfig.zealotShadowArmourDamageMultiplier.get().get(rank).floatValue());
                    ModParticles.spawnParticlesServer(vampireTarget.getCommandSenderWorld(), new GenericParticleOptions(ResourceLocation.fromNamespaceAndPath("minecraft", "generic_7"), 40, 0x000000, 0.2F) , vampireTarget.getX(), vampireTarget.getY() + 0.5f, vampireTarget.getZ(), 10, 0,0.2f, 0, 0);
                }
                if(bl.getRank() >= CommonConfig.zealotBrightAreaDamageMultiplierRank.get() && vampireTarget.getCommandSenderWorld().getRawBrightness(vampireTarget.getOnPos().above(), 0) >= CommonConfig.zealotBrightAreaDamageMultiplierLightLevel.get()) {
                    event.setAmount(event.getAmount() * CommonConfig.zealotBrightAreaDamageMultiplier.get().floatValue());
                }
                if(vamp.getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_HEX_PROTECTION) && (event.getSource().is(DamageTypes.MAGIC) || event.getSource().is(DamageTypes.INDIRECT_MAGIC))) {
                    event.setAmount(event.getAmount() * CommonConfig.zealotHexProtectionMultiplier.get().get(rank).floatValue());
                }
            }

            if (bl.getBloodline() instanceof BloodlineFrost) {

                if(event.getSource().is(ModDamageTypes.VAMPIRE_IN_FIRE) || event.getSource().is(ModDamageTypes.VAMPIRE_ON_FIRE)) {
                    event.setAmount(event.getAmount() * CommonConfig.ectothermFireDamageMultipliers.get().get(rank).floatValue());
                }
                if(vamp.getSkillHandler().isSkillEnabled(BloodlineSkills.ECTOTHERM_DIFFUSION.get()) && event.getSource().is(ModDamageTypes.HOLY_WATER)) {
                    event.setAmount(event.getAmount() * CommonConfig.ectothermHolyWaterDiffusion.get().get(rank).floatValue());
                }
            }

        }
    }
    private static void handleLeeching(Player player, float originalAmount, LivingEntity target) {
        VampirePlayer vampirePlayer = VampirePlayer.get(player);
        ISpecialAttributes specialAttributes = (ISpecialAttributes) vampirePlayer.getSpecialAttributes();
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

    @SubscribeEvent
    public static void livingFallEvent(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player && Helper.isVampire(player)) {
            if (player.getBlockStateOn().is(BloodlinesTagsProviders.BloodlinesBlockTagProvider.ZEALOT_STONE) && VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_FALL_DAMAGE.get())) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onFoodEatenFinish(LivingEntityUseItemEvent.Finish event) {
        if(event.getEntity() instanceof Player player && Helper.isVampire(player)) {
            VampirePlayer vp = VampirePlayer.get(player);
            if(event.getItem().is(Items.ROTTEN_FLESH) && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_FLESH_EATING.get())) {
                //not ideal but should be fine
                if(player.getEffect(MobEffects.HUNGER) != null && player.getEffect(MobEffects.HUNGER).getDuration() == 600) {
                    player.removeEffect(MobEffects.HUNGER);
                }
                vp.drinkBlood(CommonConfig.zealotFleshEatingNutrition.get(), CommonConfig.zealotFleshEatingSaturation.get().floatValue(), new DrinkBloodContext(event.getItem()));
            }
            if(event.getItem().is(BloodlinesTagsProviders.BloodlinesItemTagProvider.ECTOTHERM_RAW_FISH) && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.FISHMONGER.get())) {
                vp.drinkBlood(CommonConfig.ectothermFishmongerNutrition.get(), CommonConfig.ectothermFishmongerSaturation.get().floatValue(), new DrinkBloodContext(event.getItem()));
            }
        }
    }
    //action events :)
    @SubscribeEvent
    public static void actionActivatedEvent(ActionEvent.ActionActivatedEvent event) {
        if(event.getAction() instanceof InvisibilityVampireAction) {
            ISkillHandler<?> skillHandler = event.getFactionPlayer().getSkillHandler();
            if(skillHandler.isSkillEnabled(BloodlineSkills.NOBLE_INVISIBILITY.get()) && skillHandler.isSkillEnabled(VampireSkills.VAMPIRE_INVISIBILITY.get())) {
                event.setDuration(CommonConfig.bothInvisibilityDuration.get() * 20);
            }
        } else if (!(event.getAction() instanceof ILastingAction<?>) && event.getFactionPlayer().getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_SHADOW_MASTERY.get())) {
            Player player = event.getFactionPlayer().asEntity();
            if(BloodlineHelper.lightMatches(CommonConfig.zealotShadowMasteryLightLevel.get(), player.getOnPos().above(), player.getCommandSenderWorld(), false)) {
                int rank = BloodlineHelper.getBloodlineRank(player) - 1;
                event.setCooldown(Math.round(event.getCooldown() * CommonConfig.zealotShadowMasteryCooldownMultiplier.get().get(rank).floatValue()));
            }
        }
    }
    @SubscribeEvent
    public static void actionDeactivatedEvent(ActionEvent.ActionDeactivatedEvent event) {
        if(event.getAction() instanceof InvisibilityVampireAction) {
            ISkillHandler<?> skillHandler = event.getFactionPlayer().getSkillHandler();
            if(skillHandler.isSkillEnabled(BloodlineSkills.NOBLE_INVISIBILITY.get()) && skillHandler.isSkillEnabled(VampireSkills.VAMPIRE_INVISIBILITY.get())) {
                event.setCooldown(CommonConfig.bothInvisibilityCooldown.get() * 20);
            }
        }
        if (event.getFactionPlayer().getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_SHADOW_MASTERY.get())) {
            Player player = event.getFactionPlayer().asEntity();
            if(BloodlineHelper.lightMatches(CommonConfig.zealotShadowMasteryLightLevel.get(), player.getOnPos().above(), player.getCommandSenderWorld(), false)) {
                int rank = BloodlineHelper.getBloodlineRank(player) - 1;
                event.setCooldown(Math.round(event.getCooldown() * CommonConfig.zealotShadowMasteryCooldownMultiplier.get().get(rank).floatValue()));
            }
        }
    }
    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        BloodlineManager bloodlineManager = BloodlineManager.get(event.getEntity());
        if(bloodlineManager.getBloodline() instanceof BloodlineZealot) {
            int rank = bloodlineManager.getRank() - 1;
            float additionalSpeed = 0.0f;
            if(event.getState().is(BloodlinesTagsProviders.BloodlinesBlockTagProvider.ZEALOT_STONE) && VampirePlayer.get(event.getEntity()).getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_TUNNELER.get())) {
                additionalSpeed = CommonConfig.zealotTunnelerIncrease.get().get(rank).floatValue();
            }
            event.setNewSpeed((event.getOriginalSpeed() + additionalSpeed) * CommonConfig.zealotMiningSpeedIncrease.get().get(rank).floatValue());
        }


    }
    @SubscribeEvent
    public static void interact(PlayerInteractEvent.RightClickEmpty event) {
        if(event.getHand() != InteractionHand.MAIN_HAND) return;

        HitResult result = event.getEntity().pick(10, 1, true);
        if(!(result instanceof BlockHitResult blockResult) ) return;
        Player player = event.getEntity();
        BlockPos eyePos = BlockPos.containing(player.getEyePosition());
        Level level = event.getLevel();
        if(level.getBlockState(eyePos).is(Blocks.WATER)) return;


        BlockPos pos = blockResult.getBlockPos();
        if(level.getBlockState(pos).is(Blocks.WATER)) {
            ISpecialAttributes specialAttributes = (ISpecialAttributes) VampirePlayer.get(event.getEntity()).getSpecialAttributes();
            if(specialAttributes.bloodlines$getIcePhasing() && specialAttributes.bloodlines$getFrostControl()) {
                //level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1, 1);
                Minecraft.getInstance().getConnection().send(ServerboundIcePacket.getInstance());
            }
        }
    }
    // Below taken and modified from
    // https://github.com/TeamLapen/Vampirism/blob/779184a22c1d3d501609ac3973beddf81991d340/src/main/java/de/teamlapen/vampirism/entity/ModEntityEventHandler.java#L73
    private static final Object2BooleanMap<String> entityAIReplacementWarnMap = new Object2BooleanArrayMap<>();
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Spider) {
            //noinspection unchecked
            makeZealotFriendly("spider", (Spider) event.getEntity(), Spider.SpiderTargetGoal.class, Player.class, 2, type -> type == EntityType.SPIDER || type == EntityType.CAVE_SPIDER);
        }
    }
    public static <T extends Mob, S extends LivingEntity, Q extends NearestAttackableTargetGoal<S>> void makeZealotFriendly(String name, @NotNull T e, @NotNull Class<Q> targetClass, @NotNull Class<S> targetEntityClass, int attackPriority, @NotNull Predicate<EntityType<? extends T>> typeCheck) {
        Goal target = null;
        for (WrappedGoal t : ((GoalSelectorAccessor) e.targetSelector).getAvailableGoals()) {
            Goal g = t.getGoal();
            if (targetClass.equals(g.getClass()) && t.getPriority() == attackPriority && targetEntityClass.equals(((NearestAttackableTargetGoalAccessor<?>) g).getTargetType())) {
                target = g;
                break;
            }
        }
        if (target != null) {
            @SuppressWarnings("unchecked")
            EntityType<? extends T> type = (EntityType<? extends T>) e.getType();
            if (typeCheck.test(type)) {
                ((ZealotTargetGoalModifier) target).ignoreZealotSpiderFriend();
            }
        } else {
            if (entityAIReplacementWarnMap.getOrDefault(name, true)) {
                LOGGER.warn("Could not modify {} attack target task for {}", name, e.getType().getDescription());
                entityAIReplacementWarnMap.put(name, false);
            }

        }
    }
}
