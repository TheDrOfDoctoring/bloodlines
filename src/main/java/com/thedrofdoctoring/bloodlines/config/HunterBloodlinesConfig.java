package com.thedrofdoctoring.bloodlines.config;

import de.teamlapen.lib.lib.util.UtilLib;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;
@SuppressWarnings("unchecked")
public class HunterBloodlinesConfig {

    public static final ModConfigSpec HUNTER_BLOODLINES_CONFIG;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> graveboundBlRank1DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> graveboundBlRank2DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> graveboundBlRank3DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> graveboundBlRank4DefaultSkills;
    public static final ModConfigSpec.BooleanValue graveboundUniqueUnlock;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> graveboundMaxSouls;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> devourSoulCooldownsSeconds;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> graveboundTradePricesMultiplier;
    public static final ModConfigSpec.IntValue playerAliveTimeForDevour;
    public static final ModConfigSpec.BooleanValue graveboundUndeadMobsignore;
    public static final ModConfigSpec.BooleanValue graveboundVulnerableDamageTypeMistForm;
    public static final ModConfigSpec.IntValue noSprintSoulCount;
    public static final ModConfigSpec.IntValue slowRegenSoulCount;
    public static final ModConfigSpec.IntValue soulInfusionSoulRequirement;
    public static final ModConfigSpec.IntValue soulInfusionCooldown;
    public static final ModConfigSpec.IntValue soulInfusionDuration;
    public static final ModConfigSpec.DoubleValue graveboundPowerfulDevourMaxHealthChange;
    public static final ModConfigSpec.IntValue sorcerousStrikeDuration;
    public static final ModConfigSpec.IntValue sorcerousStrikeCooldown;
    public static final ModConfigSpec.IntValue sorcerousStrikeWitherDuration;
    public static final ModConfigSpec.DoubleValue sorcerousStrikeAdditionalDamageMultiplier;
    public static final ModConfigSpec.IntValue lingeringDevourCooldown;
    public static final ModConfigSpec.IntValue lingeringDevourSoulCost;
    public static final ModConfigSpec.IntValue lingeringDevourDuration;
    public static final ModConfigSpec.IntValue soulClaimingCooldown;
    public static final ModConfigSpec.IntValue soulClaimingDuration;
    public static final ModConfigSpec.IntValue immortalityGraveboundRank;
    public static final ModConfigSpec.IntValue mistFormDuration;
    public static final ModConfigSpec.IntValue mistFormCooldown;
    public static final ModConfigSpec.IntValue reducedMistFormCooldown;

    public static final ModConfigSpec.DoubleValue mistFormFlightSpeed;
    public static final ModConfigSpec.IntValue phylacteryTeleportCooldown;
    public static final ModConfigSpec.IntValue phylacteryTeleportRegularSoulCost;
    public static final ModConfigSpec.IntValue phylacteryTeleportMistFormSoulCost;
    public static final ModConfigSpec.IntValue ghostWalkCooldown;
    public static final ModConfigSpec.IntValue ghostWalkSoulCost;
    public static final ModConfigSpec.IntValue ghostWalkDuration;
    public static final ModConfigSpec.IntValue phylacterySoulTransferActionCooldown;
    public static final ModConfigSpec.IntValue possessionSoulCost;
    public static final ModConfigSpec.IntValue possessionDuration;
    public static final ModConfigSpec.IntValue possessionCooldown;
    public static final ModConfigSpec.IntValue possessionSwapCooldown;
    public static final ModConfigSpec.IntValue possessionSwapSoulCost;


    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> phylacteryMaxStorageTiers;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> phylacteryMaxStorageTierRequirements;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> graveboundRankExperienceMult;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> mistFormSoulRequirement;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> reducedMistFormSoulRequirement;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> graveboundMagicDamageMultiplier;
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> graveboundSoulSpeedMultiplier;



    public static final ModConfigSpec.ConfigValue<List<? extends String>>[] graveboundDefaults = new ModConfigSpec.ConfigValue[4];
    static {
        ModConfigSpec.Builder HUNTER_BUILDER = new ModConfigSpec.Builder();

        HUNTER_BUILDER.push("gravebound_bloodline");

        HUNTER_BUILDER.push("gravebound_mobs");
        HUNTER_BUILDER.pop();
        graveboundUniqueUnlock = HUNTER_BUILDER.comment("Whether the Gravebound Bloodline's unique method of joining the bloodline is available. If disabled, then add a recipe for the bloodline fang or add another way to become a member of the bloodline.").define("graveboundUniqueUnlock", true);

        graveboundDefaults[0] = graveboundBlRank1DefaultSkills = HUNTER_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 1").defineList("graveboundBlRank1DefaultSkills", List.of("bloodlines:gravebound"), () -> "bloodlines:gravebound", string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        graveboundDefaults[1] = graveboundBlRank2DefaultSkills = HUNTER_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 2").defineList("graveboundBlRank2DefaultSkills", List.of("bloodlines:gravebound_rank_2"), () -> "bloodlines:gravebound", string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        graveboundDefaults[2] = graveboundBlRank3DefaultSkills = HUNTER_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 3").defineList("graveboundBlRank3DefaultSkills", List.of("bloodlines:gravebound_rank_3"), () -> "bloodlines:gravebound",string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        graveboundDefaults[3] = graveboundBlRank4DefaultSkills = HUNTER_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 4").defineList("graveboundBlRank4DefaultSkills", List.of("bloodlines:gravebound_rank_4"), () -> "bloodlines:gravebound",string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));

        graveboundMaxSouls = HUNTER_BUILDER.comment("Max souls for a gravebound at each Bloodline Rank").defineList("graveboundMaxSouls", Arrays.asList(8, 12, 16, 20), () -> 10, t -> t instanceof Integer);
        devourSoulCooldownsSeconds = HUNTER_BUILDER.comment("Devour Soul action cooldown at each Bloodline Rank").defineList("devourSoulCooldownsSeconds", Arrays.asList(75, 60, 40, 20), () -> 10, t -> t instanceof Integer);
        playerAliveTimeForDevour = HUNTER_BUILDER.comment("Amount of time a player must be alive before they can be devoured, in ticks").defineInRange("playerAliveTimeForDevour",  24000, 0, Integer.MAX_VALUE);
        phylacteryMaxStorageTiers = HUNTER_BUILDER.comment("Max Soul Storage for each Phylactery tier, based on total souls devoured, from phylacteryMaxStorageTierRequirements").defineList("phylacteryMaxStorageTiers", List.of(15, 30, 50, 100, 250), () -> 10, t -> t instanceof Integer);
        phylacteryMaxStorageTierRequirements = HUNTER_BUILDER.comment("Total souls devoured requirement for corresponding phylactery tier. ").defineList("phylacteryMaxStorageTierRequirements", Arrays.asList(0, 350, 900, 3500, 10000), () -> 10, t -> t instanceof Integer);
        graveboundVulnerableDamageTypeMistForm = HUNTER_BUILDER.comment("Whether the Gravebound vulnerable damage types prevent them from entering Mist Form").define("graveboundVulnerableDamageTypeMistForm", true);

        graveboundUndeadMobsignore = HUNTER_BUILDER.comment("Whether Undead mobs can ignore members of the Gravebound bloodline.").define("graveboundUndeadMobsignore", true);
        noSprintSoulCount = HUNTER_BUILDER.comment("Minimum amount of souls below which you can no longer sprint.").defineInRange("noSprintSoulCount",  3, 0, 50);
        slowRegenSoulCount = HUNTER_BUILDER.comment("Minimum amount of souls above which you regenerate health slowly").defineInRange("slowRegenSoulCount",  16, 0, 50);
        soulInfusionCooldown = HUNTER_BUILDER.comment("Cooldown for Soul Infusion action in seconds").defineInRange("soulInfusionCooldown",  30, 0, Integer.MAX_VALUE);
        soulInfusionDuration = HUNTER_BUILDER.comment("Duration for Soul Infusion action in seconds").defineInRange("soulInfusionDuration",  10, 0, Integer.MAX_VALUE);
        soulInfusionSoulRequirement = HUNTER_BUILDER.comment("Souls required and drained by Soul Infusion action").defineInRange("soulInfusionSoulRequirement",  4, 0, 50);
        graveboundTradePricesMultiplier = HUNTER_BUILDER.comment("Trade Prices increase/decrease for each bloodline rank").defineList("graveboundTradePricesMultiplier", Arrays.asList(1.25d, 1.5d, 1.75d, 2d), () -> 0.5d,t -> t instanceof Double);
        graveboundPowerfulDevourMaxHealthChange = HUNTER_BUILDER.comment("The amount that the minimum remaining max health percentage to devour a soul is increased by with the powerful devour skill.").defineInRange("graveboundPowerfulDevourMaxHealthChange",  0.15, 0, 1);
        sorcerousStrikeCooldown = HUNTER_BUILDER.comment("Cooldown for Sorcerous Strike action in seconds").defineInRange("sorcerousStrikeCooldown",  90, 0, Integer.MAX_VALUE);
        sorcerousStrikeDuration = HUNTER_BUILDER.comment("Duration for Sorcerous Strike action in seconds").defineInRange("sorcerousStrikeDuration",  10, 0, Integer.MAX_VALUE);
        sorcerousStrikeWitherDuration = HUNTER_BUILDER.comment("Duration for Sorcerous Strike's wither effect in seconds").defineInRange("sorcerousStrikeWitherDuration",  8, 0, Integer.MAX_VALUE);
        sorcerousStrikeAdditionalDamageMultiplier = HUNTER_BUILDER.comment("The additional damage multiplier added to the existing one for a critical hit with sorcerous strike.").defineInRange("sorcerousStrikeAdditionalDamageMultiplier",  1.25d, 0, 100d);
        lingeringDevourCooldown = HUNTER_BUILDER.comment("Cooldown for Lingering Devour action in seconds").defineInRange("lingeringDevourCooldown",  250, 0, Integer.MAX_VALUE);
        lingeringDevourSoulCost = HUNTER_BUILDER.comment("Soul Cost for Lingering Devour action").defineInRange("lingeringDevourSoulCost",  2, 0, 50);
        lingeringDevourDuration = HUNTER_BUILDER.comment("Duration for Lingering Devour action in seconds").defineInRange("lingeringDevourDuration",  30, 0, Integer.MAX_VALUE);
        graveboundRankExperienceMult = HUNTER_BUILDER.comment("Multiplier for experience gain at each bloodline rank").defineList("graveboundRankExperienceMult", Arrays.asList(0.85d, 0.7d, 0.6d, 0.5d), () -> 0.5d, t -> t instanceof Double);
        graveboundMagicDamageMultiplier = HUNTER_BUILDER.comment("Multiplier for damage taken from magical sources at each bloodline rank").defineList("graveboundMagicDamageMultiplier", Arrays.asList(1.15d, 1.25d, 1.35d, 1.5d), () -> 1.5d, t -> t instanceof Double);

        soulClaimingCooldown = HUNTER_BUILDER.comment("Cooldown for Soul Claiming action in seconds").defineInRange("soulClaimingCooldown",  3000, 0, Integer.MAX_VALUE);
        soulClaimingDuration = HUNTER_BUILDER.comment("Duration for Soul Claiming action in seconds").defineInRange("soulClaimingDuration",  20, 0, Integer.MAX_VALUE);
        immortalityGraveboundRank = HUNTER_BUILDER.comment("Rank required for a Gravebound to utilise the immortality mechanic").defineInRange("immortalityGraveboundRank",  3, 0, 4);
        mistFormDuration = HUNTER_BUILDER.comment("Duration of mist form, in seconds, before death").defineInRange("mistFormDuration", 20, 0, Integer.MAX_VALUE);
        mistFormCooldown = HUNTER_BUILDER.comment("Cooldown of mist form, in seconds, before death").defineInRange("mistFormCooldown",  90, 0, Integer.MAX_VALUE);
        mistFormSoulRequirement = HUNTER_BUILDER.comment("Required number of souls to enter Mist Form and resurrect death from each bloodline rank.").defineList("mistFormSoulRequirement", Arrays.asList(15, 12, 8, 5), () -> 15, t -> t instanceof Integer);
        reducedMistFormSoulRequirement = HUNTER_BUILDER.comment("Required number of souls to enter Mist Form and resurrect death from each bloodline rank, with reduction skill.").defineList("reducedMistFormSoulRequirement", Arrays.asList(10, 8, 6, 3), () -> 15, t -> t instanceof Integer);
        reducedMistFormCooldown = HUNTER_BUILDER.comment("Cooldown of mist form, in seconds, before death, with reduction skill").defineInRange("reducedMistFormCooldown",  30, 0, Integer.MAX_VALUE);

        mistFormFlightSpeed = HUNTER_BUILDER.comment("Flight speed of Mist Form").defineInRange("mistFormFlightSpeed",  0.015f, 0.001, 0.5);
        phylacteryTeleportCooldown = HUNTER_BUILDER.comment("Cooldown of Phylactery Teleport action, in seconds").defineInRange("phylacteryTeleportCooldown", 300, 0, Integer.MAX_VALUE);
        phylacteryTeleportMistFormSoulCost = HUNTER_BUILDER.comment("Soul Cost of Phylactery Teleport action whilst in Mist Form").defineInRange("phylacteryTeleportMistFormSoulCost", 6, 0, 50);
        phylacteryTeleportRegularSoulCost = HUNTER_BUILDER.comment("Regular Soul Cost of Phylactery Teleport action").defineInRange("phylacteryTeleportRegularSoulCost", 4, 0, 50);
        ghostWalkCooldown = HUNTER_BUILDER.comment("Cooldown for Ghost Walk action in seconds").defineInRange("ghostWalkCooldown",  400, 0, Integer.MAX_VALUE);
        ghostWalkSoulCost = HUNTER_BUILDER.comment("Soul Cost for Ghost Walk action").defineInRange("ghostWalkSoulCost",  6, 0, 50);
        ghostWalkDuration = HUNTER_BUILDER.comment("Duration for Ghost Walk action in seconds").defineInRange("ghostWalkDuration",  20, 0, Integer.MAX_VALUE);
        phylacterySoulTransferActionCooldown = HUNTER_BUILDER.comment("Cooldown for Phylactery Soul Transfer action in seconds").defineInRange("phylacterySoulTransferActionCooldown",  30, 0, Integer.MAX_VALUE);
        graveboundSoulSpeedMultiplier = HUNTER_BUILDER.comment("Multiplier of movement speed on soul sand / soul soil for each gravebound rank. Requires skill").defineList("graveboundSoulSpeedMultiplier", Arrays.asList(1.15d, 1.2d, 1.25d, 1.35d), () -> 1.0d, t -> t instanceof Double);
        possessionCooldown = HUNTER_BUILDER.comment("Cooldown for Possession action in seconds").defineInRange("possessionCooldown",  120, 0, Integer.MAX_VALUE);
        possessionSoulCost = HUNTER_BUILDER.comment("Soul Cost for Possession action").defineInRange("possessionSoulCost",  3, 0, 50);
        possessionDuration = HUNTER_BUILDER.comment("Duration for Possession action in seconds").defineInRange("possessionDuration",  100, 0, Integer.MAX_VALUE);
        possessionSwapCooldown = HUNTER_BUILDER.comment("Cooldown for Possession Swap action in seconds").defineInRange("possessionSwapCooldown",  20, 0, Integer.MAX_VALUE);
        possessionSwapSoulCost = HUNTER_BUILDER.comment("Soul Cost for Possession Swap action").defineInRange("possessionSwapSoulCost",  2, 0, 50);
        HUNTER_BUILDER.pop();

        HUNTER_BLOODLINES_CONFIG = HUNTER_BUILDER.build();
    }
}
