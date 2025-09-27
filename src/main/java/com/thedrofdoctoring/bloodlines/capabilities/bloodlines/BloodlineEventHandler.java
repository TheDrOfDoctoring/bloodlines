package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.blocks.entities.PhylacteryBlockEntity;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.VampireBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.other.IPossessedEntity;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import com.thedrofdoctoring.bloodlines.core.BloodlinesEffects;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import com.thedrofdoctoring.bloodlines.effects.HeinousCurseEffect;
import com.thedrofdoctoring.bloodlines.entity.GraveboundTargetModifier;
import com.thedrofdoctoring.bloodlines.entity.ZealotTargetGoalModifier;
import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundIcePacket;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound.GraveboundMistFormAction;
import com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound.GraveboundPossessionAction;
import com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound.GraveboundSoulClaimingAction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.event.ActionEvent;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import de.teamlapen.vampirism.config.VampirismConfig;
import de.teamlapen.vampirism.core.ModDamageTypes;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.InvisibilityVampireAction;
import de.teamlapen.vampirism.entity.player.vampire.skills.VampireSkills;
import de.teamlapen.vampirism.entity.vampire.DrinkBloodContext;
import de.teamlapen.vampirism.mixin.accessor.GoalSelectorAccessor;
import de.teamlapen.vampirism.mixin.accessor.NearestAttackableTargetGoalAccessor;
import de.teamlapen.vampirism.util.Helper;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;


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
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onJoinWorld(EntityJoinLevelEvent event) {

        if(event.getEntity() instanceof Player player && event.getLevel() instanceof ServerLevel level) {
            BloodlineManager manager = BloodlineManager.get(player);

            BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(manager);
            if(state == null || state.getPhylacteryDimension() == null) return;
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, state.getPhylacteryDimension());
            ServerLevel phylacteryDimension = level.getServer().getLevel(dimension);

            if(phylacteryDimension != null && state.getPhylacteryPos() != null) {
                BlockState block = phylacteryDimension.getBlockState(state.getPhylacteryPos());
                if(!block.is(BloodlinesBlocks.PHYLACTERY)) {
                    state.setPhylactery(null, null);
                    manager.updatePlayerCache();
                    manager.sync(false);
                } else if(phylacteryDimension.getBlockEntity(state.getPhylacteryPos()) instanceof PhylacteryBlockEntity phylactery) {
                    if(phylactery.getOwnerUUID() != null && !phylactery.getOwnerUUID().equals(player.getUUID())) {
                        state.setPhylactery(null, null);
                        manager.updatePlayerCache();
                        manager.sync(false);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLeaveWorld(EntityLeaveLevelEvent event) {
        if(event.getEntity() instanceof Player player) {
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
            if(atts.getGraveboundData().possessionActive) {

                LivingEntity possessed = atts.getGraveboundData().possessedEntity;
                if(possessed != null && possessed.isAlive()) {
                    ((IPossessedEntity) possessed).bloodlines$clearPossession();
                }

                GraveboundPossessionAction.clearPossession(player);
                HunterPlayer.get(player).getActionHandler().deactivateAction(BloodlineActions.GRAVEBOUND_POSSESSION_ACTION.get());
            }
        }
        if(event.getEntity() instanceof IPossessedEntity possessed) {
            if(possessed.bloodlines$isPossessed() && possessed.bloodlines$getPossessedPlayer() != null) {
                Player possessor = possessed.bloodlines$getPossessedPlayer();
                BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(possessor);
                atts.getGraveboundData().possessedEntity = null;
            }
        }
    }
    @SubscribeEvent
    public static void onLeaveWorld(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
        if(atts.getGraveboundData().possessionActive) {
            LivingEntity possessed = atts.getGraveboundData().possessedEntity;
            if(possessed != null && possessed.isAlive()) {
                ((IPossessedEntity) possessed).bloodlines$clearPossession();
            }
            GraveboundPossessionAction.clearPossession(player);
            HunterPlayer.get(player).getActionHandler().deactivateAction(BloodlineActions.GRAVEBOUND_POSSESSION_ACTION.get());
        }
    }
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {

        IBloodlineManager manager = BloodlineHelper.getBloodlineManager(event.getEntity());

        if(manager != null && manager.getBloodline() instanceof IBloodlineEventReceiver eventReceiver) {
            eventReceiver.onLivingDeath(event);
        }

        GraveboundSoulClaimingAction.handleSoulClaiming(event);

        if(event.getEntity() instanceof IPossessedEntity possessed) {
            if(possessed.bloodlines$isPossessed() && possessed.bloodlines$getPossessedPlayer() != null) {
                Player possessor = possessed.bloodlines$getPossessedPlayer();
                GraveboundPossessionAction.clearPossession(possessor);
            }
        }



    }

    @SubscribeEvent
    public static void onPotionRemove(MobEffectEvent.Remove event) {
        if(event.getEffect() == ModEffects.OBLIVION && event.getEntity() instanceof Player player) {
            BloodlineManager.getOpt(player).ifPresent(bl -> {
                if(bl.getBloodline() != null) {
                    bl.getBloodline().onBloodlineChange(player, bl.getRank());
                }
            });
        }
    }
    @SubscribeEvent
    public static void onExperienceGain(PlayerXpEvent.XpChange event) {
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(event.getEntity());
        if(atts.bloodline == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get()) {
            float xpMult = HunterBloodlinesConfig.graveboundRankExperienceMult.get().get(atts.bloodlineRank - 1).floatValue();
            event.setAmount((int) (event.getAmount() * xpMult));
        }
    }



    @SubscribeEvent
    public static void onBloodDrink(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        if(event.getVampire() instanceof VampirePlayer vp) {

            BloodlineManager manager = BloodlineManager.get(vp.asEntity());

            if(manager.getBloodline() instanceof VampireBloodline vampireBloodline) {
                vampireBloodline.onBloodDrink(event, manager.getRank() - 1, vp);
            }
        }
    }
    @SubscribeEvent
    public static void onCrit(CriticalHitEvent event) {
        IBloodline sourceBloodline = BloodlineManager.get(event.getEntity()).getBloodline();
        if (sourceBloodline instanceof IBloodlineEventReceiver receiver) {
            receiver.onCrit(event);
        }
    }

    @SubscribeEvent
    public static void onTick(PlayerTickEvent.Post event) {

        IBloodline bl = BloodlinesPlayerAttributes.get(event.getEntity()).bloodline;
        if (bl instanceof IBloodlineEventReceiver receiver) {
            receiver.tick(event.getEntity());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide) return;

        // Handle Unlocks

        if (event.getEntity() instanceof Player player && event.getAmount() >= event.getEntity().getHealth()) {
            BloodlineManager bl = BloodlineManager.get(player);
            if (CommonConfig.ectothermUniqueUnlock.get() && Helper.isVampire(player) && event.getSource().is(ModDamageTypes.SUN_DAMAGE) && bl.getBloodline() == null && player.hasEffect(BloodlinesEffects.COLD_BLOODED) && player.isInWater()) {
                player.removeEffect(BloodlinesEffects.COLD_BLOODED);
                player.addEffect(new MobEffectInstance(ModEffects.SUNSCREEN, 30 * 20, 2));
                BloodlineHelper.joinBloodlineGeneric(player, BloodlineRegistry.BLOODLINE_ECTOTHERM.get(), Component.translatable("text.bloodlines.ectotherm_join").withStyle(ChatFormatting.DARK_RED));
                event.setCanceled(true);
                return;
            }
            if (HunterBloodlinesConfig.graveboundUniqueUnlock.get() && player.hasEffect(BloodlinesEffects.SOUL_RENDING) && player.hasEffect(BloodlinesEffects.HEINOUS_CURSE) && Helper.isHunter(player) && bl.getBloodline() == null) {
                Optional<PhylacteryBlockEntity> phylacteryOpt = PhylacteryBlockEntity.searchForNearbyPhylactery(player.level(), player.getOnPos().above(), false, -2, 2, -2, 2, -2, 2);
                if (phylacteryOpt.isPresent() && phylacteryOpt.get().getOwner() == null) {
                    PhylacteryBlockEntity phylactery = phylacteryOpt.get();
                    BlockPos phylacteryPos = phylactery.getBlockPos();
                    BloodlineHelper.joinBloodlineGeneric(player, BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), Component.translatable("text.bloodlines.gravebound_join").withStyle(ChatFormatting.DARK_RED));
                    phylactery.setOwner(player);
                    player.removeEffect(BloodlinesEffects.SOUL_RENDING);
                    player.removeEffect(BloodlinesEffects.HEINOUS_CURSE);
                    BloodlineManager manager = BloodlineManager.get(player);

                    if (manager.getBloodlineState().isPresent() && manager.getBloodlineState().get() instanceof BloodlineGravebound.State state) {
                        state.setPhylactery(phylacteryPos, player.level().dimension().location());
                        state.setSouls(10);
                        manager.sync(false);
                    }

                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2));
                    ModParticles.spawnParticlesServer(player.level(), ParticleTypes.SOUL, phylacteryPos.getX(), phylacteryPos.getY(), phylacteryPos.getZ(), 100, 0f, 1f, 0f, 0.1f);
                    event.setCanceled(true);
                    return;
                }
            }

        }


        IBloodlineManager targetManager = BloodlineHelper.getBloodlineManager(event.getEntity());
        if (targetManager != null && targetManager.getBloodline() instanceof IBloodlineEventReceiver receiver) {
            receiver.onReceiveDamage(event, event.getEntity(), targetManager.getRank() - 1);
        }

        if (event.getSource().getEntity() instanceof LivingEntity source && !event.isCanceled()) {

            IBloodlineManager sourceManager = BloodlineHelper.getBloodlineManager(source);

            if (sourceManager != null && sourceManager.getBloodline() instanceof IBloodlineEventReceiver receiver) {
                receiver.onDealDamage(event, source, event.getEntity(), sourceManager.getRank() - 1);
            }

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
    public static void playerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {

        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(event.getEntity());
        if(atts.getGraveboundData().mistForm) {
            DamageSource lastKnownDamageSource = atts.getGraveboundData().lastDamageSource;
            if(lastKnownDamageSource != null) {
                event.getEntity().hurt(lastKnownDamageSource, 1000f);
            } else {
                event.getEntity().kill();
            }
        }
    }

    @SubscribeEvent
    public static void onFoodEatenFinish(LivingEntityUseItemEvent.Finish event) {
        if(event.getEntity() instanceof Player player && Helper.isVampire(player)) {
            VampirePlayer vp = VampirePlayer.get(player);
            if(event.getItem().is(Items.ROTTEN_FLESH) && vp.getSkillHandler().isSkillEnabled(BloodlineSkills.ZEALOT_FLESH_EATING.get())) {
                //not ideal but should be fine
                MobEffectInstance hungerEffectInstance = player.getEffect(MobEffects.HUNGER);
                if(hungerEffectInstance != null && hungerEffectInstance.getDuration() == 600) {
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
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(event.getEntity());
        if(atts.bloodline == BloodlineRegistry.BLOODLINE_ZEALOT.get()) {
            int rank = atts.bloodlineRank - 1;
            float additionalSpeed = 0.0f;
            if(event.getState().is(BloodlinesTagsProviders.BloodlinesBlockTagProvider.ZEALOT_STONE) && atts.getZealotAtts().hasTunneler) {
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
            BloodlineFrost.SpecialAttributes atts = BloodlinesPlayerAttributes.get(player).getEctothermAtts();
            if(atts.icePhasing && atts.frostControl && Minecraft.getInstance().getConnection() != null) {
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
            makeFriendly(false, "spider", (Spider) event.getEntity(), Spider.SpiderTargetGoal.class, Player.class, 2, type -> type == EntityType.SPIDER || type == EntityType.CAVE_SPIDER);
        }
        if (HunterBloodlinesConfig.graveboundUndeadMobsignore.get()) {

            if (event.getEntity() instanceof Zombie) {
                //noinspection unchecked
                makeFriendly(true, "zombie", (Zombie) event.getEntity(), NearestAttackableTargetGoal.class, Player.class, 2, type -> type == EntityType.ZOMBIE || type == EntityType.HUSK || type == EntityType.ZOMBIE_VILLAGER || type == EntityType.DROWNED);

            }
            if (event.getEntity() instanceof Skeleton || event.getEntity() instanceof Stray) {
                //noinspection unchecked
                makeFriendly(true, "skeleton", (AbstractSkeleton) event.getEntity(), NearestAttackableTargetGoal.class, Player.class, 2, type -> type == EntityType.SKELETON);
            }
        }
    }



    public static <T extends Mob, S extends LivingEntity, Q extends NearestAttackableTargetGoal<S>> void makeFriendly(boolean gravebound, String name, @NotNull T e, @NotNull Class<Q> targetClass, @NotNull Class<S> targetEntityClass, int attackPriority, @NotNull Predicate<EntityType<? extends T>> typeCheck) {
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
            if (!gravebound && typeCheck.test(type)) {
                ((ZealotTargetGoalModifier) target).bloodlines$ignoreZealotSpiderFriend();
            } else if(typeCheck.test(type)) {
                ((GraveboundTargetModifier) target).bloodlines$ignoreGravebound();
            }
        } else {
            if (entityAIReplacementWarnMap.getOrDefault(name, true)) {
                Bloodlines.LOGGER.warn("Could not modify {} attack target task for {}", name, e.getType().getDescription());
                entityAIReplacementWarnMap.put(name, false);
            }

        }
    }

    //
    // Below is taken from Vampirism's Bat Mode event handlers
    //

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void eyeHeight(EntityEvent.@NotNull Size event) {
        if (event.getEntity() instanceof Player player && player.getInventory() != null) {
            if (event.getEntity().isAlive() && event.getEntity().position().lengthSqr() != 0 && event.getEntity().getVehicle() == null) { //Do not attempt to get capability while entity is being initialized
                if (BloodlinesPlayerAttributes.get((Player) event.getEntity()).getGraveboundData().mistForm) {
                    event.setNewSize(GraveboundMistFormAction.MISTFORM_SIZE);
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttackEntity(@NotNull AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.isAlive()) {
            if (BloodlinesPlayerAttributes.get(event.getEntity()).getGraveboundData().mistForm) {
                event.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.@NotNull EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player) || !event.getEntity().isAlive()) return;
        if (event.getPlacedBlock().isAir()) return; //If for some reason, cough Create cough, a block is removed (so air is placed) we don't want to prevent that.
        try {
            if (BloodlinesPlayerAttributes.get((Player) event.getEntity()).getGraveboundData().mistForm) {
                event.setCanceled(true);

                //Workaround for https://github.com/MinecraftForge/MinecraftForge/issues/7609 or https://github.com/TeamLapen/Vampirism/issues/1021
                //Chest drops content when restoring snapshot
                if (event.getPlacedBlock().hasBlockEntity()) {
                    BlockEntity t = event.getLevel().getBlockEntity(event.getPos());
                    if (t instanceof Container) {
                        ((Container) t).clearContent();
                    }
                }

                if (event.getEntity() instanceof ServerPlayer) { //For some reason this event is only run serverside. Therefore, we have to make sure the client is notified about the not-placed block.
                    MinecraftServer server = event.getEntity().level().getServer();
                    if (server != null) {
                        server.getPlayerList().sendAllPlayerInfo((ServerPlayer) event.getEntity()); //Would probably suffice to just sent a SHeldItemChangePacket
                    }
                }
            }
        } catch (Exception e) {
            // Added try catch to prevent any exception in case some other mod uses auto placers or so
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.@NotNull BreakSpeed event) {
        if (BloodlinesPlayerAttributes.get(event.getEntity()).getGraveboundData().mistForm) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemPickupPre(@NotNull ItemEntityPickupEvent.Pre event) {
        if (BloodlinesPlayerAttributes.get(event.getPlayer()).getGraveboundData().mistForm) {
            event.setCanPickup(TriState.FALSE);
        }
    }

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.@NotNull RightClickItem event) {

        if ((event.getItemStack().getItem() instanceof ThrowablePotionItem || event.getItemStack().getItem() instanceof CrossbowItem)) {
            if (BloodlinesPlayerAttributes.get(event.getEntity()).getGraveboundData().mistForm) {
                event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemUse(LivingEntityUseItemEvent.@NotNull Start event) {
        if (event.getEntity() instanceof Player player) {
            if (BloodlinesPlayerAttributes.get(player).getGraveboundData().mistForm) {
                event.setCanceled(true);
            }
        }

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockRightClicked(PlayerInteractEvent.RightClickBlock event) {
        if (BloodlinesPlayerAttributes.get(event.getEntity()).getGraveboundData().mistForm) {
            event.setCanceled(true);
        }
    }
}
