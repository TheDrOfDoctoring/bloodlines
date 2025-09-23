package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourMixin {
    @Shadow public abstract Block getBlock();

    @Unique
    public final VoxelShape bloodlines$voxelShape = Shapes.empty();

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At(value = "RETURN"), cancellable = true)
    private void getCollisionShape(BlockGetter getter, BlockPos pos, CollisionContext con, CallbackInfoReturnable<VoxelShape> cir) {
        if(!(con instanceof EntityCollisionContext)) {
            return;
        }
        Entity entity = ((EntityCollisionContext) con).getEntity();
        if(!(entity instanceof Player player)) {
            return;
        }

        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
        BlockState state = getter.getBlockState(pos);
        boolean canGhostwalk = atts.getGraveboundData().ghostWalk && !state.is(BloodlinesTagsProviders.BloodlinesBlockTagProvider.GHOSTWALK_BLACKLIST);
        boolean canIcePhase = atts.getEctothermAtts().icePhasing && state.is(BloodlinesTagsProviders.BloodlinesBlockTagProvider.ECTOTHERM_ICE);

        atts.inWall = false;
        if (canGhostwalk || canIcePhase) {


            if(state.isSolidRender(getter, pos)) {
                if(!bloodlines$isAbove(entity, Shapes.block(), pos) || entity.isDescending()) {
                    atts.inWall = true;
                    cir.setReturnValue(bloodlines$voxelShape);
                }
            }

            if(entity.getDeltaMovement().y > 0.1 && !entity.isDescending() && !bloodlines$isAbove(entity, Shapes.block(), pos)) {
                atts.inWall = true;
                if(entity.isInWater() && atts.getEctothermAtts().icePhasing) {
                    entity.setDeltaMovement(entity.getDeltaMovement().x, 0.5f, entity.getDeltaMovement().z);
                }
            }
            entity.resetFallDistance();
        }
    }
    @Unique
    private boolean bloodlines$isAbove(Entity entity, VoxelShape shape, BlockPos pos) {
        return entity.getY() > pos.getY() + shape.max(Direction.Axis.Y) - (entity.onGround() ? 8.05/16.0 : 0.0015);
    }
}
