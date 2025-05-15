package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineBloodknight;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineZealot;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.items.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class BloodlinesItems {
    private static final Set<DeferredHolder<Item, ? extends Item>> creativeTabItems = new HashSet<>();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Bloodlines.MODID);
    public static final DeferredRegister<CreativeModeTab> BLOODLINE_CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Bloodlines.MODID);
    public static final ResourceKey<CreativeModeTab> BLOODLINE_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Bloodlines.rl("bloodlines"));
    public static final DeferredHolder<Item, BloodlineFang> BLOODLINE_FANG_ECTOTHERM = register("bloodline_fang_ectotherm", () -> new BloodlineFang(new Item.Properties().stacksTo(1), BloodlineFrost.ECTOTHERM));
    public static final DeferredHolder<Item, BloodlineFang> BLOODLINE_FANG_ZEALOT = register("bloodline_fang_zealot", () -> new BloodlineFang(new Item.Properties().stacksTo(1), BloodlineZealot.ZEALOT));
    public static final DeferredHolder<Item, BloodlineFang> BLOODLINE_FANG_BLOODKNIGHT = register("bloodline_fang_bloodknight", () -> new BloodlineFang(new Item.Properties().stacksTo(1), BloodlineBloodknight.BLOOD_KNIGHT));
    public static final DeferredHolder<Item, BloodlineFang> BLOODLINE_FANG_NOBLE = register("bloodline_fang_noble", () -> new BloodlineFang(new Item.Properties().stacksTo(1), BloodlineNoble.NOBLE));
    public static final DeferredHolder<Item, BottomlessChaliceItem> CHALICE_ITEM = register("bottomless_chalice", () -> new BottomlessChaliceItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, PurityInjection> PURITY_INJECTION = register("purity_injection", () -> new PurityInjection(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, BloodlineTesterItem> BLOODLINE_TESTER = register("bloodline_tester", () -> new BloodlineTesterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, LordslayerInjectionItem> LORDSLAYER_INJECTION = register("lordslayer_injection", () -> new LordslayerInjectionItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> CORRUPTED_BLOOD_SAMPLE = register("corrupted_blood_sample", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredHolder<Item, Item> FROZEN_BLOOD_SAMPLE = register("frozen_blood_sample", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredHolder<Item, ElixirItem> HEINOUS_ELIXIR = register("heinous_elixir", () -> new ElixirItem(new Item.Properties(), BloodlinesEffects.HEINOUS_CURSE, () -> CommonConfig.heinousElixirDurationSeconds.get() * 20));
    public static final DeferredHolder<Item, ElixirItem> COLD_ELIXIR = register("freezing_elixir", () -> new ElixirItem(new Item.Properties(), BloodlinesEffects.COLD_BLOODED, () -> CommonConfig.coldBloodedElixirDurationSeconds.get() * 20));
    public static final DeferredHolder<Item, Item> ZEALOT_RITUAL_CATALYST = register("zealot_ritual_catalyst", () -> new Item(new Item.Properties().stacksTo(64)));


    public static <T extends Item> DeferredHolder<Item, T> register(final String id, final Supplier<? extends T> itemSupplier) {
        DeferredHolder<Item, T> item = ITEMS.register(id, itemSupplier);
        creativeTabItems.add(item);
        return item;
    }
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        BLOODLINE_CREATIVE_TAB.register(bus);
    }
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOODLINE_TAB = BLOODLINE_CREATIVE_TAB.register(BLOODLINE_TAB_KEY.location().getPath(), () -> CreativeModeTab.builder().displayItems(
                    (pParameters, pOutput) -> creativeTabItems.forEach(item -> pOutput.accept(item.get())))
            .title(Component.translatable("itemGroup." + Bloodlines.MODID))
            .icon(() -> new ItemStack(BLOODLINE_FANG_ZEALOT.get()))
            .build()
    );
}
