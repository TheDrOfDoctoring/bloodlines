package com.thedrofdoctoring.bloodlines.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.blocks.entities.PhylacteryBlockEntity;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.menus.PhylacteryMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import org.jetbrains.annotations.NotNull;

public class PhylacteryScreen extends AbstractContainerScreen<PhylacteryMenu> {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 145;

    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;

    private static final ResourceLocation BACKGROUND = Bloodlines.rl("textures/gui/phylactery/phylactery.png");
    private static final ResourceLocation SOUL_BAR_BACKGROUND = Bloodlines.rl("phylactery/soul_bar_background");
    private static final ResourceLocation SOUL_BAR_PROGRESS = Bloodlines.rl("phylactery/soul_bar_progress");

    private int guiLeft;
    private int guiTop;
    private int maxStoredSouls;
    private int soulsForNextTier;



    protected PhylacteryScreen(PhylacteryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        assert this.minecraft != null;
        this.guiLeft = (this.width - WIDTH) / 2;
        this.guiTop = (this.height - HEIGHT) / 2;
        this.leftPos = guiLeft;
        this.topPos = guiTop;
        if(this.minecraft.player != null && this.minecraft.gameMode != null) {
            MultiPlayerGameMode gameMode = this.minecraft.gameMode;

            this.addRenderableWidget(new ExtendedButton(this.guiLeft + 20, this.guiTop + 42, 60, 15, Component.translatable("screens.bloodlines.phylactery_take", 1), (button) -> gameMode.handleInventoryButtonClick(this.menu.containerId, 0)));
            this.addRenderableWidget(new ExtendedButton(this.guiLeft + 20, this.guiTop + 42 + 16, 60, 15, Component.translatable("screens.bloodlines.phylactery_take", 5), (button) -> gameMode.handleInventoryButtonClick(this.menu.containerId, 1)));
            this.addRenderableWidget(new ExtendedButton(this.guiLeft + 20 + 70, this.guiTop + 42, 60, 15, Component.translatable("screens.bloodlines.phylactery_give", 1), (button) -> gameMode.handleInventoryButtonClick(this.menu.containerId, 2)));
            this.addRenderableWidget(new ExtendedButton(this.guiLeft + 20 + 70, this.guiTop + 42 + 16, 60, 15, Component.translatable("screens.bloodlines.phylactery_give", 5), (button) -> gameMode.handleInventoryButtonClick(this.menu.containerId, 3)));
        }
        if(BloodlineHelper.hasBloodline(BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), minecraft.player)) {
            BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(minecraft.player);
            if(state != null) {
                int totalDevoured     = state.getTotalSoulsDevoured();
                this.soulsForNextTier = PhylacteryBlockEntity.determineSoulsForNextTier(totalDevoured);
                this.maxStoredSouls   = HunterBloodlinesConfig.phylacteryMaxStorageTiers.get().get(PhylacteryBlockEntity.determineTier(totalDevoured));
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        for(Renderable renderable : this.renderables) {
            if(renderable instanceof Button button) {
                button.setFocused(false);
            }
        }
        renderFg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
    }

    @SuppressWarnings("DataFlowIssue")
    protected void renderFg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(this.guiLeft, this.guiTop, 100);
        Component storedSoulsText = Component.translatable("screens.bloodlines.phylactery_stored_souls", menu.getStoredSouls(), maxStoredSouls).withStyle(ChatFormatting.DARK_AQUA);
        Component soulsForTierText = Component.translatable("screens.bloodlines.phylactery_next_tier", soulsForNextTier).withStyle(ChatFormatting.DARK_AQUA);
        guiGraphics.drawString(minecraft.font, storedSoulsText, 10, 90, -1, true);
        guiGraphics.drawString(minecraft.font, soulsForTierText, 10, 110, -1, true);
        pose.scale(0.75f, 1f, 1f);
        guiGraphics.blitSprite(SOUL_BAR_BACKGROUND, (int) (WIDTH / 7.5f), 24, BAR_WIDTH, BAR_HEIGHT);
        float progress = (float) menu.getStoredSouls() / maxStoredSouls;
        guiGraphics.blitSprite(SOUL_BAR_PROGRESS, BAR_WIDTH, BAR_HEIGHT, 0, 0, (int) (WIDTH / 7.5f), 24, (int) (BAR_WIDTH * progress), BAR_HEIGHT);

        pose.popPose();
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(this.guiLeft, this.guiTop, 0);
        guiGraphics.blit(BACKGROUND, 0, 0, 0, 0, this.imageWidth, this.imageHeight);

        pose.popPose();
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }
}
