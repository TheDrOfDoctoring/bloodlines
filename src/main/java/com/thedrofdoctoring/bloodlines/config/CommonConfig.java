package com.thedrofdoctoring.bloodlines.config;

import de.teamlapen.lib.lib.util.UtilLib;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {

    public static ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec.BooleanValue defaultNotManuallyUnlockable;
    public static final ModConfigSpec.IntValue sunTicksPerIncrease;
    public static final ModConfigSpec.IntValue celerityCooldown;
    public static final ModConfigSpec.IntValue celerityDuration;
    public static final ModConfigSpec.IntValue celerityStepAssistRank;
    public static final ModConfigSpec.BooleanValue celerityEnabled;
    public static final ModConfigSpec.BooleanValue mesmeriseEnabled;
    public static final ModConfigSpec.IntValue mesmeriseCooldown;
    public static final ModConfigSpec.IntValue mesmeriseDuration;
    public static final ModConfigSpec.BooleanValue nobleRainWeakness;
    public static final ModConfigSpec.BooleanValue nobleWaterWeakness;
    public static final ModConfigSpec.BooleanValue leechingEnabled;
    public static final ModConfigSpec.IntValue leechingCooldown;
    public static final ModConfigSpec.IntValue leechingDuration;
    public static final ModConfigSpec.IntValue bothInvisibilityCooldown;
    public static final ModConfigSpec.IntValue bothInvisibilityDuration;
    public static final ModConfigSpec.DoubleValue leechingMultiplier;
    public static final ModConfigSpec.DoubleValue nobleIntrigueDamageMultiplier;
    public static final ModConfigSpec.DoubleValue nobleBatArmourMultiplier;
    public static final ModConfigSpec.IntValue zealotShadowwalkLightLevel;
    public static final ModConfigSpec.IntValue zealotShaddowwalkMaxDistance;
    public static final ModConfigSpec.IntValue zealotShadowArmourLightLevel;
    public static final ModConfigSpec.IntValue zealotDarkCloakLightLevel;
    public static final ModConfigSpec.IntValue zealotDarkCloakDuration;
    public static final ModConfigSpec.IntValue zealotDarkCloakCooldown;
    public static final ModConfigSpec.DoubleValue zealotWallClimbSpeed;
    public static final ModConfigSpec.IntValue zealotWallClimbCooldown;
    public static final ModConfigSpec.IntValue zealotWallClimbDuration;
    public static final ModConfigSpec.IntValue zealotDoubleSunTickRank;
    public static final ModConfigSpec.IntValue zealotFleshEatingNutrition;
    public static final ModConfigSpec.DoubleValue zealotFleshEatingSaturation;
    public static final ModConfigSpec.IntValue zealotShadowMasteryLightLevel;
    public static final ModConfigSpec.IntValue zealotObscuredPowerLightLevel;
    public static final ModConfigSpec.IntValue zealotObscuredPowerTimerIncrease;
    public static final ModConfigSpec.IntValue nobleFlankCooldown;
    public static final ModConfigSpec.IntValue nobleFlankRange;
    public static final ModConfigSpec.IntValue zealotFrenzyCooldown;
    public static final ModConfigSpec.IntValue zealotFrenzyDuration;
    public static final ModConfigSpec.DoubleValue zealotBrightAreaDamageMultiplier;
    public static final ModConfigSpec.IntValue zealotBrightAreaDamageMultiplierRank;
    public static final ModConfigSpec.IntValue zealotBrightAreaDamageMultiplierLightLevel;
    public static final ModConfigSpec.IntValue zealotPoisonedStrikeDuration;


    public static final ModConfigSpec.IntValue ectothermFishmongerNutrition;
    public static final ModConfigSpec.IntValue ectothermFrozenAttackFreezingAmount;
    public static final ModConfigSpec.IntValue ectothermSlowingAttackSlownessDuration;
    public static final ModConfigSpec.DoubleValue ectothermFishmongerSaturation;
    public static final ModConfigSpec.IntValue ectothermIceLordCooldown;
    public static final ModConfigSpec.IntValue ectothermDolphinLeapCooldown;
    public static final ModConfigSpec.IntValue ectothermDolphinLeapDuration;
    public static final ModConfigSpec.IntValue ectothermHotBiomeReducedMovementSpeedRank;
    public static final ModConfigSpec.IntValue ectothermHotBiomeReducedMaxHealthRank;
    public static final ModConfigSpec.IntValue ectothermColdBiomeIncreasedMovementSpeedRank;
    public static final ModConfigSpec.IntValue ectothermLordofFrostIncreasedDamageRank;
    public static final ModConfigSpec.IntValue ectothermHotBiomeActionCooldownRank;
    public static final ModConfigSpec.DoubleValue ectothermDolphinLeapDistance;
    public static final ModConfigSpec.DoubleValue ectothermTentacleInteractionDistance;
    public static final ModConfigSpec.DoubleValue ectothermLordOfFrostMultiplier;
    public static final ModConfigSpec.IntValue ectothermUnderwaterDurationIncrease;
    public static final ModConfigSpec.IntValue ectothermInkSplashCooldown;
    public static final ModConfigSpec.DoubleValue ectothermHydrodynamicFormSpeedMultiplier;
    public static final ModConfigSpec.IntValue ectothermInkSplashBlindnessLength;


    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleMaxHealthChange;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleAttackSpeedIncrease;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleBloodThirstChange;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleTradePricesMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleNeonatalMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleFasterResurrectionMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleBatSpeedMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleBloodGainDecreaseMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleSpeedMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleBloodGainMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleCeleritySpeedMultipliers;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleIncreasedNonNobleDamage;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleFireDamageMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> nobleMobSpeedIncrease;


    public static final ModConfigSpec.ConfigValue<List<? extends String>> nobleBlRank1DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> nobleBlRank2DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> nobleBlRank3DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> nobleBlRank4DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>>[] nobleDefaults = new ModConfigSpec.ConfigValue[4];
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotBloodExhaustionChange;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotMiningSpeedIncrease;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotMobDamageIncrease;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotTunnelerIncrease;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotSunDamageMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotStoneSpeedMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotShadowMasteryCooldownMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotHexProtectionMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotMiningSpeedMultipliers;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotSerpentSpeedMultipliers;


    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> zealotShadowwalkCooldowns ;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotShadowArmourDamageMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> zealotTradePricesMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> zealotBlRank1DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> zealotBlRank2DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> zealotBlRank3DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> zealotBlRank4DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>>[] zealotDefaults = new ModConfigSpec.ConfigValue[4];

    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermFireDamageMultipliers;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ectothermBlRank1DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ectothermBlRank2DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ectothermBlRank3DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ectothermBlRank4DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermSwimSpeedMultipliers;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> ectothermLordOfFrostDuration ;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> ectothermUnderwaterVisionDistance;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermUnderwaterMiningSpeedMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermHolyWaterDiffusion;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermHotBiomeReducedMovementSpeedMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermHotBiomeReducedMaxHealthAmount;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermLordOfFrostDamageMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermColdBiomeSpeedMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermHotBiomeActionCooldownMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> ectothermMobAdditionalKnockbackResistance;
    public static final ModConfigSpec.ConfigValue<List<? extends String>>[] ectothermDefaults = new ModConfigSpec.ConfigValue[4];

    public static final ModConfigSpec.ConfigValue<List<? extends String>> bloodknightBlRank1DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> bloodknightBlRank2DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> bloodknightBlRank3DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> bloodknightBlRank4DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>>[] bloodknightDefaults = new ModConfigSpec.ConfigValue[4];
    public static final ModConfigSpec.DoubleValue bloodknightVampireMinHealthFeedingPercentage;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> bloodknightBloodThirstChange;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> bloodknightDamageIncrease;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> bloodknightOtherSourceBloodDecrease;
    public static final ModConfigSpec.IntValue bloodknightVampireBloodBottleNutrition;
    public static final ModConfigSpec.DoubleValue bloodknightVampireBloodBottleSaturation;
    public static final ModConfigSpec.DoubleValue bloodknightBloodFrenzyDamageBonus;
    public static final ModConfigSpec.DoubleValue bloodknightBloodFrenzyMaxHealthBonus;
    public static final ModConfigSpec.DoubleValue bloodknightBloodFrenzy2SpeedBonus;
    public static final ModConfigSpec.DoubleValue bloodknightBloodFrenzy2JumpHeightBonus;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> bloodknightBloodFrenzyDurationPerRank;
    public static final ModConfigSpec.DoubleValue bloodknightFeignedVampireMaxHealthFeedingAmount;
    public static final ModConfigSpec.IntValue bloodknightCrimsonLeapCooldown;
    public static final ModConfigSpec.IntValue bloodknightCrimsonLeapDuration;
    public static final ModConfigSpec.IntValue bloodknightCrimsonLeapBloodUse;
    public static final ModConfigSpec.IntValue bloodknightBloodHuntDuration;
    public static final ModConfigSpec.IntValue bloodknightBloodHuntCooldown;
    public static final ModConfigSpec.IntValue bloodknightBloodHuntTimePerBloodLoss;
    public static final ModConfigSpec.IntValue bloodknightBloodHuntBaseBloodCost;
    public static final ModConfigSpec.IntValue bloodknightSanguineInfusionCooldown;
    public static final ModConfigSpec.IntValue bloodknightSanguineInfusionDuration;
    public static final ModConfigSpec.IntValue bloodknightSanguineInfusionTimePerBloodLoss;
    public static final ModConfigSpec.IntValue bloodknightSanguineInfusionBaseBloodCost;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> bloodknightTradePricesMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> bloodknightVampireBonusBloodMult;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> bloodknightVampireBonusSaturationMult;
    public static final ModConfigSpec.DoubleValue bloodknightSanguineInfusionSpeedMult;
    public static final ModConfigSpec.DoubleValue bloodknightSanguineInfusionJumpHeightMult;
    public static final ModConfigSpec.DoubleValue bloodknightSanguineInfusionMiningSpeedMult;
    public static final ModConfigSpec.IntValue bloodknightSappingStrikePlayerDrain;
    public static final ModConfigSpec.IntValue bloodknightSappingStrikeMobDrain;
    public static final ModConfigSpec.DoubleValue bloodknightSappingStrikeSaturation;
    public static final ModConfigSpec.IntValue bloodknightDaywalkerDuration;
    public static final ModConfigSpec.IntValue bloodknightDaywalkerCooldown;
    public static final ModConfigSpec.IntValue bloodknightDaywalkerTimePerBloodLoss;
    public static final ModConfigSpec.IntValue bloodknightDaywalkerBaseBloodCost;
    public static final ModConfigSpec.DoubleValue bloodknightBatSpeedMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> bloodknightVampireDamageMult;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> bloodknightHunterDamageMult;
    public static final ModConfigSpec.IntValue bloodknightBloodExtractionCooldown;
    public static final ModConfigSpec.DoubleValue bloodknightBloodFrenzyAttackSpeedMult;
    public static final ModConfigSpec.IntValue bloodknightHiddenStrikeSlownessDurationPlayer;
    public static final ModConfigSpec.IntValue bloodknightHiddenStrikeWeaknessDurationPlayer;
    public static final ModConfigSpec.IntValue bloodknightHiddenStrikeWeaknessDurationMob;
    public static final ModConfigSpec.IntValue bloodknightHiddenStrikeSlownessDurationMob;

    static {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

        defaultNotManuallyUnlockable = COMMON_BUILDER.comment("Whether default unlocked skills (skills unlocked for advancing in bloodline rank) are not unlockable manually").define("defaultNotUnlockable", true);
        COMMON_BUILDER.comment("When changing any config which contains decimal values, ensure your edited value has a decimal at the end or it may be reset to original.");
        COMMON_BUILDER.comment("Multipliers work differently based on usage, many of them are attributes where you can effectively take the multiplier as 1 + value specified in config. Configs like this will be marked as attribute in description");
        COMMON_BUILDER.push("noble_bloodline");
        COMMON_BUILDER.comment("Noble bloodline configurable values");

        COMMON_BUILDER.push("noble_mobs");
        nobleMobSpeedIncrease = COMMON_BUILDER.comment("Additional speed for Noble Bloodline Mobs").defineList("nobleMobSpeedIncrease", Arrays.asList(0.1d, 0.15d, 0.25d, 0.5d), t -> true);


        COMMON_BUILDER.pop();

        sunTicksPerIncrease = COMMON_BUILDER.comment("When the correct bloodline skill is enabled, how often (amount of ticks) sun damage progress is increased. Integer values only").defineInRange("sunTicksPerIncrease", 3, 2, Integer.MAX_VALUE);
        nobleMaxHealthChange = COMMON_BUILDER.comment("Max Health increase/decrease for each bloodline rank.").defineList("nobleMaxHealthIncrease", Arrays.asList(-2.0d, -4.0d, -4.0d, -8.0d), t -> t instanceof Double);
        nobleAttackSpeedIncrease = COMMON_BUILDER.comment("Noble attack speed increase per rank. Attribute Multiplier").defineList("nobleAttackSpeedIncrease", Arrays.asList(0.1d, 0.15d, 0.2d, 0.25d), t -> t instanceof Double);
        nobleBloodThirstChange = COMMON_BUILDER.comment("Blood Thirst increase/decrease for each bloodline rank. Attribute Multiplier").defineList("nobleBloodThirstChange", Arrays.asList(0.2d, 0.4d, 0.6d, 0.75d), t -> t instanceof Double);
        nobleNeonatalMultiplier = COMMON_BUILDER.comment("Neonatal timer increase/decrease for each bloodline rank. Attribute Multiplier. ").defineList("nobleNeonatalMultiplier", Arrays.asList(0d, -0.8d, -0.65d, -0.4d), t -> t instanceof Double);
        nobleFasterResurrectionMultiplier = COMMON_BUILDER.comment("Resurrection timer increase/decrease for each bloodline rank. Attribute Multiplier. ").defineList("nobleResurrectionTimerMultiplier", Arrays.asList(0d, -0.8d, -0.65d, -0.4d), t -> t instanceof Double);
        nobleTradePricesMultiplier = COMMON_BUILDER.comment("Trade Prices increase/decrease for each bloodline rank. Attribute Multiplier").defineList("nobleTradePricesMultiplier", Arrays.asList(0.8d, 0.75d, 0.5d, 0.35d), t -> t instanceof Double);
        nobleBloodGainDecreaseMultiplier = COMMON_BUILDER.comment("Multiplier for blood gain decrease when gaining blood from human hearts or other Vampirism Blood Food Items").defineList("nobleBloodGainDecreaseMultiplier", Arrays.asList(0.7d, 0.6d, 0.5d, 0.35d), t -> t instanceof Double);
        nobleBloodGainMultiplier = COMMON_BUILDER.comment("Multiplier for blood gain with noble blood drain skill when draining directly from mobs").defineList("nobleBloodMultiplier", Arrays.asList(1d, 1.25d, 1.5d, 1.8d), t -> t instanceof Double);
        nobleBatSpeedMultiplier = COMMON_BUILDER.comment("Multiplier for bat speed with the bat speed increase skill").defineList("nobleBatSpeedMultiplier", Arrays.asList(1d, 1.1d, 1.2d, 1.4d), t -> t instanceof Double);
        nobleSpeedMultiplier = COMMON_BUILDER.comment("Multiplier for speed from speed boost skill. Attribute Multiplier").defineList("nobleSpeedMultiplier", Arrays.asList(0d, 0.05d, 0.1d, 0.2d), t -> true);

        nobleCeleritySpeedMultipliers = COMMON_BUILDER.comment("Multiplier for speed from celerity action. Attribute Multiplier").defineList("nobleCeleritySpeedMultiplier", Arrays.asList(0d, 0.2d, 0.4d, 0.6d), t -> t instanceof Double);
        celerityEnabled = COMMON_BUILDER.comment("Whether celerity action is enabled").define("celerityEnabled", true);
        celerityCooldown = COMMON_BUILDER.comment("Celerity action cooldown in seconds").defineInRange("celerityCooldown", 60, 1, Integer.MAX_VALUE);
        celerityDuration = COMMON_BUILDER.comment("Celerity action duration in seconds").defineInRange("celerityDuration", 5, 1, Integer.MAX_VALUE);
        celerityStepAssistRank = COMMON_BUILDER.comment("Bloodline Rank at which celerity action gives Step Assist. Set to 6 to disable").defineInRange("celerityStepAssistRank", 3, 1, Integer.MAX_VALUE);
        mesmeriseEnabled = COMMON_BUILDER.comment("Whether mesmerise action is enabled").define("mesmeriseEnabled", true);
        mesmeriseCooldown = COMMON_BUILDER.comment("Mesmerise action cooldown in seconds").defineInRange("mesmeriseCooldown", 90, 1, Integer.MAX_VALUE);
        mesmeriseDuration = COMMON_BUILDER.comment("Mesmerise action duration in seconds").defineInRange("mesmeriseDuration", 20, 1, Integer.MAX_VALUE);
        leechingEnabled = COMMON_BUILDER.comment("Whether leeching action is enabled").define("leechingEnabled", true);
        leechingCooldown = COMMON_BUILDER.comment("Leeching action cooldown in seconds").defineInRange("leechingCooldown", 120, 1, Integer.MAX_VALUE);
        leechingDuration = COMMON_BUILDER.comment("Leeching action duration in seconds").defineInRange("leechingDuration", 10, 1, Integer.MAX_VALUE);
        leechingMultiplier = COMMON_BUILDER.comment("Leeching multiplier, how much the damage is multiplied to get exhaustion amount").defineInRange("leechingMultiplier", 0.25d, 0.01, 1000);
        nobleRainWeakness = COMMON_BUILDER.comment("Whether rain weakens noble vampires").define("nobleRainWeakness", true);
        nobleWaterWeakness = COMMON_BUILDER.comment("Whether flowing water further weakens noble vampires").define("nobleWaterWeakness", true);
        nobleIncreasedNonNobleDamage = COMMON_BUILDER.comment("Damage multiplier for damage from non-noble bloodline vampires").defineList("nonNobleDamageMultiplier", Arrays.asList(1d, 1d, 1d, 1.5d), t -> t instanceof Double);
        nobleFireDamageMultiplier = COMMON_BUILDER.comment("Damage multiplier when taking fire damage").defineList("nobleFireDamageMultiplier", Arrays.asList(1d, 1d, 0.8d, 0.5d), t -> t instanceof Double);
        bothInvisibilityCooldown = COMMON_BUILDER.comment("Invisibility cooldown when a player has both noble and regular vampire invisibility actions unlocked").defineInRange("bothInvisiblityCooldown", 10, 1, Integer.MAX_VALUE);
        bothInvisibilityDuration = COMMON_BUILDER.comment("Invisibility duration when a player has both noble and regular vampire invisibility actions unlocked").defineInRange("bothInvisibilityDuration", 45, 1, Integer.MAX_VALUE);
        nobleIntrigueDamageMultiplier = COMMON_BUILDER.comment("How much damage is multiplied by when attacking from behind with intrigue skill").defineInRange("intrigueMultiplier", 1.5d, 0, 1000);
        nobleFlankCooldown = COMMON_BUILDER.comment("Flank cooldown in seconds").defineInRange("nobleFlankCooldown", 15, 1, Integer.MAX_VALUE);
        nobleFlankRange = COMMON_BUILDER.comment("Flank max distance").defineInRange("nobleFlankMaxDistance", 200, 1, 1000);
        nobleBatArmourMultiplier = COMMON_BUILDER.comment("Multiplier for armour in bat form with requisite skill").defineInRange("nobleBatArmourMultiplier", -0.45d, -1d, 1d);

        nobleDefaults[0] = nobleBlRank1DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 1").defineList("nobleBLRank1DefaultSkills", List.of("bloodlines:noble"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));;
        nobleDefaults[1] = nobleBlRank2DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 2").defineList("nobleBLRank2DefaultSkills", List.of("bloodlines:noble_rank_2"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        nobleDefaults[2] = nobleBlRank3DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 3").defineList("nobleBLRank3DefaultSkills", List.of("bloodlines:noble_rank_3"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        nobleDefaults[3] = nobleBlRank4DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 4").defineList("nobleBLRank4DefaultSkills", List.of("bloodlines:noble_rank_4"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));



        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("zealot_bloodline");
        COMMON_BUILDER.comment("Deep Zealots bloodline configurable values");

        COMMON_BUILDER.push("zealot_mobs");
        zealotMobDamageIncrease = COMMON_BUILDER.comment("Zealot Mob Damage Increase for each rank").defineList("zealotMobDamageIncrease", Arrays.asList(1d, 1.5d, 3d, 4.5d), t -> t instanceof Double);
        COMMON_BUILDER.pop();

        zealotBloodExhaustionChange = COMMON_BUILDER.comment("Blood exhaustion change for each bloodline rank, Attribute Multiplier").defineList("zealotBloodExhaustionChange", Arrays.asList(-0.15d, -0.3, -0.4d, -0.5d), t -> t instanceof Double);
        zealotMiningSpeedIncrease = COMMON_BUILDER.comment("Mining speed multiplier for each zealot rank").defineList("zealotMiningSpeed", Arrays.asList(1.1, 1.25d, 1.35d, 1.5d), t -> t instanceof Double);
        zealotTunnelerIncrease = COMMON_BUILDER.comment("Mining speed increase for each rank when mining stone").defineList("zealotTunnelerIncrease", Arrays.asList(5d, 10d, 15d, 20d), t -> t instanceof Double);
        zealotStoneSpeedMultiplier = COMMON_BUILDER.comment("Multiplier of movement speed on stone for each zealot rank. Requires skill").defineList("zealotStoneSpeed", Arrays.asList(1.075d, 1.125d, 1.2d, 1.25d), t -> t instanceof Double);
        zealotSunDamageMultiplier = COMMON_BUILDER.comment("Multiplier of sun damage for each zealot rank.").defineList("zealotSunDamageMultiplier", Arrays.asList(0.5d, 1d, 1.5d, 2d), t -> t instanceof Double);
        zealotShadowwalkLightLevel = COMMON_BUILDER.comment("Maximum Light Level for shadowwalk to work").defineInRange("zealotShadowwalkLightLevel", 6, 0, 15);
        zealotShaddowwalkMaxDistance = COMMON_BUILDER.comment("Maximum distance for shadowwalk").defineInRange("zealotShadowwalkmaxDistance", 25, 1, 1000);
        zealotShadowwalkCooldowns = COMMON_BUILDER.comment("Shaddowwalk cooldown at each rank, in seconds").defineList("shadowwalkCooldown", Arrays.asList(15, 8, 5, 3), t -> t instanceof Integer);
        zealotShadowArmourDamageMultiplier = COMMON_BUILDER.comment("Damage multiplier for shadow armour depending on BL Rank").defineList("zealotShadowArmourDamageMultiplier", Arrays.asList(0.9d, 0.95d, 0.85d, 0.8d), t -> t instanceof Double);
        zealotShadowArmourLightLevel = COMMON_BUILDER.comment("Maximum Light Level for shadow armour to work").defineInRange("zealotShadowarmourLightLevel", 8, 0, 15);
        zealotDarkCloakLightLevel = COMMON_BUILDER.comment("Maximum Light Level for dark cloak to work").defineInRange("zealotDarkCloakLightLevel", 10, 0, 15);
        zealotDarkCloakCooldown = COMMON_BUILDER.comment("Dark cloak cooldown").defineInRange("zealotDarkCloakCooldown", 0, 0, Integer.MAX_VALUE);
        zealotDarkCloakDuration = COMMON_BUILDER.comment("Dark cloak duration").defineInRange("zealotDarkCloakDuration", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        zealotWallClimbSpeed = COMMON_BUILDER.comment("Wall Climb Vertical Speed").defineInRange("zealotWallClimbSpeed", 0.3, 0, 1);
        zealotWallClimbCooldown = COMMON_BUILDER.comment("Wall Climb cooldown").defineInRange("zealotWallClimbCooldown", 20, 0, Integer.MAX_VALUE);
        zealotWallClimbDuration = COMMON_BUILDER.comment("Wall Climb duration").defineInRange("zealotWallClimbDuration", 15, 0, Integer.MAX_VALUE);
        zealotDoubleSunTickRank = COMMON_BUILDER.comment("Rank at which ticks from sun are increased more quickly").defineInRange("zealotDoubleSunTickRank", 3, 0, 4);
        zealotFleshEatingNutrition = COMMON_BUILDER.comment("How much nutrition (blood) is gained from eating rotten flesh with the flesh eating perk").defineInRange("zealotFleshEatingNutrition", 5, 0, 20);
        zealotFleshEatingSaturation = COMMON_BUILDER.comment("How much saturation gained from eating rotten flesh with the flesh eating perk").defineInRange("zealotFleshEatingSaturation", 0.5, 0, 5);
        zealotObscuredPowerLightLevel = COMMON_BUILDER.comment("Maximum light level that shadow mastery works at").defineInRange("zealotObscuredPowerLightLevel", 10, 0, 15);
        zealotObscuredPowerTimerIncrease = COMMON_BUILDER.comment("How much the timer replenishes every 10 ticks with obscured power. By default this means the timer will simply not decrease, but not increase either").defineInRange("zealotObscuredPowerTimerIncrease", 10, 0, 1000);
        zealotShadowMasteryLightLevel = COMMON_BUILDER.comment("Maximum light level that shadow mastery works at").defineInRange("zealotShadowMasteryLightLevel", 8, 0, 15);
        zealotTradePricesMultiplier = COMMON_BUILDER.comment("Trade Prices increase/decrease for each bloodline rank. Multiplier").defineList("zealotTradePricesMultiplier", Arrays.asList(0.8d, 0.7d, 0.6d, 0.5d), t -> t instanceof Double);
        zealotShadowMasteryCooldownMultiplier = COMMON_BUILDER.comment("Multiplier for action cooldownsa affected by Shadow Mastery action").defineList("zealotShadowMasteryCooldownMultiplier", Arrays.asList(1d, 1.25d, 1.5d, 2d), t -> t instanceof Double);
        zealotHexProtectionMultiplier = COMMON_BUILDER.comment("Multiplier for damage from magical sources").defineList("zealotHexProtectionMultiplier", Arrays.asList(0.9d, 0.85d, 0.85d, 0.75d), t -> t instanceof Double);
        zealotFrenzyCooldown = COMMON_BUILDER.comment("Frenzy cooldown, seconds").defineInRange("zealotFrenzyCooldown", 60, 0, Integer.MAX_VALUE);
        zealotFrenzyDuration = COMMON_BUILDER.comment("Frenzy duration, seconds").defineInRange("zealotFrenzyDuration", 15, 0, Integer.MAX_VALUE);
        zealotSerpentSpeedMultipliers = COMMON_BUILDER.comment("Crawling or shifting speed multiplier for Serpent Skill, at each bloodline rank. Attribute Multiplier").defineList("zealotSerpentSpeedMultipliers", Arrays.asList(0.1d, 0.15d, 0.2d, 0.25d), t -> t instanceof Double);
        zealotMiningSpeedMultipliers = COMMON_BUILDER.comment("Frenzy mining speed multipliers. Attribute Multiplier").defineList("zealotMiningSpeedMultipliers", Arrays.asList(0.1d, 0.15d, 0.2d, 0.25d), t -> t instanceof Double);
        zealotBrightAreaDamageMultiplier = COMMON_BUILDER.comment("Damage taken in bright areas at specificed Bloodline Rank").defineInRange("zealotBrightAreaDamageMultiplier", 1.2d, 0, Integer.MAX_VALUE);
        zealotBrightAreaDamageMultiplierRank = COMMON_BUILDER.comment("Bloodline Rank required to start taking more damage in bright areas, set to 5 to disable").defineInRange("zealotBrightAreaDamageMultiplierRank",4, 0, 5);
        zealotBrightAreaDamageMultiplierLightLevel = COMMON_BUILDER.comment("Light Level at which more damage is taken").defineInRange("zealotBrightAreaDamageMultiplierLightLevel",14, 0, 15);
        zealotPoisonedStrikeDuration = COMMON_BUILDER.comment("Poisoned strike poison duration, in ticks").defineInRange("zealotPoisonedStrikeDuration", 60, 0, Integer.MAX_VALUE);


        zealotDefaults[0] = zealotBlRank1DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 1").defineList("zealotBLRank1DefaultSkills", List.of("bloodlines:zealot"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));;
        zealotDefaults[1] = zealotBlRank2DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 2").defineList("zealotBLRank2DefaultSkills", List.of("bloodlines:zealot_rank_2"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        zealotDefaults[2] = zealotBlRank3DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 3").defineList("zealotBLRank3DefaultSkills", List.of("bloodlines:zealot_rank_3"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        zealotDefaults[3] = zealotBlRank4DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 4").defineList("zealotBLRank4DefaultSkills", List.of("bloodlines:zealot_rank_4"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));



        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("ectotherm_bloodline");
        COMMON_BUILDER.comment("Ectotherm bloodline configurable values");

        COMMON_BUILDER.push("ectotherm_mobs");
        ectothermMobAdditionalKnockbackResistance = COMMON_BUILDER.comment("Additional knockback resistance for Ectotherm Bloodline mobs").defineList("ectothermMobAdditionalKnockbackResistance", Arrays.asList(0.25d, 0.5d, 0.75d, 1d), t -> t instanceof Double);

        COMMON_BUILDER.pop();

        ectothermIceLordCooldown = COMMON_BUILDER.comment("Ice Lord cooldown").defineInRange("ectothermIceLordCooldown", 15, 0, Integer.MAX_VALUE);
        ectothermUnderwaterVisionDistance = COMMON_BUILDER.comment("Distance you can see clearly underwater at, for each bloodline rank. Set to 0 for normal amount").defineList("ectothermUnderwaterVisionDistance", Arrays.asList(0, 90, 125, 150), t -> t instanceof Integer);
        ectothermFishmongerNutrition = COMMON_BUILDER.comment("How much nutrition (blood) is gained from eating fish with the fishmonger perk").defineInRange("ectothermFishmongerNutrition", 5, 0, 20);
        ectothermFishmongerSaturation = COMMON_BUILDER.comment("How much saturation gained from eating fish with the fishmonger perk").defineInRange("ectothermFishmongerSaturation", 0.5, 0, 5);
        ectothermFireDamageMultipliers = COMMON_BUILDER.comment("How much each BL Rank increases fire damage by").defineList("ectothermFireDamageMultipliers", Arrays.asList(1.25d, 1.35d, 1.5d, 1.75d), t -> t instanceof Double);
        ectothermSwimSpeedMultipliers = COMMON_BUILDER.comment("Swim speed multipliers for each BL rank. Attribute Multiplier").defineList("ectothermSwimSpeedMultipliers", Arrays.asList(0.25d, 0.5d, 0.75d, 1d), t -> t instanceof Double);
        ectothermUnderwaterMiningSpeedMultiplier = COMMON_BUILDER.comment("Underwater mining speed multipliesr at each rank, with requisite skill").defineList("ectothermUnderwaterMiningSpeedMultiplier", Arrays.asList(4d, 8d, 10d, 12d), t -> t instanceof Double);
        ectothermFrozenAttackFreezingAmount = COMMON_BUILDER.comment("How much a crit with freezing attack contributes to freezing").defineInRange("ectothermFrozenAttackFreezingAmount", 25, 0, 10000);
        ectothermSlowingAttackSlownessDuration = COMMON_BUILDER.comment("How long slowness from a crit with slowing attack lasts").defineInRange("ectothermSlowingAttackSlownessDuration", 30, 0, 10000);
        ectothermDolphinLeapCooldown = COMMON_BUILDER.comment("Dolphin Leap Action cooldown, in seconds").defineInRange("ectothermDolphinLeapCooldown", 7, 0, Integer.MAX_VALUE);
        ectothermDolphinLeapDistance = COMMON_BUILDER.comment("Dolphin Leap action distance leaped multiplier").defineInRange("ectothermDolphinLeapDistance", 2.5d, 0d, 1000);
        ectothermDolphinLeapDuration = COMMON_BUILDER.comment("Dolphin Leap Action duration, in seconds").defineInRange("ectothermDolphinDuration", 5, 0, Integer.MAX_VALUE);
        ectothermTentacleInteractionDistance = COMMON_BUILDER.comment("How much extra block interaction range is given by the Tentacle Skill").defineInRange("ectothermTentacleRange", 1.75d, 0, 10);
        ectothermHolyWaterDiffusion = COMMON_BUILDER.comment("Damage multipliers when damaged by Holy Water at each Bloodline Rank, with requisite skill").defineList("ectothermHolyWaterDiffusion", Arrays.asList(0.9d, 0.8d, 0.7d, 0.5d), t -> t instanceof Double);
        ectothermHotBiomeReducedMaxHealthAmount = COMMON_BUILDER.comment("How much each BL Rank reduces max health in hot biomes").defineList("ectothermHotBiomeReducedMaxHealthAmount", Arrays.asList(-1.5d, -2.5d, -3.0d, -3.5d), t -> t instanceof Double);
        ectothermHotBiomeReducedMovementSpeedMultiplier = COMMON_BUILDER.comment("How much each BL Rank reduces movement speed in hot biomes (Attribute Multiplier)").defineList("ectothermHotBiomeReducedMovementSpeedMultiplier", Arrays.asList(-0.05d, -0.1d, -0.15d, -0.25d), t -> t instanceof Double);
        ectothermColdBiomeSpeedMultiplier = COMMON_BUILDER.comment("How much each BL Rank increases fire damage by. Attribute Multiplier").defineList("ectothermColdBiomeSpeedMultiplier", Arrays.asList(0.1d, 0.15d, 0.2d, 0.25d), t -> t instanceof Double);
        ectothermHotBiomeActionCooldownMultiplier = COMMON_BUILDER.comment("How much each BL Rank multiplies the action cooldown by in hot biomes").defineList("ectothermHotBiomeActionCooldownMultiplier", Arrays.asList(1.25d, 1.5d, 2d, 2.5d), t -> t instanceof Double);
        ectothermHotBiomeReducedMovementSpeedRank = COMMON_BUILDER.comment("Rank at which movement speed is reduced in hot biomes. Attribute Multiplier").defineInRange("ectothermHotBiomeReducedMovementSpeedRank", 4, 0, 5);
        ectothermHotBiomeActionCooldownRank = COMMON_BUILDER.comment("Rank at which action cooldown is increased in hot biomes").defineInRange("ectothermHotBiomeActionCooldownRank", 3, 0, 5);
        ectothermHotBiomeReducedMaxHealthRank = COMMON_BUILDER.comment("Rank at which max health is reduced in hot biomes").defineInRange("ectothermHotBiomeReducedMaxHealthRank", 2, 0, 5);
        ectothermColdBiomeIncreasedMovementSpeedRank = COMMON_BUILDER.comment("Rank at which movement speed is increased in cold biomes").defineInRange("ectothermColdBiomeIncreasedMovementSpeedRank", 4, 0, 5);
        ectothermLordOfFrostDuration = COMMON_BUILDER.comment("Lord of Frost action duration at each BL Rank, in seconds").defineList("ectothermFrostDuration", Arrays.asList(15, 30, 60, 120), t -> t instanceof Integer);
        ectothermLordOfFrostMultiplier = COMMON_BUILDER.comment("Duration multiplier for Lord of Frost with requisite skill").defineInRange("ectothermLordOfFrostMultiplier", 2d, 0, 5.0d);
        ectothermLordOfFrostDamageMultiplier = COMMON_BUILDER.comment("How much each BL Rank increases fire damage by").defineList("ectothermLordOfFrostDamageMultiplier", Arrays.asList(1.25d, 1.35d, 1.5d, 1.75d), t -> t instanceof Double);
        ectothermLordofFrostIncreasedDamageRank = COMMON_BUILDER.comment("Rank at which lord of frost damage is increased").defineInRange("ectothermLordofFrostIncreasedDamageRank", 3, 0, 5);
        ectothermUnderwaterDurationIncrease = COMMON_BUILDER.comment("Increase to duration for actions every 10 ticks whilst underwater with requisite skill").defineInRange("ectothermUnderwaterDurationIncrease", 5, 0, 1000);
        ectothermInkSplashCooldown = COMMON_BUILDER.comment("Cooldown for ink splash action in seconds").defineInRange("ectothermInkSplashCooldown", 60, 0, 1000);
        ectothermHydrodynamicFormSpeedMultiplier = COMMON_BUILDER.comment("Hydrodynamic Form underwater speed additive multiplier. Multiplier").defineInRange("ectothermHydrodynamicFormSpeedMultiplier", 2.5d, 0, 10d);
        ectothermInkSplashBlindnessLength = COMMON_BUILDER.comment("Duration of blindness from ink splash, in ticks").defineInRange("ectothermInkSplashBlindnessLength", 150, 0, 1000);


        ectothermDefaults[0] = ectothermBlRank1DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 1").defineList("ectothermBlRank1DefaultSkills", List.of("bloodlines:ectotherm"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));;
        ectothermDefaults[1] = ectothermBlRank2DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 2").defineList("ectothermBlRank2DefaultSkills", List.of("bloodlines:ectotherm_rank_2"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        ectothermDefaults[2] = ectothermBlRank3DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 3").defineList("ectothermBlRank3DefaultSkills", List.of("bloodlines:ectotherm_rank_3"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        ectothermDefaults[3] = ectothermBlRank4DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 4").defineList("ectothermBlRank4DefaultSkills", List.of("bloodlines:ectotherm_rank_4"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));



        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("bloodknight_bloodline");
        COMMON_BUILDER.comment("Bloodknight bloodline configurable values");

        COMMON_BUILDER.push("bloodknight_mobs");

        COMMON_BUILDER.pop();

        bloodknightVampireMinHealthFeedingPercentage = COMMON_BUILDER.comment("The proportion of remaining health that a vampire mob must have, or be below, so that a Bloodknight Vampire can feed on it.").defineInRange("bloodknightVampireMinHealthFeedingProportion", 0.25, 0, 1);

        bloodknightDefaults[0] = bloodknightBlRank1DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 1").defineList("bloodknightBlRank1DefaultSkills", List.of("bloodlines:bloodknight"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));;
        bloodknightDefaults[1] = bloodknightBlRank2DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 2").defineList("bloodknightBlRank2DefaultSkills", List.of("bloodlines:bloodknight_rank_2"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        bloodknightDefaults[2] = bloodknightBlRank3DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 3").defineList("bloodknightBlRank3DefaultSkills", List.of("bloodlines:bloodknight_rank_3"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        bloodknightDefaults[3] = bloodknightBlRank4DefaultSkills = COMMON_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 4").defineList("bloodknightBlRank4DefaultSkills", List.of("bloodlines:bloodknight_rank_4"), string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        bloodknightBloodThirstChange = COMMON_BUILDER.comment("Multipliers for blood exhaustion at each blood knight rank. Attribute Multiplier").defineList("bloodknightBloodThirstChange", Arrays.asList(-0.9d, -0.75d, -0.5d,-0.35d), t -> t instanceof Double);
        bloodknightDamageIncrease = COMMON_BUILDER.comment("Multipliers for damage at each blood knight rank. Attribute Multiplier").defineList("bloodknightDamageIncrease", Arrays.asList(0.1d, 0.15d, 0.2d, 0.25d), t -> t instanceof Double);
        bloodknightOtherSourceBloodDecrease = COMMON_BUILDER.comment("Multiplier for blood gain from non-vampiric blood sources").defineList("bloodknightOtherSourceBloodDecrease", Arrays.asList(0.7d, 0.5d, 0.3d, 0.2d), t -> t instanceof Double);
        bloodknightVampireBloodBottleNutrition = COMMON_BUILDER.comment("Vampire Blood Bottle Nutrition (Blood Gain)").defineInRange("bloodknightVampireBloodBottleNutrition",  18, 0, 100);
        bloodknightVampireBloodBottleSaturation = COMMON_BUILDER.comment("Vampire Blood Bottle Saturation").defineInRange("bloodknightVampireBloodBottleSaturation",  2d, 0d, 10d);
        bloodknightBloodFrenzyDamageBonus = COMMON_BUILDER.comment("Damage bonus increase from the Blood Frenzy effect").defineInRange("bloodknightBloodFrenzyDamageBonus",  3d, 0d, 10d);
        bloodknightBloodFrenzyMaxHealthBonus = COMMON_BUILDER.comment("Max Health increase from the Blood Frenzy effect").defineInRange("bloodknightBloodFrenzyMaxHealthBonus",  4d, 0d, 10d);
        bloodknightBloodFrenzy2SpeedBonus = COMMON_BUILDER.comment("Speed multiplier from the Blood Frenzy tier 2 effect").defineInRange("bloodknightBloodFrenzy2SpeedBonus",  0.15, -5d, 5d);
        bloodknightBloodFrenzy2JumpHeightBonus = COMMON_BUILDER.comment("Jump multiplier  from the Blood Frenzy tier 2 effect").defineInRange("bloodknightBloodFrenzy2JumpHeightBonus",  0.15, -5d, 5d);
        bloodknightBloodFrenzyDurationPerRank = COMMON_BUILDER.comment("Duration of blood frenzy effect, for each BL Rank, in seconds").defineList("bloodknightBloodFrenzyDurationPerRank", Arrays.asList(60, 180, 320, 500, 900), t -> t instanceof Integer);
        bloodknightFeignedVampireMaxHealthFeedingAmount = COMMON_BUILDER.comment("The percentage with Feigned Mercy skill of max health that a vampire must have to begin feeding on it").defineInRange("bloodknightFeignedVampireMaxHealthFeedingAmount",  0.5d, 0d, 1d);
        bloodknightCrimsonLeapCooldown = COMMON_BUILDER.comment("Cooldown for crimson leap action, in ticks").defineInRange("bloodknightCrimsonLeapCooldown",  20, 0, 10000);
        bloodknightCrimsonLeapDuration = COMMON_BUILDER.comment("Duration for crimson leap action, in ticks").defineInRange("bloodknightCrimsonLeapDuration",  50, 0, 10000);
        bloodknightCrimsonLeapBloodUse = COMMON_BUILDER.comment("Blood cost of using crimson leap action").defineInRange("bloodknightCrimsonLeapBloodUse",  8, 0, 50);
        bloodknightSanguineInfusionBaseBloodCost = COMMON_BUILDER.comment("Blood usage upon activating Sanguine Infusion").defineInRange("bloodknightSanguineInfusionBaseBloodCost",  3, 0, 50);
        bloodknightSanguineInfusionCooldown = COMMON_BUILDER.comment("Cooldown for sanguine infusion action, in seconds").defineInRange("bloodknightSanguineInfusionCooldown",  30, 0, 10000);
        bloodknightSanguineInfusionDuration = COMMON_BUILDER.comment("Duration for sangine infusion action, in seconds").defineInRange("bloodknightSanguineInfusionDuration",  3000, 0, 10000);
        bloodknightSanguineInfusionTimePerBloodLoss = COMMON_BUILDER.comment("Amount of ticks it takes for 1 blood to be used whilst action is active").defineInRange("bloodknightSanguineInfusionTimePerBloodLoss",  160, 0, 10000);
        bloodknightSanguineInfusionSpeedMult = COMMON_BUILDER.comment("Speed mult from sanguine infusion").defineInRange("bloodknightSanguineInfusionSpeedMult",  0.75d, 0, 50);
        bloodknightSanguineInfusionJumpHeightMult = COMMON_BUILDER.comment("Jump mult from sanguine infusion").defineInRange("bloodknightSanguineInfusionJumpHeightMult",  0.25d, 0, 50);
        bloodknightSanguineInfusionMiningSpeedMult = COMMON_BUILDER.comment("Speed mult from sanguine infusion with requisite skill").defineInRange("bloodknightSanguineInfusionMiningSpeedMult",  0.2d, 0, 50);
        bloodknightBloodHuntCooldown = COMMON_BUILDER.comment("Cooldown for blood hunt action, in seconds").defineInRange("bloodknightBloodHuntCooldown",  30, 0, 10000);
        bloodknightBloodHuntDuration = COMMON_BUILDER.comment("Duration for blood hunt action, in seconds").defineInRange("bloodknightBloodHuntDuration",  3000, 0, 10000);
        bloodknightBloodHuntTimePerBloodLoss = COMMON_BUILDER.comment("Amount of ticks it takes for 1 blood to be used whilst action is active").defineInRange("bloodknightBloodHuntTimePerBloodLoss",  140, 0, 10000);
        bloodknightBloodHuntBaseBloodCost = COMMON_BUILDER.comment("Blood usage upon activating Blood Hunt").defineInRange("bloodknightBloodHuntBaseBloodCost",  3, 0, 50);
        bloodknightTradePricesMultiplier = COMMON_BUILDER.comment("Trade Prices increase/decrease for each bloodline rank. Multiplier").defineList("bloodknightTradePricesMultiplier", Arrays.asList(1d, 0.8d, 0.7d, 0.6d), t -> t instanceof Double);
        bloodknightVampireBonusBloodMult = COMMON_BUILDER.comment("Increased blood gain when drinking blood from vampires with requisite skill. Multiplier").defineList("bloodknightVampireBonusBloodMult", Arrays.asList(1.5d, 2d, 2.5d, 3d), t -> t instanceof Double);
        bloodknightVampireBonusSaturationMult = COMMON_BUILDER.comment("Increased saturation gain when drinking blood from vampires with requisite skill. Multiplier").defineList("bloodknightVampireBonusSaturationMult", Arrays.asList(1.25d, 1.5d, 1.75d, 2.0d), t -> t instanceof Double);
        bloodknightSappingStrikeMobDrain = COMMON_BUILDER.comment("Blood drain from vampire mobs with sapping strike skill").defineInRange("bloodknightSappingStrikeMobDrain",  4, 0, 40);
        bloodknightSappingStrikePlayerDrain = COMMON_BUILDER.comment("Blood drain from vampire players with sapping strike skill").defineInRange("bloodknightSappingStrikePlayerDrain",  1, 0, 40);
        bloodknightSappingStrikeSaturation = COMMON_BUILDER.comment("Saturation gain from sapping strike skill").defineInRange("bloodknightSappingStrikeSaturation",  0.25d, 0, 10d);
        bloodknightDaywalkerBaseBloodCost = COMMON_BUILDER.comment("Blood usage upon activating Daywalker").defineInRange("bloodknightDaywalkerBaseBloodCost",  5, 0, 50);
        bloodknightDaywalkerCooldown = COMMON_BUILDER.comment("Cooldown for daywalker action, in seconds").defineInRange("bloodknightDaywalkerCooldown",  150, 0, 10000);
        bloodknightDaywalkerDuration = COMMON_BUILDER.comment("Duration for daywalker action, in seconds").defineInRange("bloodknightDaywalkerDuration",  300, 0, 10000);
        bloodknightDaywalkerTimePerBloodLoss = COMMON_BUILDER.comment("Amount of ticks it takes for 1 blood to be used whilst action is active").defineInRange("bloodknightDaywalkerTimePerBloodLoss",  75, 0, 10000);
        bloodknightBatSpeedMultiplier = COMMON_BUILDER.comment("Bat Speed flight multiplier from Frenzied Bat Skill").defineInRange("bloodknightBatSpeedMultiplier", 1.75d, 0d, 100d);
        bloodknightHunterDamageMult = COMMON_BUILDER.comment("Damage received from hunters multiplier for Bloodknights. Multiplier").defineList("bloodknightHunterDamageMult", Arrays.asList(1d, 1d, 1.15d, 1.35), t -> t instanceof Double);
        bloodknightVampireDamageMult = COMMON_BUILDER.comment("Damage dealt to vampires multiplier for Bloodknights. Multiplier").defineList("bloodknightVampireDamageMult", Arrays.asList(1.5d, 2d, 1.25d, 1.5d), t -> t instanceof Double);
        bloodknightBloodExtractionCooldown = COMMON_BUILDER.comment("Cooldown for Blood Extraction action, in seconds").defineInRange("bloodknightBloodExtractionCooldown", 900, 0, 100000);
        bloodknightBloodFrenzyAttackSpeedMult = COMMON_BUILDER.comment("Blood Frenzy attack speed mult with requisite skill. Attribute Multiplier").defineInRange("bloodknightBloodFrenzyAttackSpeedMult", 0.35d, 0d, 10d);
        bloodknightHiddenStrikeWeaknessDurationMob = COMMON_BUILDER.comment("Duration of Hidden Strike weakness on mobs, in ticks").defineInRange("bloodknightHiddenStrikeWeaknessDurationMob", 500, 0, 100000);
        bloodknightHiddenStrikeSlownessDurationMob = COMMON_BUILDER.comment("Duration of Hidden Strike slowness on mobs, in ticks").defineInRange("bloodknightHiddenStrikeSlownessDurationMob", 500, 0, 100000);
        bloodknightHiddenStrikeWeaknessDurationPlayer = COMMON_BUILDER.comment("Duration of Hidden Strike weakness on players, in ticks").defineInRange("bloodknightHiddenStrikeWeaknessDurationPlayer", 200, 0, 100000);
        bloodknightHiddenStrikeSlownessDurationPlayer = COMMON_BUILDER.comment("Duration of Hidden Strike slowness on players, in ticks").defineInRange("bloodknightHiddenStrikeSlownessDurationPlayer", 300, 0, 100000);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

}
