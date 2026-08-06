package com.thedrofdoctoring.bloodlines.menus;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BloodlinesMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Bloodlines.MODID);


    public static final DeferredHolder<MenuType<?>, MenuType<PhylacteryMenu>> PHYLACTERY = MENUS.register("phylactery", () -> create(PhylacteryMenu::new));

    private static <T extends AbstractContainerMenu> MenuType<T> create(MenuType.MenuSupplier<T> supplier) {
        return new MenuType<>(supplier, FeatureFlags.DEFAULT_FLAGS);
    }
}
