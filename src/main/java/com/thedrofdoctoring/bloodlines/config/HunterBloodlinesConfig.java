package com.thedrofdoctoring.bloodlines.config;

import de.teamlapen.lib.lib.util.UtilLib;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
@SuppressWarnings("unchecked")
public class HunterBloodlinesConfig {

    public static ModConfigSpec HUNTER_BLOODLINES_CONFIG;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> graveboundBlRank1DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> graveboundBlRank2DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> graveboundBlRank3DefaultSkills;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> graveboundBlRank4DefaultSkills;


    public static final ModConfigSpec.ConfigValue<List<? extends String>>[] graveboundDefaults = new ModConfigSpec.ConfigValue[4];
    static {
        ModConfigSpec.Builder HUNTER_BUILDER = new ModConfigSpec.Builder();

        HUNTER_BUILDER.push("gravebound_bloodline");

        HUNTER_BUILDER.push("gravebound_mobs");
        HUNTER_BUILDER.pop();
        graveboundDefaults[0] = graveboundBlRank1DefaultSkills = HUNTER_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 1").defineList("graveboundBlRank1DefaultSkills", List.of("bloodlines:gravebound"), () -> "bloodlines:gravebound", string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        graveboundDefaults[1] = graveboundBlRank2DefaultSkills = HUNTER_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 2").defineList("graveboundBlRank2DefaultSkills", List.of("bloodlines:gravebound_rank_2"), () -> "bloodlines:gravebound", string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        graveboundDefaults[2] = graveboundBlRank3DefaultSkills = HUNTER_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 3").defineList("graveboundBlRank3DefaultSkills", List.of("bloodlines:gravebound_rank_3"), () -> "bloodlines:gravebound",string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));
        graveboundDefaults[3] = graveboundBlRank4DefaultSkills = HUNTER_BUILDER.comment("Bloodline Skills that are enabled by default upon reaching Rank 4").defineList("graveboundBlRank4DefaultSkills", List.of("bloodlines:gravebound_rank_4"), () -> "bloodlines:gravebound",string -> string instanceof String && UtilLib.isValidResourceLocation(((String) string)));



        HUNTER_BUILDER.pop();

        HUNTER_BLOODLINES_CONFIG = HUNTER_BUILDER.build();
    }
}
