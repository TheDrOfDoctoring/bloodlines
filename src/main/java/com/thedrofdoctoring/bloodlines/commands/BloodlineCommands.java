package com.thedrofdoctoring.bloodlines.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class  BloodlineCommands {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Bloodlines.MODID);
    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<?, ?>> BLOODLINE = COMMAND_ARGUMENT_TYPES.register("bloodline", () -> ArgumentTypeInfos.registerByClass(BloodlineArgument.class, SingletonArgumentInfo.contextFree(BloodlineArgument::bloodlines)));
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal(Bloodlines.MODID).then(ChangeBloodlineCommand.register()));
        dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal(Bloodlines.MODID).then(BloodlinePerkCommand.register()));
        dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal(Bloodlines.MODID).then(BloodlineSummonCommand.register(buildContext)));

    }


}
