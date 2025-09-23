package com.thedrofdoctoring.bloodlines.networking.packets.from_client;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import de.teamlapen.vampirism.entity.player.actions.ActionHandler;
import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundDevourSoulPacket(int entityId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundDevourSoulPacket> TYPE = new CustomPacketPayload.Type<>(Bloodlines.rl("devour_soul"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundDevourSoulPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ServerboundDevourSoulPacket::entityId, ServerboundDevourSoulPacket::new);


    @NotNull
    public CustomPacketPayload.@NotNull Type<ServerboundDevourSoulPacket> type() {
        return TYPE;
    }

    public static void handleDevourSoulPacket(ServerboundDevourSoulPacket devourSoulPacket, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            int targetEntityId = devourSoulPacket.entityId();
            Entity entity = player.level().getEntity(targetEntityId);
            BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
            if(atts.bloodline == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get() && entity instanceof LivingEntity living && !atts.getGraveboundData().possessionActive) {
                ActionHandler<IHunterPlayer> handler = (ActionHandler<IHunterPlayer>) HunterPlayer.get(player).getActionHandler();
                if(!handler.isActionOnCooldown(BloodlineActions.GRAVEBOUND_DEVOUR_SOUL.get())) {
                    handler.toggleAction(BloodlineActions.GRAVEBOUND_DEVOUR_SOUL.get(), new ActionHandler.ActivationContext(living));
                }
            }
        });

    }
}
