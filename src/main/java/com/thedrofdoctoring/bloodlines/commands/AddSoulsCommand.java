package com.thedrofdoctoring.bloodlines.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.lib.lib.util.BasicCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class AddSoulsCommand extends BasicCommand {



    public static ArgumentBuilder<CommandSourceStack, ?> register() {

        return Commands.literal("souls")
                .requires(context -> context.hasPermission(PERMISSION_LEVEL_CHEAT))
                .then(Commands.argument("souls", IntegerArgumentType.integer(-50))
                        .executes(context -> addSouls(context, IntegerArgumentType.getInteger(context, "souls"), Lists.newArrayList(context.getSource().getPlayerOrException())))
                        .then(Commands.argument("player", EntityArgument.entities())
                                .executes(context -> addSouls(context, IntegerArgumentType.getInteger(context, "souls"), EntityArgument.getPlayers(context, "player")))));
    }
    @SuppressWarnings("SameReturnValue")
    private static int addSouls(@NotNull CommandContext<CommandSourceStack> context, int souls, @NotNull Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            BloodlineManager bl = BloodlineManager.get(player);
            if(bl.getBloodline() == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get()) {
                BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(player);
                if (state != null) {
                    state.addSouls(souls);
                    bl.sync(false);
                    bl.updatePlayerCache();
                    context.getSource().sendSuccess(() -> Component.translatable("command.bloodlines.souls_success", souls), true);
                }
            } else {
                context.getSource().sendFailure(Component.translatable("command.bloodlines.not_gravebound", players.size() > 1 ? player.getDisplayName() : "Player"));
            }
        }
        return 0;
    }
}
