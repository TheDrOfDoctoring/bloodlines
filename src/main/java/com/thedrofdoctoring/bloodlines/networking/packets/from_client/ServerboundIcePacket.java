package com.thedrofdoctoring.bloodlines.networking.packets.from_client;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.blocks.SpecialIceBlock;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ServerboundIcePacket implements CustomPacketPayload {

    private static ServerboundIcePacket INSTANCE;

    private ServerboundIcePacket() {
    }
    public static ServerboundIcePacket getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ServerboundIcePacket();
        }
        return INSTANCE;
    }
    public static final CustomPacketPayload.Type<ServerboundIcePacket> TYPE = new CustomPacketPayload.Type<>(Bloodlines.rl("ice"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundIcePacket> CODEC = StreamCodec.unit(getInstance());
    @NotNull
    public CustomPacketPayload.@NotNull Type<ServerboundIcePacket> type() {
        return TYPE;
    }

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
                BloodlineFrost.SpecialAttributes atts = BloodlinesPlayerAttributes.get(player).getEctothermAtts();
                if(atts.icePhasing && atts.frostControl) {

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
