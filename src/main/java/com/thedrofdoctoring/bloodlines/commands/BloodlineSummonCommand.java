package com.thedrofdoctoring.bloodlines.commands;


import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.entity.BloodlineMobManager;
import de.teamlapen.lib.lib.util.BasicCommand;
import de.teamlapen.vampirism.api.entity.IFactionMob;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class BloodlineSummonCommand extends BasicCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed"));
    private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed.uuid"));
    private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(Component.translatable("commands.summon.invalidPosition"));
    private static final SimpleCommandExceptionType INVALID_BLOODLINE = new SimpleCommandExceptionType(Component.translatable("commands.bloodline_summon.invalid_bloodline"));


    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandBuildContext buildContext) {

        return Commands.literal("summon_bloodline_mob")
                .requires(context -> context.hasPermission(PERMISSION_LEVEL_CHEAT))
                .then(Commands.argument("bloodline", BloodlineArgument.bloodlines())

                        .then(Commands.argument("entity", ResourceArgument.resource(buildContext, Registries.ENTITY_TYPE))
                                    .executes(
                                            context -> spawnEntity (

                                                    context.getSource(),
                                                    BloodlineArgument.getBloodline(context, "bloodline"),
                                                    1,
                                                    ResourceArgument.getSummonableEntityType(context, "entity"),
                                                    context.getSource().getPosition()
                                            )
                                    )
                                    .then(Commands.argument("rank", IntegerArgumentType.integer(1, 4)).executes(
                                                    context -> spawnEntity(
                                                            context.getSource(),
                                                            BloodlineArgument.getBloodline(context, "bloodline"),
                                                            IntegerArgumentType.getInteger(context, "rank"),
                                                            ResourceArgument.getSummonableEntityType(context, "entity"),
                                                            context.getSource().getPosition()                                                    )
                                            )

                                    .then(
                                            Commands.argument("pos", Vec3Argument.vec3())
                                                    .executes(
                                                            context -> spawnEntity(
                                                                    context.getSource(),
                                                                    BloodlineArgument.getBloodline(context, "bloodline"),
                                                                    IntegerArgumentType.getInteger(context, "rank"),
                                                                    ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                    Vec3Argument.getVec3(context, "pos")
                                                            )
                                                    )
                                    )

                        )
                    )
                );
    }
    public static Entity createEntity(CommandSourceStack pSource, IBloodline bloodline, int rank, Holder.Reference<EntityType<?>> pType, Vec3 pPos) throws CommandSyntaxException {
        BlockPos blockpos = BlockPos.containing(pPos);
        if (!Level.isInSpawnableBounds(blockpos)) {
            throw INVALID_POSITION.create();
        } else {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("id", pType.key().location().toString());
            ServerLevel serverlevel = pSource.getLevel();
            Entity entity = EntityType.loadEntityRecursive(nbt, serverlevel, p_138828_ -> {
                p_138828_.moveTo(pPos.x, pPos.y, pPos.z, p_138828_.getYRot(), p_138828_.getXRot());
                return p_138828_;
            });
            if (!(entity instanceof PathfinderMob mob)) {
                throw INVALID_BLOODLINE.create();
            }
            EventHooks.finalizeMobSpawn(mob, pSource.getLevel(), pSource.getLevel().getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.COMMAND, null);
            if(mob instanceof IFactionMob fMob) {
                if(bloodline.getFaction().equals(fMob.getFaction())) {
                    BloodlineMobManager manager = BloodlineMobManager.get(mob);
                    manager.setBloodline(bloodline);
                    manager.setRank(rank);
                    manager.onBloodlineChange(null, 0);
                } else {
                    throw INVALID_BLOODLINE.create();
                }
            } else {
                throw INVALID_BLOODLINE.create();
            }

            if (!serverlevel.tryAddFreshEntityWithPassengers(entity)) {
                throw ERROR_DUPLICATE_UUID.create();
            } else {
                return entity;
            }
        }
    }
    private static int spawnEntity(CommandSourceStack pSource, IBloodline bloodline, int rank, Holder.Reference<EntityType<?>> pType, Vec3 pPos) throws CommandSyntaxException {
        Entity entity = createEntity(pSource, bloodline, rank, pType, pPos);
        pSource.sendSuccess(() -> Component.translatable("commands.summon.success", entity.getDisplayName()), true);
        return 1;
    }
}
