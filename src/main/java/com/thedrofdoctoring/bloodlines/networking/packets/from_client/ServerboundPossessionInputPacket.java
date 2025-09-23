package com.thedrofdoctoring.bloodlines.networking.packets.from_client;

import com.mojang.datafixers.util.Function8;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound.GraveboundPossessionAction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record ServerboundPossessionInputPacket(float sideways, float forward, float xRot, float yRot, float yRotHead, boolean jumping, boolean shift, boolean sprint) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundPossessionInputPacket> TYPE = new CustomPacketPayload.Type<>(Bloodlines.rl("possession_input"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundPossessionInputPacket> CODEC = composite(
            ByteBufCodecs.FLOAT, ServerboundPossessionInputPacket::sideways,
            ByteBufCodecs.FLOAT, ServerboundPossessionInputPacket::forward,
            ByteBufCodecs.FLOAT, ServerboundPossessionInputPacket::xRot,
            ByteBufCodecs.FLOAT, ServerboundPossessionInputPacket::yRot,
            ByteBufCodecs.FLOAT, ServerboundPossessionInputPacket::yRotHead,
            ByteBufCodecs.BOOL, ServerboundPossessionInputPacket::jumping,
            ByteBufCodecs.BOOL, ServerboundPossessionInputPacket::shift,
            ByteBufCodecs.BOOL, ServerboundPossessionInputPacket::sprint,
            ServerboundPossessionInputPacket::new);


    @NotNull
    public CustomPacketPayload.@NotNull Type<ServerboundPossessionInputPacket> type() {
        return TYPE;
    }

    public static void handlePossessionPacket(ServerboundPossessionInputPacket possessionPacket, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
            if(atts.getGraveboundData().possessedEntity != null) {
                GraveboundPossessionAction.handlePossessionMovement(possessionPacket, atts.getGraveboundData().possessedEntity);
            }
        });

    }


    private static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> pCodec1,
            final Function<C, T1> pGetter1,
            final StreamCodec<? super B, T2> pCodec2,
            final Function<C, T2> pGetter2,
            final StreamCodec<? super B, T3> pCodec3,
            final Function<C, T3> pGetter3,
            final StreamCodec<? super B, T4> pCodec4,
            final Function<C, T4> pGetter4,
            final StreamCodec<? super B, T5> pCodec5,
            final Function<C, T5> pGetter5,
            final StreamCodec<? super B, T6> pCodec6,
            final Function<C, T6> pGetter6,
            final StreamCodec<? super B, T7> pCodec7,
            final Function<C, T7> pGetter7,
            final StreamCodec<? super B, T8> pCodec8,
            final Function<C, T8> pGetter8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> pFactory
    ) {
        return new StreamCodec<>() {
            @Override
            public @NotNull C decode(@NotNull B type) {
                T1 t1 = pCodec1.decode(type);
                T2 t2 = pCodec2.decode(type);
                T3 t3 = pCodec3.decode(type);
                T4 t4 = pCodec4.decode(type);
                T5 t5 = pCodec5.decode(type);
                T6 t6 = pCodec6.decode(type);
                T7 t7 = pCodec7.decode(type);
                T8 t8 = pCodec8.decode(type);
                return pFactory.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(@NotNull B codec, @NotNull C getter) {
                pCodec1.encode(codec, pGetter1.apply(getter));
                pCodec2.encode(codec, pGetter2.apply(getter));
                pCodec3.encode(codec, pGetter3.apply(getter));
                pCodec4.encode(codec, pGetter4.apply(getter));
                pCodec5.encode(codec, pGetter5.apply(getter));
                pCodec6.encode(codec, pGetter6.apply(getter));
                pCodec7.encode(codec, pGetter7.apply(getter));
                pCodec8.encode(codec, pGetter8.apply(getter));
            }
        };
    }

}
