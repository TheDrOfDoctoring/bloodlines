package com.thedrofdoctoring.bloodlines.networking;

import com.thedrofdoctoring.bloodlines.blocks.SpecialIceBlock;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    public static void handleIcePacket(ServerboundIcePacket ignoredData, final IPayloadContext context) {
        context.enqueueWork(() -> {

            Player player = context.player();
            Level level = context.player().level();
            HitResult result = player.pick(10, 1, true);
            if(!(result instanceof BlockHitResult blockResult) ) return;

            BlockPos pos = blockResult.getBlockPos();
            if(level.getBlockState(pos).is(Blocks.WATER)) {
                BlockPos[] possiblePositions = new BlockPos[4];
                possiblePositions[0] = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1);
                possiblePositions[1] = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1);
                possiblePositions[2] = new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ());
                possiblePositions[3] = new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ());
                FluidState fState = level.getFluidState(pos);
                BlockState wasSource = BloodlinesBlocks.ICE_BLOCK.get().defaultBlockState().setValue(SpecialIceBlock.WAS_WATER, true);
                BlockState notSource = BloodlinesBlocks.ICE_BLOCK.get().defaultBlockState().setValue(SpecialIceBlock.WAS_WATER, false);

                if(((IVampSpecialAttributes) VampirePlayer.get(player).getSpecialAttributes()).bloodlines$getIcePhasing()) {

                    level.setBlock(pos, fState.isSource() ? wasSource : notSource, 3);
                    for(BlockPos possiblePos : possiblePositions) {
                        if(level.getBlockState(possiblePos).is(Blocks.WATER))
                            level.setBlock(possiblePos, fState.isSource() ? wasSource : notSource, 3);

                    }
                }

            }
        });
    }
}
