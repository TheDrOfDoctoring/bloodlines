package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.ISpecialAttributes;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourMixin {
    @Unique
    public final VoxelShape bloodlines$voxelShape = Shapes.empty();
    @Unique
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At(value = "RETURN"), cancellable = true)
    private void getCollisionShape(BlockGetter getter, BlockPos pos, CollisionContext con, CallbackInfoReturnable<VoxelShape> cir) {
        if(!(con instanceof EntityCollisionContext)) {
            return;
        }
        Entity entity = ((EntityCollisionContext) con).getEntity();
        if(!(entity instanceof Player) || !Helper.isVampire(entity)) {
            return;
        }

        VampirePlayer vp = VampirePlayer.get((Player) entity);
        ISpecialAttributes specialAttributes = ((ISpecialAttributes)vp.getSpecialAttributes());
        if (getter.getBlockState(pos).is(BloodlinesTagsProviders.BloodlinesBlockTagProvider.ECTOTHERM_ICE) && specialAttributes.bloodlines$getIcePhasing()) {

            if(!bloodlines$isAbove(entity, Shapes.block(), pos) || entity.isDescending()) {
                specialAttributes.bloodlines$setInWall(true);
                cir.setReturnValue(bloodlines$voxelShape);
            }
            if(entity.getDeltaMovement().y > 0.1 && !entity.isDescending() && !bloodlines$isAbove(entity, Shapes.block(), pos)) {
                specialAttributes.bloodlines$setInWall(true);
                if(entity.isInWater()) {
                    entity.setDeltaMovement(entity.getDeltaMovement().x, 0.5f, entity.getDeltaMovement().z);
                }
            }
            entity.resetFallDistance();
        } else {
            specialAttributes.bloodlines$setInWall(false);
        }
    }
    @Unique
    private boolean bloodlines$isAbove(Entity entity, VoxelShape shape, BlockPos pos) {
        return entity.getY() > pos.getY() + shape.max(Direction.Axis.Y) - (entity.onGround() ? 8.05/16.0 : 0.0015);
    }
}
