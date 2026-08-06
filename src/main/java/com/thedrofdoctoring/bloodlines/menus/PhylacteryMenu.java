package com.thedrofdoctoring.bloodlines.menus;

import com.thedrofdoctoring.bloodlines.blocks.entities.PhylacteryBlockEntity;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class PhylacteryMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final DataSlot storedSouls;


    public PhylacteryMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public PhylacteryMenu(int containerId, @NotNull Inventory playerInventory, ContainerLevelAccess access) {
        super(BloodlinesMenus.PHYLACTERY.get(), containerId);
        this.access = access;
        this.storedSouls = DataSlot.standalone();
        updateStoredSouls();
        this.addDataSlot(storedSouls);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean clickMenuButton(@NotNull Player pPlayer, int pId) {

        int amount = switch (pId) {
            case 0 -> 1;
            case 1 -> 5;
            case 2 -> -1;
            default -> -5;
        };

        this.access.execute((level, pos) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if(be instanceof PhylacteryBlockEntity phylactery && pPlayer.getUUID().equals(phylactery.getOwnerUUID())) {
                phylactery.extractSouls(pPlayer, amount);
                this.updateStoredSouls();
            }
        });
        return super.clickMenuButton(pPlayer, pId);

    }

    public int getStoredSouls() {
        return this.storedSouls.get();
    }

    private void updateStoredSouls() {
        this.storedSouls.set(access.evaluate( (level, pos) -> {
            if(level.getBlockEntity(pos) instanceof PhylacteryBlockEntity phylactery) {
                return phylactery.getStoredSouls();
            } return 0;
        },0));
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return AbstractContainerMenu.stillValid(this.access, pPlayer, BloodlinesBlocks.PHYLACTERY.get());
    }
}
