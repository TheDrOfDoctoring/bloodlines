package com.thedrofdoctoring.bloodlines.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import de.teamlapen.lib.lib.util.BasicCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class BloodlinePerkCommand extends BasicCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {

        return Commands.literal("perk_points")
                .requires(context -> context.hasPermission(PERMISSION_LEVEL_CHEAT))
                        .then(Commands.argument("points", IntegerArgumentType.integer(0))
                                .executes(context -> setPerkPoints(context, IntegerArgumentType.getInteger(context, "points"), Lists.newArrayList(context.getSource().getPlayerOrException())))
                                .then(Commands.argument("player", EntityArgument.entities())
                                    .executes(context -> setPerkPoints(context, IntegerArgumentType.getInteger(context, "points"), EntityArgument.getPlayers(context, "player")))));
    }
    @SuppressWarnings("SameReturnValue")
    private static int setPerkPoints(@NotNull CommandContext<CommandSourceStack> context, int additionalPoints, @NotNull Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            BloodlineManager bl = BloodlineManager.get(player);
            if(BloodlineManager.get(player).getBloodline() != null) {
                bl.getSkillHandler().setSkillPoints(bl.getSkillHandler().getSkillPoints() + additionalPoints);
                bl.onBloodlineChange(bl.getBloodline(), bl.getRank());
                context.getSource().sendSuccess(() -> Component.translatable("command.bloodlines.perk_success"), true);
            } else {
                context.getSource().sendFailure(Component.translatable("command.bloodlines.no_bloodline", players.size() > 1 ? player.getDisplayName() : "Player"));
            }
        }
        return 0;
    }
}
