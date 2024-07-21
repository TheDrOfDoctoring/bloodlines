package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineZealot;
import com.thedrofdoctoring.bloodlines.items.BloodlineFang;
import com.thedrofdoctoring.bloodlines.items.BottomlessChaliceItem;
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

    public static final DeferredHolder<Item, BloodlineFang> BLOODLINE_FANG_NOBLE = register("bloodline_fang_noble", () -> new BloodlineFang(new Item.Properties().stacksTo(1), BloodlineNoble.NOBLE));
    public static final DeferredHolder<Item, BottomlessChaliceItem> CHALICE_ITEM = register("bottomless_chalice", () -> new BottomlessChaliceItem(new Item.Properties().stacksTo(1)));

    private static <T extends Item> DeferredHolder<Item, T> register(final String id, final Supplier<? extends T> itemSupplier) {
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
