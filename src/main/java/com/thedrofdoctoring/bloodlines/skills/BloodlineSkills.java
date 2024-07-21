package com.thedrofdoctoring.bloodlines.skills;

import com.thedrofdoctoring.bloodlines.BloodlineReference;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.ISpecialAttributes;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.factions.ISkillNode;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.entity.player.skills.SkillNode;
import de.teamlapen.vampirism.entity.player.skills.SkillTree;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class BloodlineSkills {

    public static final HashMap<BloodlineSkillType, ArrayList<ISkill<?>>> bloodlineSkills = new HashMap<>();

    public static final DeferredRegister<ISkill<?>> SKILLS = DeferredRegister.create(VampirismRegistries.Keys.SKILL, Bloodlines.MODID);

    // Root Skills

    public static final DeferredHolder<ISkill<?>, ISkill<IVampirePlayer>> NOBLE_SKILL = SKILLS.register("noble", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, true, 0, false ));
    public static final DeferredHolder<ISkill<?>, ISkill<IVampirePlayer>> ZEALOT_SKILL = SKILLS.register("zealot", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, false));
    public static final DeferredHolder<ISkill<?>,ISkill<?>> ECTOTHERM_SKILL = SKILLS.register("ectotherm", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0).setToggleActions(player -> ((VampirePlayer) player).getSpecialAttributes().waterResistance = true, player -> ((VampirePlayer) player).getSpecialAttributes().waterResistance = false));

    // Zealot Skills

    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_RANK_2 = SKILLS.register("zealot_rank_2", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, false));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_RANK_3 = SKILLS.register("zealot_rank_3", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, false, 0, false));
    public static final DeferredHolder<ISkill<?>, ISkill<IVampirePlayer>> ZEALOT_RANK_4 = SKILLS.register("zealot_rank_4", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, false));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_STONE_SPEED = SKILLS.register("zealot_stone_speed", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_DARKCLOAK = SKILLS.register("zealot_darkcloak", () -> new BloodlineActionSkill<>(BloodlineActions.ZEALOT_DARK_CLOAK_ACTION, 0, true, BloodlineReference.ZEALOT, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_SHADOWWALK = SKILLS.register("zealot_shadowwalk", () -> new BloodlineActionSkill<>(BloodlineActions.ZEALOT_SHADOWWALK_ACTION, 0, true, BloodlineReference.ZEALOT, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_WALL_CLIMB = SKILLS.register("zealot_wall_climb", () -> new BloodlineActionSkill<>(BloodlineActions.ZEALOT_WALL_CLIMB_ACTION, 0, true, BloodlineReference.ZEALOT, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_FALL_DAMAGE = SKILLS.register("zealot_fall_damage", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_FLESH_EATING = SKILLS.register("zealot_flesh_eating", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_TUNNELER = SKILLS.register("zealot_tunneler", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_SPIDER_FRIEND = SKILLS.register("zealot_spider_friend", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_SHADOW_MASTERY = SKILLS.register("zealot_shadow_mastery", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_OBSCURED_POWER = SKILLS.register("zealot_obscured_power", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_HEX_PROTECTION = SKILLS.register("zealot_hex_protection", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ZEALOT_FRENZY = SKILLS.register("zealot_frenzy", () -> new BloodlineActionSkill<>(BloodlineActions.ZEALOT_FRENZY_ACTION, 0, true, BloodlineReference.ZEALOT, true));
    public static final DeferredHolder<ISkill<?>,ISkill<?>> ZEALOT_SHADOW_ARMOUR = SKILLS.register("zealot_shadow_armour", () -> new BloodlineSkill<>(BloodlineReference.ZEALOT, true, 0).setToggleActions(player -> ((ISpecialAttributes)((VampirePlayer) player).getSpecialAttributes()).bloodlines$setShadowArmour(true), player -> ((ISpecialAttributes)((VampirePlayer) player).getSpecialAttributes()).bloodlines$setShadowArmour(false)));

    // Noble Skills

    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_RANK_2 = SKILLS.register("noble_rank_2", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, true, 0, false));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_RANK_3 = SKILLS.register("noble_rank_3", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, true, 0, false));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_RANK_4 = SKILLS.register("noble_rank_4", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, true, 0, false));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_BETTER_TRADE_PRICES = SKILLS.register("noble_better_prices", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, false, 0));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_BETTER_BLOOD_DRAIN = SKILLS.register("noble_better_blood_drain", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, true, 0));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_FASTER_RESURRECT = SKILLS.register("noble_faster_resurrect", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, false, 0));
    public static final DeferredHolder<ISkill<?>,ISkill<?>> NOBLE_MORE_TICKS_IN_SUN = SKILLS.register("noble_more_ticks_in_sun", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, true, 0).setToggleActions(player -> ((ISpecialAttributes)((VampirePlayer) player).getSpecialAttributes()).bloodlines$setSlowSun(true), player -> ((ISpecialAttributes)((VampirePlayer) player).getSpecialAttributes()).bloodlines$setSlowSun(false)));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_BAT_FLIGHT_SPEED = SKILLS.register("noble_bat_flying_speed", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, false, 0));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_FASTER_MOVEMENT_SPEED = SKILLS.register("noble_faster_movement_speed", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, false, 0));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_CHALICE_SKILL = SKILLS.register("noble_bottomless_chalice", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, true, 0));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_ENHANCED_LEECHING = SKILLS.register("noble_enhanced_leeching", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, true, 0));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_INTRIGUE = SKILLS.register("noble_intrigue", () -> new BloodlineSkill<>(BloodlineReference.NOBLE, true, 0));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_CELERITY = SKILLS.register("noble_celerity", () -> new BloodlineActionSkill<>(BloodlineActions.NOBLE_CELERITY_ACTION,0, false, BloodlineReference.NOBLE, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_MESMERISE = SKILLS.register("noble_mesmerise", () -> new BloodlineActionSkill<>(BloodlineActions.NOBLE_MESMERISE_ACTION, 0, true, BloodlineReference.NOBLE, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_LEECHING = SKILLS.register("noble_leeching", () -> new BloodlineActionSkill<>(BloodlineActions.NOBLE_LEECHING_ACTION, 0, true, BloodlineReference.NOBLE, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_INVISIBILITY = SKILLS.register("noble_invisibility", () -> new BloodlineActionSkill<>(BloodlineActions.NOBLE_INVISIBILITY_ACTION, 0, true, BloodlineReference.NOBLE, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> NOBLE_FLANK = SKILLS.register("noble_flank", () -> new BloodlineActionSkill<>(BloodlineActions.NOBLE_FLANK_ACTION, 0, true, BloodlineReference.NOBLE, true));

    //Ectotherm skills

    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ECTOTHERM_RANK_2 = SKILLS.register("ectotherm_rank_2", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0, false) {
            @Override
            protected void getActions(@NotNull Collection<IAction<IVampirePlayer>> list) {
            list.add(BloodlineActions.ECTOTHERM_FROST_LORD_ACTION.get());
        }
    });
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ECTOTHERM_RANK_3 = SKILLS.register("ectotherm_rank_3", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0, false));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ECTOTHERM_RANK_4 = SKILLS.register("ectotherm_rank_4", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0, false));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> FISHMONGER = SKILLS.register("ectotherm_fishmonger", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<?>> ECTOTHERM_REFRACTION = SKILLS.register("ectotherm_refraction", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0).setToggleActions(player -> ((ISpecialAttributes)((VampirePlayer) player).getSpecialAttributes()).bloodlines$setRefraction(true), player -> ((ISpecialAttributes)((VampirePlayer) player).getSpecialAttributes()).bloodlines$setRefraction(false)));
    public static final DeferredHolder<ISkill<?>,ISkill<?>> ECTOTHERM_DIFFUSION = SKILLS.register("ectotherm_diffusion", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0));

    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ECTOTHERM_ICELORD = SKILLS.register("ectotherm_icelord", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<?>> ECTOTHERM_FROST_CONTROL = SKILLS.register("ectotherm_frost_control", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0).setToggleActions(player -> ((ISpecialAttributes)((VampirePlayer) player).getSpecialAttributes()).bloodlines$setFrost(true), player -> ((ISpecialAttributes)((VampirePlayer) player).getSpecialAttributes()).bloodlines$setFrost(false)));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ECTOTHERM_MINING_SPEED_UNDERWATER = SKILLS.register("ectotherm_mining_speed_underwater", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ECTOTHERM_FROZEN_ATTACK = SKILLS.register("ectotherm_frozen_attack", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ECTOTHERM_SLOWNESS_ATTACK = SKILLS.register("ectotherm_slowness_attack", () -> new BloodlineSkill<>(BloodlineReference.ECTOTHERM, true, 0, true));
    public static final DeferredHolder<ISkill<?>,ISkill<IVampirePlayer>> ECTOTHERM_DOLPHIN_LEAP = SKILLS.register("ectotherm_dolphin_leap", () -> new BloodlineActionSkill<>(BloodlineActions.ECTOTHERM_DOLPHIN_LEAP_ACTION, 0, true, BloodlineReference.ECTOTHERM, true));


    //Bloodline Skills are added here so that they can be removed if the player's bloodline changes. Not sure if there's a better way to do this.
    public static void addSkill(ISkill<?> skill, BloodlineSkillType type) {
        if(!bloodlineSkills.containsKey(type)) {
            bloodlineSkills.put(type, new ArrayList<>());
        }
        bloodlineSkills.get(type).add(skill);
    }

    public static ArrayList<ISkill<?>> getBloodlineTypeSkills(BloodlineSkillType type) {
        return bloodlineSkills.get(type);
    }
    public static class Trees {
        public static final ResourceKey<ISkillTree> NOBLE = tree("noble");
        public static final ResourceKey<ISkillTree> ZEALOT = tree("zealot");
        public static final ResourceKey<ISkillTree> ECTOTHERM = tree("ectotherm");


        private static ResourceKey<ISkillTree> tree(String path) {
            return ResourceKey.create(VampirismRegistries.Keys.SKILL_TREE, Bloodlines.rl("vampire/" + path));
        }

        public static void createSkillTrees(BootstrapContext<ISkillTree> context) {
            context.register(ECTOTHERM, new SkillTree(VReference.VAMPIRE_FACTION, EntityPredicate.Builder.entity().subPredicate(new BloodlineSubPredicate(BloodlineReference.ECTOTHERM)).build(), new ItemStack(ModItems.PURE_BLOOD_4.asItem()), Component.translatable("text.bloodlines.skills.ectotherm")));
            context.register(ZEALOT, new SkillTree(VReference.VAMPIRE_FACTION, EntityPredicate.Builder.entity().subPredicate(new BloodlineSubPredicate(BloodlineReference.ZEALOT)).build(), new ItemStack(ModItems.PURE_BLOOD_4.asItem()), Component.translatable("text.bloodlines.skills.zealot")));
            context.register(NOBLE, new SkillTree(VReference.VAMPIRE_FACTION, EntityPredicate.Builder.entity().subPredicate(new BloodlineSubPredicate(BloodlineReference.NOBLE)).build(), new ItemStack(ModItems.PURE_BLOOD_4.asItem()), Component.translatable("text.bloodlines.skills.noble")));
        }
    }
    public static class Nodes {
        public static final ResourceKey<ISkillNode> NOBLE_ROOT = node("root", "noble");
        public static final ResourceKey<ISkillNode> ZEALOT_ROOT = node("root", "zealot");
        public static final ResourceKey<ISkillNode> ECTOTHERM_ROOT = node("root", "ectotherm");

        public static final ResourceKey<ISkillNode> ZEALOT_RANK_2 = node("rank_2", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_RANK_3 = node("rank_3", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_RANK_4 = node("rank_4", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_STONE_SPEED = node("stone_speed", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_SHADOWWALK = node("shadow_walk", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_SHADOWARMOUR = node("shadow_armour", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_DARKCLOAK = node("dark_cloak", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_WALL_CLIMB = node("wall_climb", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_FALL_DAMAGE = node("fall_damage", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_FLESH_ARMOUR = node("flesh_shadow_armour", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_TUNNELER = node("tunneler", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_SPIDER_FRIEND = node("spider_friend", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_SHADOW_MASTERY = node("shadow_mastery", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_OBSCURED_POWER = node("obscured_power", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_HEX_PROTECTION = node("hex_protection", "zealot");
        public static final ResourceKey<ISkillNode> ZEALOT_FRENZY = node("frenzy", "zealot");

        public static final ResourceKey<ISkillNode> ECTOTHERM_RANK_2 = node("rank_2", "ectotherm");
        public static final ResourceKey<ISkillNode> ECTOTHERM_RANK_3 = node("rank_3", "ectotherm");
        public static final ResourceKey<ISkillNode> ECTOTHERM_RANK_4 = node("rank_4", "ectotherm");
        public static final ResourceKey<ISkillNode> ECTOTHERM_ICE_LORD = node("ice_lord", "ectotherm");
        public static final ResourceKey<ISkillNode> ECTOTHERM_FISHMONGER = node("fishmonger", "ectotherm");
        public static final ResourceKey<ISkillNode> ECTOTHERM_REFRACTION = node("refraction", "ectotherm");
        public static final ResourceKey<ISkillNode> ECTOTHERM_DIFFUSION = node("diffusion", "ectotherm");

        public static final ResourceKey<ISkillNode> ECTOTHERM_FROST_CONTROL = node("frost_control", "ectotherm");
        public static final ResourceKey<ISkillNode> ECTOTHERM_UNDERWATER_MINING_SPEED = node("underwater_mining_speed", "ectotherm");
        public static final ResourceKey<ISkillNode> ECTOTHERM_FROZEN_SLOWNESS = node("frozen_slowness", "ectotherm");
        public static final ResourceKey<ISkillNode> ECTOTHERM_DOLPHIN_LEAP = node("dolphin_leap", "ectotherm");


        public static final ResourceKey<ISkillNode> NOBLE_RANK_2 = node("rank_2", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_RANK_3 = node("rank_3", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_RANK_4 = node("rank_4", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_BETTER_TRADE_PRICES = node("better_trade_prices", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_BETTER_BLOOD_DRAIN = node("better_blood_drain", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_FASTER_RESURRECT = node("better_faster_resurrect", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_MORE_TICKS_IN_SUN = node("better_more_ticks_in_sun", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_BAT_FLIGHT_SPEED = node("better_bat_flight_speed", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_FASTER_MOVEMENT_SPEED = node("faster_movement_speed", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_CHALICE_SKILL = node("bottomless_chalice", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_CELERITY = node("celerity", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_MESMERISE = node("mesmerise", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_LEECHING = node("leeching", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_ENHANCED_LEECHING = node("enhanced_leeching", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_INVISIBILITY = node("invisibility", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_INTRIGUE = node("noble_intrigue", "noble");
        public static final ResourceKey<ISkillNode> NOBLE_FLANK = node("noble_flank", "noble");


        private static ResourceKey<ISkillNode> node(String path, String bloodline) {
            return ResourceKey.create(VampirismRegistries.Keys.SKILL_NODE, Bloodlines.rl(bloodline + "_" + path));
        }
        public static void createSkillNodes(BootstrapContext<ISkillNode> context) {
            context.register(NOBLE_ROOT, new SkillNode(NOBLE_SKILL));
            context.register(ECTOTHERM_ROOT, new SkillNode(ECTOTHERM_SKILL));

            context.register(ZEALOT_ROOT, new SkillNode(ZEALOT_SKILL));
            context.register(ZEALOT_RANK_2, new SkillNode(BloodlineSkills.ZEALOT_RANK_2));
            context.register(ZEALOT_RANK_3, new SkillNode(BloodlineSkills.ZEALOT_RANK_3));
            context.register(ZEALOT_RANK_4, new SkillNode(BloodlineSkills.ZEALOT_RANK_4));
            context.register(ZEALOT_STONE_SPEED, new SkillNode(BloodlineSkills.ZEALOT_STONE_SPEED));
            context.register(ZEALOT_SHADOWWALK, new SkillNode(BloodlineSkills.ZEALOT_SHADOWWALK));
            context.register(ZEALOT_SHADOWARMOUR, new SkillNode(BloodlineSkills.ZEALOT_SHADOW_ARMOUR));
            context.register(ZEALOT_DARKCLOAK, new SkillNode(BloodlineSkills.ZEALOT_DARKCLOAK));
            context.register(ZEALOT_WALL_CLIMB, new SkillNode(BloodlineSkills.ZEALOT_WALL_CLIMB));
            context.register(ZEALOT_FALL_DAMAGE, new SkillNode(BloodlineSkills.ZEALOT_FALL_DAMAGE));
            context.register(ZEALOT_FLESH_ARMOUR, new SkillNode(BloodlineSkills.ZEALOT_FLESH_EATING, BloodlineSkills.ZEALOT_SHADOW_ARMOUR));
            context.register(ZEALOT_TUNNELER, new SkillNode(BloodlineSkills.ZEALOT_TUNNELER));
            context.register(ZEALOT_SPIDER_FRIEND, new SkillNode(BloodlineSkills.ZEALOT_SPIDER_FRIEND));
            context.register(ZEALOT_SHADOW_MASTERY, new SkillNode(BloodlineSkills.ZEALOT_SHADOW_MASTERY));
            context.register(ZEALOT_OBSCURED_POWER, new SkillNode(BloodlineSkills.ZEALOT_OBSCURED_POWER));
            context.register(ZEALOT_HEX_PROTECTION, new SkillNode(BloodlineSkills.ZEALOT_HEX_PROTECTION));
            context.register(ZEALOT_FRENZY, new SkillNode(BloodlineSkills.ZEALOT_FRENZY));

            context.register(ECTOTHERM_RANK_2, new SkillNode(BloodlineSkills.ECTOTHERM_RANK_2));
            context.register(ECTOTHERM_RANK_3, new SkillNode(BloodlineSkills.ECTOTHERM_RANK_3));
            context.register(ECTOTHERM_RANK_4, new SkillNode(BloodlineSkills.ECTOTHERM_RANK_4));
            context.register(ECTOTHERM_FISHMONGER, new SkillNode(BloodlineSkills.FISHMONGER));
            context.register(ECTOTHERM_REFRACTION, new SkillNode(BloodlineSkills.ECTOTHERM_REFRACTION));
            context.register(ECTOTHERM_DIFFUSION, new SkillNode(BloodlineSkills.ECTOTHERM_DIFFUSION));
            context.register(ECTOTHERM_ICE_LORD, new SkillNode(BloodlineSkills.ECTOTHERM_ICELORD));
            context.register(ECTOTHERM_FROST_CONTROL, new SkillNode(BloodlineSkills.ECTOTHERM_FROST_CONTROL));
            context.register(ECTOTHERM_UNDERWATER_MINING_SPEED, new SkillNode(BloodlineSkills.ECTOTHERM_MINING_SPEED_UNDERWATER));
            context.register(ECTOTHERM_FROZEN_SLOWNESS, new SkillNode(BloodlineSkills.ECTOTHERM_FROZEN_ATTACK, BloodlineSkills.ECTOTHERM_SLOWNESS_ATTACK));
            context.register(ECTOTHERM_DOLPHIN_LEAP, new SkillNode(BloodlineSkills.ECTOTHERM_DOLPHIN_LEAP));


            context.register(NOBLE_RANK_2, new SkillNode(BloodlineSkills.NOBLE_RANK_2));
            context.register(NOBLE_RANK_3, new SkillNode(BloodlineSkills.NOBLE_RANK_3));
            context.register(NOBLE_RANK_4, new SkillNode(BloodlineSkills.NOBLE_RANK_4));
            context.register(NOBLE_BETTER_TRADE_PRICES, new SkillNode(BloodlineSkills.NOBLE_BETTER_TRADE_PRICES));
            context.register(NOBLE_BETTER_BLOOD_DRAIN, new SkillNode(BloodlineSkills.NOBLE_BETTER_BLOOD_DRAIN));
            context.register(NOBLE_FASTER_RESURRECT, new SkillNode(BloodlineSkills.NOBLE_FASTER_RESURRECT));
            context.register(NOBLE_MORE_TICKS_IN_SUN, new SkillNode(BloodlineSkills.NOBLE_MORE_TICKS_IN_SUN));
            context.register(NOBLE_BAT_FLIGHT_SPEED, new SkillNode(BloodlineSkills.NOBLE_BAT_FLIGHT_SPEED));
            context.register(NOBLE_FASTER_MOVEMENT_SPEED, new SkillNode(BloodlineSkills.NOBLE_FASTER_MOVEMENT_SPEED));
            context.register(NOBLE_CHALICE_SKILL, new SkillNode(BloodlineSkills.NOBLE_CHALICE_SKILL));
            context.register(NOBLE_CELERITY, new SkillNode(BloodlineSkills.NOBLE_CELERITY));
            context.register(NOBLE_MESMERISE, new SkillNode(BloodlineSkills.NOBLE_MESMERISE));
            context.register(NOBLE_LEECHING, new SkillNode(BloodlineSkills.NOBLE_LEECHING));
            context.register(NOBLE_ENHANCED_LEECHING, new SkillNode(BloodlineSkills.NOBLE_ENHANCED_LEECHING));
            context.register(NOBLE_INVISIBILITY, new SkillNode(BloodlineSkills.NOBLE_INVISIBILITY));
            context.register(NOBLE_INTRIGUE, new SkillNode(BloodlineSkills.NOBLE_INTRIGUE));
            context.register(NOBLE_FLANK, new SkillNode(BloodlineSkills.NOBLE_FLANK));

        }
    }
}
