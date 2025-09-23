package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class GraveboundPhylacteryTeleportAction extends GraveboundSoulAction{
    @Override
    public int getConsumedSouls(IHunterPlayer player) {
        return BloodlinesPlayerAttributes.get(player.asEntity()).getGraveboundData().mistForm ? HunterBloodlinesConfig.phylacteryTeleportMistFormSoulCost.get() : HunterBloodlinesConfig.phylacteryTeleportRegularSoulCost.get();
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        if(!super.activate(iHunterPlayer, activationContext)) {
            return false;
        }
        Player player = iHunterPlayer.asEntity();
        BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(player);
        if(state == null || state.getPhylacteryPos() == null) return false;

        if(!player.level().dimension().location().equals(state.getPhylacteryDimension())) {
            player.displayClientMessage(Component.translatable("skill.bloodlines.gravebound_incorrect_dimension").withStyle(ChatFormatting.BLUE), true);
            return false;
        }

        Vec3 newPosition = findTeleportLocation(player, state.getPhylacteryPos());
        if(newPosition == null) {
            player.displayClientMessage(Component.translatable("skill.bloodlines.gravebound_no_safe_location").withStyle(ChatFormatting.BLUE), true);
            return false;
        }
        player.teleportTo(newPosition.x, newPosition.y, newPosition.z);
        consumeSouls(iHunterPlayer);
        return true;
    }

    private Vec3 findTeleportLocation(Player player, BlockPos phylacteryPos) {
        Vec3 firstPos = DismountHelper.findSafeDismountLocation(EntityType.PLAYER, player.level(), phylacteryPos, true);
        if(firstPos != null) return firstPos;
        Level level = player.level();
        int phylacteryY = phylacteryPos.getY();
        for(int x = -1; x < 2; x++) {
            for(int z=  -1; z < 2; z++) {
                BlockPos phylacteryHeight = new BlockPos(phylacteryPos.getX() + x, phylacteryY, phylacteryPos.getZ() + z);
                BlockPos abovePhylactery = new BlockPos(phylacteryPos.getX() + x, phylacteryY + 1, phylacteryPos.getZ() + z);
                BlockState block = level.getBlockState(phylacteryHeight);
                if(block.is(BlockTags.AIR)) {
                    block = level.getBlockState(abovePhylactery);
                    if(block.is(BlockTags.AIR)) {
                        return abovePhylactery.getCenter();
                    }
                }
            }
        }
        return null;

    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.phylacteryTeleportCooldown.get() * 20;
    }

    @Override
    public boolean canBeUsedBy(IHunterPlayer player) {
        return BloodlinesPlayerAttributes.get(player.asEntity()).getGraveboundData().hasPhylactery;
    }

    @Override
    public boolean showInSelectAction(Player player) {
        return BloodlinesPlayerAttributes.get(player).getGraveboundData().hasPhylactery && BloodlineHelper.hasBloodline(BloodlineRegistry.BLOODLINE_GRAVEBOUND.get(), player);
    }

    @Override
    public boolean showHudCooldown(Player player) {
        return false;
    }
}
