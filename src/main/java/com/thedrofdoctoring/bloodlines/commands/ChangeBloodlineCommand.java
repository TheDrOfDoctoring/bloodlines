package com.thedrofdoctoring.bloodlines.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import de.teamlapen.lib.lib.util.BasicCommand;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ChangeBloodlineCommand extends BasicCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {

        return Commands.literal("bloodline")
                .requires(context -> context.hasPermission(PERMISSION_LEVEL_CHEAT))
                .then(Commands.argument("bloodline", new BloodlineArgument())
                        .executes(context -> setRank(context, BloodlineArgument.getBloodline(context, "bloodline"), 1, Lists.newArrayList(context.getSource().getPlayerOrException())))
                        .then(Commands.argument("rank", IntegerArgumentType.integer(0))
                                .executes(context -> setRank(context, BloodlineArgument.getBloodline(context, "bloodline"), IntegerArgumentType.getInteger(context, "rank"), Lists.newArrayList(context.getSource().getPlayerOrException())))
                                .then(Commands.argument("player", EntityArgument.entities())
                                        .executes(context -> setRank(context, BloodlineArgument.getBloodline(context, "bloodline"), IntegerArgumentType.getInteger(context, "rank"), EntityArgument.getPlayers(context, "player"))))));
    }
    @SuppressWarnings("SameReturnValue")
    private static int setRank(@NotNull CommandContext<CommandSourceStack> context, IBloodline bloodline, int rank, @NotNull Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            if(rank > 4 || rank < 0) {
                context.getSource().sendFailure(Component.translatable("command.bloodlines.rank_wrong"));
            }
            else if(bloodline.getFaction() == FactionPlayerHandler.getOpt(player).map(FactionPlayerHandler::getCurrentFaction).orElse(null)) {
                BloodlineManager.getOpt(player).ifPresent(bl -> {
                    IBloodline oldBloodline = bl.getBloodline();
                    int oldRank = bl.getRank();
                    if(rank == 0) {
                        bl.setBloodline(null);
                    } else {
                        bl.setBloodline(bloodline);
                    }
                    bl.setRank(rank);
                    bl.onBloodlineChange(oldBloodline, oldRank);
                    context.getSource().sendSuccess(() -> Component.translatable("command.bloodlines.bloodline_success"), true);
                });
            } else {
                context.getSource().sendFailure(Component.translatable("command.bloodlines.faction_wrong", bloodline.getFaction().getNamePlural()));
            }
        }
        return 0;
    }
}
