package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkill;
import com.thedrofdoctoring.bloodlines.skills.IBloodlineSkill;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.client.gui.screens.skills.SkillNodeScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

@Mixin(SkillNodeScreen.class)
public class SkillNodeScreenMixin {

    @Shadow @Final private static ResourceLocation DESCRIPTION_SPRITE;

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private int y;

    //adds a message to bloodline skill nodes saying they need bloodline skill points.
    //slightly grotesque. will break if the SkillNodeState enum ordinals are changed
    @Inject(method = "drawHover", at = @At(value = "INVOKE", target = "Lde/teamlapen/vampirism/client/gui/screens/skills/SkillNodeScreen;getLockingSkills(Lde/teamlapen/vampirism/entity/player/skills/SkillTreeConfiguration$SkillTreeNodeConfiguration;)Ljava/util/List;"), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addBloodlineHover(GuiGraphics graphics, double mouseX, double mouseY, float fade, int scrollX, int scrollY, CallbackInfo ci, @Coerce Enum<?> state, Holder<ISkill<?>>[] elements, int hoveredSkillIndex, Holder<ISkill<?>> hoveredSkill, int x) {
        if(hoveredSkill.value() instanceof IBloodlineSkill skill && skill.requiresBloodlineSkillPoints() && state.ordinal() == 0) {
            int remainingPoints = BloodlineManager.get(minecraft.player).getSkillHandler().getRemainingSkillPoints();
            List<Component> text = new ArrayList<>();
            text.add(Component.translatable("text.bloodlines.skills.required_points").withStyle(ChatFormatting.DARK_RED));
            text.add(Component.translatable("text.bloodlines.skills.remaining_points", remainingPoints).withStyle(ChatFormatting.DARK_RED));
            int width = text.stream().mapToInt(this.minecraft.font::width).max().getAsInt();
            graphics.blitSprite(DESCRIPTION_SPRITE, scrollX + x - 3, scrollY + this.y - 3 - text.size() * 9, width + 8, 10 + text.size() * 10);
            int fontY = scrollY + this.y + 1 - text.size() * 9;
            for (int i = 0; i < text.size(); i++) {
                graphics.drawString(this.minecraft.font, text.get(i), scrollX + x + 2, fontY + i * 9, -1, true);
            }
        }
    }
}
