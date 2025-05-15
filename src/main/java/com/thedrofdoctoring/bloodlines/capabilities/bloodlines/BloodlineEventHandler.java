package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.*;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesEffects;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import com.thedrofdoctoring.bloodlines.effects.HeinousCurseEffect;
import com.thedrofdoctoring.bloodlines.entity.ZealotTargetGoalModifier;
import com.thedrofdoctoring.bloodlines.networking.ServerboundIcePacket;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.api.event.ActionEvent;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import de.teamlapen.vampirism.config.BalanceConfig;
import de.teamlapen.vampirism.config.VampirismConfig;
import de.teamlapen.vampirism.core.ModDamageTypes;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.entity.ExtendedCreature;
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
import de.teamlapen.vampirism.world.ModDamageSources;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


@EventBusSubscriber(modid = Bloodlines.MODID)
public class BloodlineEventHandler {

    private static final ResourceLocation reducedMovementSpeed = Bloodlines.rl("ectotherm_biome_reduced_movement_speed");
    private static final ResourceLocation increasedMovementSpeed = Bloodlines.rl("ectotherm_biome_increased_movement_speed");
    private static final ResourceLocation reducedMaxHealth = Bloodlines.rl("ectotherm_biome_reduced_max_haelth");

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
            if(!event.getTarget().getType().is(EntityTypeTags.UNDEAD) && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_POISONED_STRIKE.get())) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, CommonConfig.zealotPoisonedStrikeDuration.get(), 0));
            }
        }
    }

    @SubscribeEvent
    public static void onBloodDrink(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        if(event.getVampire() instanceof VampirePlayer vp) {
            BloodlineManager.getOpt(vp.getRepresentingPlayer()).ifPresent(bl -> {
                int rank = bl.getRank() - 1;
                if(bl.getBloodline() == BloodlineRegistry.BLOODLINE_NOBLE) {
                    //Decrease blood gain from food sources and increase blood gain from mobs for nobles.
                    if(event.getBloodSource().getStack().isPresent() && event.getBloodSource().getStack().get().getItem() instanceof VampirismItemBloodFoodItem) {
                        event.setAmount((int) (event.getAmount() * CommonConfig.nobleBloodGainDecreaseMultiplier.get().get(rank).floatValue()));
                        event.setSaturationModifier(event.getSaturation() * CommonConfig.nobleBloodGainDecreaseMultiplier.get().get(rank).floatValue());
                    }
                    else if(event.getBloodSource().getEntity().isPresent() && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_BETTER_BLOOD_DRAIN.get())) {
                        event.setAmount((int) (event.getAmount() * CommonConfig.nobleBloodGainMultiplier.get().get(rank).floatValue()));
                        event.setSaturationModifier(event.getSaturation() * CommonConfig.nobleBloodGainMultiplier.get().get(rank).floatValue());
                    }
                }
                if(bl.getBloodline() == BloodlineRegistry.BLOODLINE_BLOODKNIGHT) {
                    if(event.getBloodSource().getStack().isPresent()) {
                        List<Item> allowed = List.of(ModItems.VAMPIRE_BLOOD_BOTTLE.get(), ModItems.BLOOD_BOTTLE.get());
                        if(!allowed.contains(event.getBloodSource().getStack().get().getItem())) {
                            event.setAmount((int) (event.getAmount() * CommonConfig.bloodknightOtherSourceBloodDecrease.get().get(rank).floatValue()));
                            event.setSaturationModifier(event.getSaturation() * CommonConfig.bloodknightOtherSourceBloodDecrease.get().get(rank).floatValue());
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
            });
        }
    }
    @SubscribeEvent
    public static void onCrit(CriticalHitEvent event) {
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
                DrinkBloodContext emptyCon = new DrinkBloodContext(null, null);
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

    @SubscribeEvent
    public static void onTick(PlayerTickEvent.Post event) {
        //yikes
        Player player = event.getEntity();
        if(player.tickCount % 5 == 0) {
            if(BloodlineManager.get(player).getBloodline() == BloodlineRegistry.BLOODLINE_ECTOTHERM) {
                player.setTicksFrozen(0);
            }
        }

        if (player.tickCount % 10 == 0) {
            checkNobleRain(player);
            checkEctothermBiome(player);
        }
    }

    private static void checkNobleRain(Player player) {
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
    private static void checkEctothermBiome(Player player) {
        if(BloodlineManager.get(player).getBloodline() == BloodlineRegistry.BLOODLINE_ECTOTHERM && !player.level().isClientSide) {
            int rank = BloodlineManager.get(player).getRank() - 1;
            int modifierRank = rank + 1;
            Holder<Biome> biome = player.level().getBiome(player.getOnPos());
            if (biome.is(Tags.Biomes.IS_HOT) && biome.is(Tags.Biomes.IS_OCEAN)) {
                if (modifierRank >= CommonConfig.ectothermHotBiomeReducedMaxHealthRank.get() && !hasAttributeAlready(player, Attributes.MAX_HEALTH, reducedMaxHealth)) {
                    player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(reducedMaxHealth, CommonConfig.ectothermHotBiomeReducedMaxHealthAmount.get().get(rank), AttributeModifier.Operation.ADD_VALUE));
                }
                if (modifierRank >= CommonConfig.ectothermHotBiomeReducedMovementSpeedRank.get() && !hasAttributeAlready(player, Attributes.MOVEMENT_SPEED, reducedMovementSpeed)) {
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(reducedMovementSpeed, CommonConfig.ectothermHotBiomeReducedMovementSpeedMultiplier.get().get(rank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                }
            } else {
                BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), reducedMovementSpeed);
                BloodlineManager.removeModifier(player.getAttribute(Attributes.MAX_HEALTH), reducedMaxHealth);
            }
            if (biome.is(Tags.Biomes.IS_COLD)) {
                if (modifierRank >= CommonConfig.ectothermColdBiomeIncreasedMovementSpeedRank.get() && !hasAttributeAlready(player, Attributes.MOVEMENT_SPEED, increasedMovementSpeed)) {
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(increasedMovementSpeed, CommonConfig.ectothermColdBiomeSpeedMultiplier.get().get(rank), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                }
            } else {
                BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), increasedMovementSpeed);
            }
        } else if(player.tickCount % 50 == 0 && !player.level().isClientSide) {
            BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), reducedMovementSpeed);
            BloodlineManager.removeModifier(player.getAttribute(Attributes.MAX_HEALTH), reducedMaxHealth);
            BloodlineManager.removeModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), increasedMovementSpeed);
        }
    }
    private static boolean hasAttributeAlready(Player player, Holder<Attribute> att, ResourceLocation rl) {
        if(player.getAttribute(att).hasModifier(rl)) {
            return true;
        }
        return false;
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide) return;
        if (!CommonConfig.ectothermUniqueUnlock.get()) return;
        if (event.getEntity() instanceof Player player && event.getAmount() >= event.getEntity().getHealth() && event.getSource().is(ModDamageTypes.SUN_DAMAGE)) {
            BloodlineManager bl = BloodlineManager.get(player);
            if(bl.getBloodline() == null && player.hasEffect(BloodlinesEffects.COLD_BLOODED) && player.isInWater()) {
                player.removeEffect(BloodlinesEffects.COLD_BLOODED);
                player.addEffect(new MobEffectInstance(ModEffects.SUNSCREEN, 30 * 20, 2));
                BloodlineHelper.joinBloodlineGeneric(player, BloodlineRegistry.BLOODLINE_ECTOTHERM.get(), Component.translatable("text.bloodlines.ectotherm_join").withStyle(ChatFormatting.DARK_RED));
                event.setCanceled(true);

            }

        }

        if (event.getSource().getEntity() instanceof LivingEntity source && Helper.isVampire(source)) {
            if (source instanceof Player player) {
                handleLeeching(player, event.getAmount(), event.getEntity());
                if (!UtilLib.canReallySee(event.getEntity(), source, false) && VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_INTRIGUE)) {
                    event.setAmount(event.getAmount() * CommonConfig.nobleIntrigueDamageMultiplier.get().floatValue());
                }
            }

            if (Helper.isVampire(event.getEntity())) {
                IBloodlineManager bl = BloodlineHelper.getBloodlineManager(event.getEntity());
                if (bl != null && bl.getBloodlineId() == BloodlineNoble.NOBLE) {
                    event.setAmount(event.getAmount() * CommonConfig.nobleIncreasedNonNobleDamage.get().get(bl.getRank() - 1).floatValue());
                }
                IBloodlineManager sourceBL = BloodlineHelper.getBloodlineManager(source);
                if(sourceBL != null && sourceBL.getBloodline() == BloodlineRegistry.BLOODLINE_BLOODKNIGHT.get()) {
                    int rank = sourceBL.getRank() - 1;
                    event.setAmount(event.getAmount() * CommonConfig.bloodknightVampireDamageMult.get().get(rank).floatValue());
                }
            }
        }

        if ((event.getEntity() instanceof Player || event.getEntity() instanceof PathfinderMob) && Helper.isVampire(event.getEntity())) {
            LivingEntity vampireTarget = event.getEntity();
            IBloodlineManager bl = BloodlineHelper.getBloodlineManager(vampireTarget);
            if(bl == null) return;
            IVampSpecialAttributes specialAttributes = null;
            ISkillHandler<IVampirePlayer> skillHandler = null;
            if(vampireTarget instanceof Player playerVampire) {
                specialAttributes = (IVampSpecialAttributes) VampirePlayer.get(playerVampire).getSpecialAttributes();
                skillHandler = VampirePlayer.get(playerVampire).getSkillHandler();
            }
            int rank = bl.getRank() - 1;
            if (bl.getBloodline() == BloodlineRegistry.BLOODLINE_NOBLE && (event.getSource().is(ModDamageTypes.VAMPIRE_ON_FIRE) || event.getSource().is(ModDamageTypes.VAMPIRE_IN_FIRE))) {
                event.setAmount(event.getAmount() * CommonConfig.nobleFireDamageMultiplier.get().get(rank).floatValue());
            }

            if (bl.getBloodline() == BloodlineRegistry.BLOODLINE_ZEALOT) {
                int brightness = vampireTarget.getCommandSenderWorld().getRawBrightness(vampireTarget.getOnPos().above(), 0);
                if((specialAttributes == null || specialAttributes.bloodlines$getShadowArmour()) && brightness <= CommonConfig.zealotShadowArmourLightLevel.get()) {
                    event.setAmount(event.getAmount() * CommonConfig.zealotShadowArmourDamageMultiplier.get().get(rank).floatValue());
                    ModParticles.spawnParticlesServer(vampireTarget.getCommandSenderWorld(), new GenericParticleOptions(ResourceLocation.fromNamespaceAndPath("minecraft", "generic_7"), 40, 0x000000, 0.2F) , vampireTarget.getX(), vampireTarget.getY() + 0.5f, vampireTarget.getZ(), 10, 0,0.2f, 0, 0);
                }
                if(bl.getRank() >= CommonConfig.zealotBrightAreaDamageMultiplierRank.get() && brightness >= CommonConfig.zealotBrightAreaDamageMultiplierLightLevel.get()) {
                    event.setAmount(event.getAmount() * CommonConfig.zealotBrightAreaDamageMultiplier.get().floatValue());
                }
                if(skillHandler == null || skillHandler.isSkillEnabled(BloodlineSkills.ZEALOT_HEX_PROTECTION.get()) && (event.getSource().is(DamageTypes.MAGIC) || event.getSource().is(DamageTypes.INDIRECT_MAGIC))) {
                    event.setAmount(event.getAmount() * CommonConfig.zealotHexProtectionMultiplier.get().get(rank).floatValue());
                }
            }

            if (bl.getBloodline() == BloodlineRegistry.BLOODLINE_ECTOTHERM) {

                if(event.getSource().is(ModDamageTypes.VAMPIRE_IN_FIRE) || event.getSource().is(ModDamageTypes.VAMPIRE_ON_FIRE)) {
                    event.setAmount(event.getAmount() * CommonConfig.ectothermFireDamageMultipliers.get().get(rank).floatValue());
                }
                if((skillHandler == null || skillHandler.isSkillEnabled(BloodlineSkills.ECTOTHERM_DIFFUSION.get())) && event.getSource().is(ModDamageTypes.HOLY_WATER)) {
                    event.setAmount(event.getAmount() * CommonConfig.ectothermHolyWaterDiffusion.get().get(rank).floatValue());
                }
            }
            if (bl.getBloodline() == BloodlineRegistry.BLOODLINE_BLOODKNIGHT) {
                if(event.getSource().getEntity() instanceof LivingEntity entity && Helper.isHunter(entity)) {
                    event.setAmount(event.getAmount() * CommonConfig.bloodknightHunterDamageMult.get().get(rank).floatValue());
                }
            }

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
        Player player = event.getFactionPlayer().asEntity();
        if(event.getAction() instanceof InvisibilityVampireAction) {
            ISkillHandler<?> skillHandler = event.getFactionPlayer().getSkillHandler();
            if(skillHandler.isSkillEnabled(BloodlineSkills.NOBLE_INVISIBILITY.get()) && skillHandler.isSkillEnabled(VampireSkills.VAMPIRE_INVISIBILITY.get())) {
                event.setCooldown(CommonConfig.bothInvisibilityCooldown.get() * 20);
            }
        }
        if (event.getFactionPlayer().getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_SHADOW_MASTERY.get())) {
            if(BloodlineHelper.lightMatches(CommonConfig.zealotShadowMasteryLightLevel.get(), player.getOnPos().above(), player.getCommandSenderWorld(), false)) {
                int rank = BloodlineHelper.getBloodlineRank(player) - 1;
                event.setCooldown(Math.round(event.getCooldown() * CommonConfig.zealotShadowMasteryCooldownMultiplier.get().get(rank).floatValue()));
            }
        }
        if (player.level().getBiome(player.getOnPos()).is(Tags.Biomes.IS_HOT) && BloodlineManager.get(event.getFactionPlayer().asEntity()).getBloodline() instanceof BloodlineFrost) {
            int rank = BloodlineManager.get(player).getRank() - 1;
            if(rank >= CommonConfig.ectothermHotBiomeActionCooldownRank.get()) {
                event.setCooldown(Math.round(event.getCooldown() * CommonConfig.ectothermHotBiomeActionCooldownMultiplier.get().get(rank).floatValue()));
            }
        }
    }
    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        BloodlineManager bloodlineManager = BloodlineManager.get(event.getEntity());
        if(bloodlineManager.getBloodline() == BloodlineRegistry.BLOODLINE_ZEALOT) {
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
            IVampSpecialAttributes specialAttributes = (IVampSpecialAttributes) VampirePlayer.get(event.getEntity()).getSpecialAttributes();
            if(specialAttributes.bloodlines$getIcePhasing() && specialAttributes.bloodlines$getFrostControl()) {
                Minecraft.getInstance().getConnection().send(ServerboundIcePacket.getInstance());
            }
        }
    }
    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        LivingEntity living = event.getEntity();
        if(living instanceof Player player && Helper.isVampire(player)) {
            removeBloodknightBatMult(player);
        }
    }
    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        LivingEntity living = event.getEntity();
        if(living instanceof Player player && Helper.isVampire(player)) {
            removeBloodknightBatMult(player);
        }
        if(living instanceof Player player && event.getEffectInstance() != null && event.getEffectInstance().is(BloodlinesEffects.HEINOUS_CURSE)) {
            HeinousCurseEffect.handleHeinousEnding(player);
        }

    }


    private static void removeBloodknightBatMult(Player player) {
        VampirePlayer vp = VampirePlayer.get(player);
        if(vp.getSpecialAttributes().bat && !vp.getActionHandler().isActionActive(BloodlineActions.BLOODKNIGHT_SANGUINE_INFUSION.get())) {
            player.getAbilities().setFlyingSpeed(VampirismConfig.BALANCE.vaBatFlightSpeed.get().floatValue());
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
                Bloodlines.LOGGER.warn("Could not modify {} attack target task for {}", name, e.getType().getDescription());
                entityAIReplacementWarnMap.put(name, false);
            }

        }
    }
}
