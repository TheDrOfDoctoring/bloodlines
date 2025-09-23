package com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.blocks.entities.PhylacteryBlockEntity;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodlineEventReceiver;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlineState;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.other.IPossessedEntity;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlinesDamageTypes;
import com.thedrofdoctoring.bloodlines.core.BloodlinesStats;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import com.thedrofdoctoring.bloodlines.data.datamaps.BloodlinesDataMaps;
import com.thedrofdoctoring.bloodlines.data.datamaps.EntitySoulData;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound.GraveboundMistFormAction;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.actions.IActionHandler;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.core.ModParticles;
import de.teamlapen.vampirism.core.ModSounds;
import de.teamlapen.vampirism.entity.player.actions.ActionHandler;
import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BloodlineGravebound extends HunterBloodline implements IBloodlineEventReceiver {
    public static final ResourceLocation GRAVEBOUND = Bloodlines.rl("gravebound");


    @Override
    public Map<Holder<Attribute>, AttributeModifier> getBloodlineAttributes(int rank, LivingEntity entity, boolean cleanup) {
        int realRank = rank - 1;
        if(entity instanceof Player player) {
            updateSpeed(player, realRank, BloodlinesPlayerAttributes.get(player).getGraveboundData());
        }
        return Map.of();
    }



    @Override
    public void onBloodlineChange(LivingEntity entity, int rank) {
        super.onBloodlineChange(entity, rank);
        if(entity instanceof Player player) {
            this.handleSpecialSkills(player, rank);
            BloodlineGravebound.SpecialAttributes atts = BloodlinesPlayerAttributes.get(player).getGraveboundData();
            if(Helper.isVampire(player)){
                atts.soulSpeed = 1f;
            }
            updateSpeed(player, Math.max(rank - 1, 0), atts);
        }
    }
    private void updateSpeed(Player player, int rank, BloodlineGravebound.SpecialAttributes attributes) {
        updateSpeed(rank, attributes, getSkillHandler(player));
    }
    private void updateSpeed(int rank, BloodlineGravebound.SpecialAttributes attributes, ISkillHandler<IHunterPlayer> skillHandler) {
        if(skillHandler != null && skillHandler.isSkillEnabled(BloodlineSkills.GRAVEBOUND_SOUL_SPEED.get())) {
            attributes.soulSpeed = HunterBloodlinesConfig.graveboundSoulSpeedMultiplier.get().get(rank).floatValue();
        } else {
            attributes.soulSpeed = 1f;
        }
    }

    private void handleSpecialSkills(Player player, int newRank) {

        ISkillHandler<IHunterPlayer> handler = this.getSkillHandler(player);
        if(handler == null) return;

        if(newRank >= HunterBloodlinesConfig.immortalityGraveboundRank.get()) {
            handler.enableSkill(BloodlineSkills.GRAVEBOUND_MIST_FORM.get());
        } else {
            handler.disableSkill(BloodlineSkills.GRAVEBOUND_MIST_FORM.get());
        }
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return GRAVEBOUND;
    }

    @Override
    public ResourceKey<ISkillTree> getSkillTree() {
        return BloodlineSkills.Trees.GRAVEBOUND;
    }

    @Override
    public ModConfigSpec.ConfigValue<List<? extends String>>[] getDefaultEnabledSkills() {
        return HunterBloodlinesConfig.graveboundDefaults;
    }

    @Override
    public String getName() {
        return "Gravebound";
    }

    @Override
    public Optional<BloodlineGravebound.State> getNewBloodlineState(Player player) {
        return Optional.of(new State(player));
    }

    public static @Nullable BloodlineGravebound.State getGraveboundState(Player player) {
        BloodlineManager manager = BloodlineManager.get(player);
        if(manager.getBloodlineState().isPresent() && manager.getBloodline() == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get()) {
            return (BloodlineGravebound.State) manager.getBloodlineState().get();
        }
        return null;
    }
    @Override
    public void tick(Player blPlayer) {
        blPlayer.setAirSupply(300);
        int souls = BloodlinesPlayerAttributes.get(blPlayer).getGraveboundData().souls;
        int foodLevel = 10;
        if(souls > HunterBloodlinesConfig.slowRegenSoulCount.get()) foodLevel = 18;
        if(souls < HunterBloodlinesConfig.noSprintSoulCount.get()) foodLevel = 6;
        blPlayer.getFoodData().setFoodLevel(foodLevel);

    }

    @Override
    public void onCrit(CriticalHitEvent event) {
        if(event.isVanillaCritical() && event.getTarget() instanceof LivingEntity living) {
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(event.getEntity());
            if(atts.getGraveboundData().critStrikeActive) {
                event.setDamageMultiplier(event.getDamageMultiplier() + HunterBloodlinesConfig.sorcerousStrikeAdditionalDamageMultiplier.get().floatValue());
                living.addEffect(new MobEffectInstance(MobEffects.WITHER, HunterBloodlinesConfig.sorcerousStrikeDuration.get() * 20));
                BloodlinesPlayerAttributes.get(event.getEntity()).getGraveboundData().critStrikeActive = false;
            }
        }
    }

    @Override
    public void onLivingDeath(LivingDeathEvent event) {
        if(event.getEntity() instanceof Player player && !player.level().isClientSide) {

            // Gets rid of some souls on full Gravebound death.

            BloodlineManager manager = BloodlineManager.get(player);
            BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(player);
            if(state == null) return;
            state.mistForm = false;
            state.setSouls(3);
            manager.sync(false);

        }
    }

    @Override
    public void onReceiveDamage(LivingIncomingDamageEvent event, LivingEntity bloodlineMember, int blRank) {

        if(blRank + 1 < HunterBloodlinesConfig.immortalityGraveboundRank.get() || bloodlineMember.getCommandSenderWorld().isClientSide) return;

        // We only negate damage / go into mist form if the damage type isn't one a gravebound is vulnerable to
        if(event.getSource().is(BloodlinesTagsProviders.BloodlinesDamageTypesProvider.GRAVEBOUND_VULNERABLE)) {
            // need to limit to floating point max value, otherwise this breaks the game.
            event.setAmount(Math.min(event.getAmount() * HunterBloodlinesConfig.graveboundMagicDamageMultiplier.get().get(blRank).floatValue(), Float.MAX_VALUE));
            if(HunterBloodlinesConfig.graveboundVulnerableDamageTypeMistForm.get()) {
                return;
            }
            if(event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                return;
            }
        }

        if(!(bloodlineMember instanceof Player player)) return;
 
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
        SpecialAttributes graveBoundData = atts.getGraveboundData();
        if(graveBoundData.mistForm) {
            event.setCanceled(true);
            return;
        }
        IActionHandler<IHunterPlayer> actionHandler = HunterPlayer.get(player).getActionHandler();
        if(event.getAmount() > bloodlineMember.getHealth() && !actionHandler.isActionActive(BloodlineActions.GRAVEBOUND_MIST_FORM.get()) && !actionHandler.isActionOnCooldown(BloodlineActions.GRAVEBOUND_MIST_FORM.get())) {

            // Already in mist form, so die for real.
            if(graveBoundData.mistForm) return;

            if(graveBoundData.souls < GraveboundMistFormAction.getRequiredSouls(player, blRank)) return;
            graveBoundData.mistForm = true;
            graveBoundData.lastDamageSource = event.getSource();
            HunterPlayer.get(player).getActionHandler().toggleAction(BloodlineActions.GRAVEBOUND_MIST_FORM.get(), new ActionHandler.ActivationContext());
            player.setHealth(1);

            event.setCanceled(true);

        }
    }

    public boolean canDevour(Entity entity, Player graveboundPlayer) {
        return canDevour(entity, graveboundPlayer, false, false);
    }
    //
    // Soul System
    //
    @SuppressWarnings("ConstantConditions")
    public boolean canDevour(Entity entity, Player graveboundPlayer, boolean ignoreDistance, boolean ignoreDead) {

        if(!ignoreDead && !entity.isAlive()) return false;

        if(entity instanceof LivingEntity target) {

            if(!ignoreDistance) {
                if (target.distanceTo(graveboundPlayer) >= graveboundPlayer.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).getValue() + 1)
                    return false;
            }
            float healthPercentage = target.getHealth() / target.getMaxHealth();

            EntitySoulData data = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(target.getType()).getData(BloodlinesDataMaps.ENTITY_SOULS);
            if (data == null) return false;

            float requiredHealthPercent = getDevourHealth(target, graveboundPlayer, data);

            if (healthPercentage > requiredHealthPercent) return false;

            if (target instanceof ServerPlayer player) {
                int timeAlive = player.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
                if (timeAlive < HunterBloodlinesConfig.playerAliveTimeForDevour.get()) return false;
            }
            return true;
        }

        return false;
    }
    public float getDevourHealth(LivingEntity target, Player graveboundPlayer, EntitySoulData data) {
        float requiredMaxHealthPercent = data.baseDevourHealth();
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(graveboundPlayer);
        if(atts.getGraveboundData().hasPowerfulDevour) {
            requiredMaxHealthPercent += HunterBloodlinesConfig.graveboundPowerfulDevourMaxHealthChange.get().floatValue();
        }
        return requiredMaxHealthPercent;
    }
    public void devour(LivingEntity target, Player graveboundPlayer) {
        devour(target, graveboundPlayer, true);
    }

    @SuppressWarnings("ConstantConditions")
    public void devour(LivingEntity target, Player graveboundPlayer, boolean applyDamage) {
        EntitySoulData soulData = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(target.getType()).getData(BloodlinesDataMaps.ENTITY_SOULS);
        int souls = soulData.souls();
        BlockPos pos = target.getOnPos();

        if(applyDamage) {
            target.hurt(new DamageSource(BloodlinesDamageTypes.getDamageSource(graveboundPlayer.level(), BloodlinesDamageTypes.DEVOUR_SOUL), graveboundPlayer), 1000f);
        }

        ModParticles.spawnParticlesServer(graveboundPlayer.level(), ParticleTypes.SOUL, pos.getX(), pos.getY(), pos.getZ(), 50, 0f, 0.5f, 0f, 0.1f);
        graveboundPlayer.level().playSound(graveboundPlayer, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.PLAYERS);
        graveboundPlayer.level().playSound(graveboundPlayer, pos, ModSounds.TELEPORT_AWAY.get(), SoundSource.PLAYERS);



        State state = getGraveboundState(graveboundPlayer);
        state.incrementTotalSoulsDevoured(souls);
        int used = state.addSouls(souls);
        this.onDevour(target, graveboundPlayer, soulData, state, souls, used);



        graveboundPlayer.awardStat(BloodlinesStats.SOULS_DEVOURED.get(), souls);
        graveboundPlayer.awardStat(BloodlinesStats.MOBS_SOUL_DEVOURED.get());

        BloodlineManager.get(graveboundPlayer).sync(false);

    }
    public void onDevour(LivingEntity target, Player graveboundPlayer, EntitySoulData soulData, State state, int actualSouls, int usedSouls) {
        ISkillHandler<IHunterPlayer> skillHandler = this.getSkillHandler(graveboundPlayer);

        if(skillHandler == null) {
            Bloodlines.LOGGER.warn("Unable to obtain skill handler for {} when devouring soul", graveboundPlayer.getName());
            return;
        }

        if(skillHandler.isSkillEnabled(BloodlineSkills.GRAVEBOUND_REGEN_DEVOUR.get())) {

            float healAmount = (soulData.baseDevourHealth() * target.getMaxHealth()) / 2;
            graveboundPlayer.heal(healAmount);
        }
        if(state.hasPhylactery() && skillHandler.isSkillEnabled(BloodlineSkills.GRAVEBOUND_SOUL_TRANSFER.get())) {
            state.tryGetPhylactery().ifPresent(be -> be.addSouls(actualSouls - usedSouls));
        }

    }



    public static @Nullable BloodlineGravebound.State getGraveboundState(BloodlineManager manager) {
        if(manager.getBloodlineState().isPresent() && manager.getBloodline() == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get()) {
            return (BloodlineGravebound.State) manager.getBloodlineState().get();
        }
        return null;
    }
    public static class State extends BloodlineState {

        @Nullable private BlockPos phylacteryPos;
        @Nullable ResourceLocation phylacteryDimensionID;

        private int souls;
        private int maxSouls;
        private int totalSoulsDevoured;

        private boolean mistForm;
        private boolean possession;
        private UUID possessedEntityUUID;
        private int possessedEntityNetworkID = -1;

        protected State(Player player) {
            super(player);
        }

        public ResourceLocation getPhylacteryDimension() {
            return this.phylacteryDimensionID;
        }

        public void removePhylactery() {
            this.phylacteryPos = null;
        }
        public void setPhylactery(@Nullable BlockPos pos, ResourceLocation dimension) {
            this.phylacteryPos = pos;
            this.phylacteryDimensionID = dimension;

        }

        public boolean isMistForm() {
            return mistForm;
        }

        public void setMistForm(boolean mistForm) {
            this.mistForm = mistForm;
        }

        public boolean isPossessing() {
            return this.possession;
        }

        public void setPossession(LivingEntity entity) {
            this.possession = true;
            this.possessedEntityUUID = entity.getUUID();
            this.possessedEntityNetworkID = entity.getId();

        }
        public void clearPossession() {
            if(possessedEntityUUID != null && player.level() instanceof ServerLevel level) {
                Entity entity = level.getEntity(possessedEntityNetworkID);
                if(entity instanceof IPossessedEntity possessed) {
                    possessed.bloodlines$clearPossession();
                }
            }
            this.possession = false;
            this.possessedEntityUUID = null;
            this.possessedEntityNetworkID = -1;


        }

        public int getTotalSoulsDevoured() {
            return totalSoulsDevoured;
        }

        public void incrementTotalSoulsDevoured(int amount) {
            this.totalSoulsDevoured += amount;
            if(this.phylacteryPos != null) {
                BlockEntity be = this.player.level().getBlockEntity(phylacteryPos);
                if(be instanceof PhylacteryBlockEntity phylactery && phylactery.getOwnerUUID().equals(this.player.getUUID())) {
                    phylactery.determineMaxSouls(totalSoulsDevoured);
                }
            }
        }
        public Optional<PhylacteryBlockEntity> tryGetPhylactery() {

            if(player.level() instanceof ServerLevel level && phylacteryPos != null && this.getPhylacteryDimension() != null) {

                ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, this.getPhylacteryDimension());

                ServerLevel phylacteryDimension = level.getServer().getLevel(dimension);
                if(phylacteryDimension == null) {
                    Bloodlines.LOGGER.warn("Phylactery dimension not null, but failed to retrieve phylactery for player {} in set dimension", player.getName());
                    return Optional.empty();
                }
                BlockEntity blockEntity = phylacteryDimension.getBlockEntity(phylacteryPos);
                if(blockEntity instanceof PhylacteryBlockEntity phylacteryBlockEntity) {
                    return Optional.of(phylacteryBlockEntity);
                }
            }
            return Optional.empty();
        }

        public @Nullable BlockPos getPhylacteryPos() {
            return phylacteryPos;
        }

        public boolean hasPhylactery() {
            return phylacteryPos != null;
        }
        public int getMaxSouls(int rank) {
            if(this.phylacteryPos == null) return 4;
            return HunterBloodlinesConfig.graveboundMaxSouls.get().get(rank - 1);
        }

        /**
         *
         * @param additional - The amount of souls to add
         * @return The number of souls actually used from additional. Excess souls are therefore additional - return value.
         */
        public int addSouls(int additional) {
            if(additional > 0) {
                int used = additional;
                if(additional + this.souls > maxSouls) {
                    used = maxSouls - souls;
                }
                this.souls = Math.min(this.maxSouls, souls + additional);
                return used;
            }
            else if (additional < 0) {
                int used = -additional;
                if (additional + souls < 0) {
                    used = souls;
                }
                this.souls = Math.max(this.souls - used, 0);
                return used;
            }
            return 0;
        }
        public void setSouls(int value) {
            this.souls = value;
        }

        public int getSouls() {
            return this.souls;
        }

        @Override
        public void deserializeUpdateNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
            if(nbt.contains("gravebound_phylactery") && nbt.contains("phylactery_dimension")) {
                Optional<BlockPos> phylacteryPos = NbtUtils.readBlockPos(nbt, "gravebound_phylactery");
                phylacteryPos.ifPresent(blockPos -> {
                    this.phylacteryPos = blockPos;
                    this.phylacteryDimensionID = ResourceLocation.parse(nbt.getString("phylactery_dimension"));

                });
            } else {
                phylacteryPos = null;
                phylacteryDimensionID = null;
            }
            this.mistForm = nbt.getBoolean("gravebound_mist_form");
            this.possession = nbt.getBoolean("gravebound_possession");
            this.souls = nbt.getInt("gravebound_souls");
            this.totalSoulsDevoured = nbt.getInt("gravebound_total_devoured");
            if(possession) {
                this.possessedEntityUUID = nbt.getUUID("gravebound_possession_uuid");
                this.possessedEntityNetworkID = nbt.getInt("gravebound_possession_id");
            } else {
                clearPossession();
            }
        }

        @Override
        public CompoundTag serializeUpdateNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
            if(phylacteryPos != null && phylacteryDimensionID != null) {
                nbt.put("gravebound_phylactery", NbtUtils.writeBlockPos(phylacteryPos));
                nbt.putString("phylactery_dimension", phylacteryDimensionID.toString());
            }
            nbt.putInt("gravebound_souls", this.souls);
            nbt.putInt("gravebound_total_devoured", this.totalSoulsDevoured);
            nbt.putBoolean("gravebound_mist_form", this.mistForm);
            nbt.putBoolean("gravebound_possession", this.possession);
            if(this.possession) {
                nbt.putUUID("gravebound_possession_uuid", this.possessedEntityUUID);
                nbt.putInt("gravebound_possession_id", this.possessedEntityNetworkID);
            }

            return nbt;
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
            if(phylacteryPos != null && phylacteryDimensionID != null) {
                nbt.put("gravebound_phylactery", NbtUtils.writeBlockPos(phylacteryPos));
                nbt.putString("phylactery_dimension", phylacteryDimensionID.toString());
            }
            if(this.possession) {
                nbt.putUUID("gravebound_possession_uuid", this.possessedEntityUUID);
                nbt.putInt("gravebound_possession_id", this.possessedEntityNetworkID);
            }
            nbt.putInt("gravebound_total_devoured", this.totalSoulsDevoured);
            nbt.putInt("gravebound_souls", this.souls);
            nbt.putBoolean("gravebound_mist_form", this.mistForm);
            nbt.putBoolean("gravebound_possession", this.possession);
            return nbt;
        }

        @Override
        public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
            if(nbt.contains("gravebound_phylactery") && nbt.contains("phylactery_dimension")) {
                Optional<BlockPos> phylacteryPos = NbtUtils.readBlockPos(nbt, "gravebound_phylactery");
                phylacteryPos.ifPresent(blockPos -> {
                    this.phylacteryPos = blockPos;
                    this.phylacteryDimensionID = ResourceLocation.parse(nbt.getString("phylactery_dimension"));
                });
            } else {
                phylacteryPos = null;
                phylacteryDimensionID = null;
            }
            this.totalSoulsDevoured = nbt.getInt("gravebound_total_devoured");
            this.souls = nbt.getInt("gravebound_souls");
            this.mistForm = nbt.getBoolean("gravebound_mist_form");
            this.possession = nbt.getBoolean("gravebound_possession");
            if(possession) {
                this.possessedEntityUUID = nbt.getUUID("gravebound_possession_uuid");
                this.possessedEntityNetworkID = nbt.getInt("gravebound_possession_id");
            } else {
                clearPossession();
            }

        }
        public void clear(Level level) {
            tryGetPhylactery().ifPresentOrElse(
                    phylactery -> phylactery.setOwner(null),
                    () -> {
                        if(this.phylacteryPos != null) Bloodlines.LOGGER.warn("Failed to retrieve phylactery BE, could not clear owner {}", player.getName());
                    });
            this.phylacteryPos = null;
            this.souls = 0;
            this.phylacteryDimensionID = null;
            this.totalSoulsDevoured = 0;
            this.clearPossession();
            this.mistForm = false;

        }

        @Override
        public void updateCache(int rank) {
            BloodlinesPlayerAttributes attributes = BloodlinesPlayerAttributes.get(player);
            this.maxSouls = getMaxSouls(rank);
            this.souls = Math.min(souls, maxSouls);
            SpecialAttributes specialAtts = attributes.getGraveboundData();
            specialAtts.maxSouls = maxSouls;
            specialAtts.souls = souls;
            specialAtts.hasPhylactery = hasPhylactery();
            specialAtts.mistForm = mistForm;
            if(possession && possessedEntityNetworkID != -1) {
                updatePossessed(specialAtts);
            } else {
                specialAtts.possessionActive = false;
            }
        }
        private void updatePossessed(SpecialAttributes specialAtts) {
            specialAtts.possessionActive = true;
            Entity entity = player.level().getEntity(this.possessedEntityNetworkID);
            if(entity instanceof LivingEntity living) {
                specialAtts.possessedEntity = living;
                if(living instanceof IPossessedEntity possessed) {
                    possessed.bloodlines$setPossessed(player);
                }

            } else {
                specialAtts.possessionActive = false;
            }
        }
    }



    public static class SpecialAttributes {
        public int souls;
        public int maxSouls;
        public boolean hasPhylactery;

        public boolean critStrikeActive;
        public boolean hasPowerfulDevour;
        public boolean mistForm;
        public boolean ghostWalk;
        public boolean soulClaimingActive;
        public boolean cheaperResurrection;
        public boolean undeadLord;
        public boolean fasterResurrection;
        public boolean passiveSoulClaiming;


        public boolean poisonImmunity;
        public boolean poisonHealing;
        public float soulSpeed;

        public DamageSource lastDamageSource;

        public boolean possessionActive;
        public LivingEntity possessedEntity;
    }

}
