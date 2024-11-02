package com.thedrofdoctoring.bloodlines.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class BloodlineArgument implements ArgumentType<IBloodline> {
    private static final Collection<String> EXAMPLES = BloodlineRegistry.BLOODLINE_REGISTRY.entrySet().stream().map(p -> p.getKey().location().toString()).toList();
    private static final DynamicCommandExceptionType BLOODLINE_NOT_FOUND = new DynamicCommandExceptionType((id) -> Component.translatable("command.bloodlines.argument.bloodline_not_found", id));

    @Override
    public IBloodline parse(StringReader reader) throws CommandSyntaxException {

        ResourceLocation id = ResourceLocation.read(reader);
        IBloodline bloodline = BloodlineHelper.getBloodlineById(id);
        if(bloodline == null) throw BLOODLINE_NOT_FOUND.create(id);
        return bloodline;
    }

    public static BloodlineArgument bloodlines() {
        return new BloodlineArgument();
    }

    public static IBloodline getBloodline(@NotNull CommandContext<CommandSourceStack> context, String id) {
        return context.getArgument(id, IBloodline.class);
    }
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(getExamples().stream(), builder);
    }
}
