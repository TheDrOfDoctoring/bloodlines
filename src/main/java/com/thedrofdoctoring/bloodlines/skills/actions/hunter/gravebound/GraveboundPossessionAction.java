package com.thedrofdoctoring.bloodlines.skills.actions.hunter.gravebound;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.capabilities.other.IPossessedEntity;
import com.thedrofdoctoring.bloodlines.config.HunterBloodlinesConfig;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import com.thedrofdoctoring.bloodlines.mixin.EntityInvoker;
import com.thedrofdoctoring.bloodlines.mixin.LivingEntityAccessor;
import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundPossessionInputPacket;
import com.thedrofdoctoring.bloodlines.networking.packets.from_client.ServerboundPossessionInteractPacket;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.hunter.IHunterPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;


/**
 * Practically useless. Took the longest time to make. But at least it's cool.
 */
public class GraveboundPossessionAction extends GraveboundSoulAction implements ILastingAction<IHunterPlayer> {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getConsumedSouls(IHunterPlayer player) {
        return HunterBloodlinesConfig.possessionSoulCost.get();
    }

    @Override
    protected boolean activate(IHunterPlayer iHunterPlayer, ActivationContext activationContext) {
        if(!super.activate(iHunterPlayer, activationContext)) {
            return false;
        }
        if(activationContext.targetEntity().isPresent()) {
            Entity targetEntity = activationContext.targetEntity().get();
            if(targetEntity instanceof LivingEntity living && living.getType().is(BloodlinesTagsProviders.BloodlinesEntityTypeTagsProvider.POSSESSION_WHITELIST)) {
                boolean poss = startPossession(iHunterPlayer.asEntity(), living);
                if(!poss) {
                    return false;
                }
            } else {
                iHunterPlayer.asEntity().displayClientMessage(Component.translatable("text.bloodlines.possesssion_entity_invalid").withStyle(ChatFormatting.BLUE), true);
                return false;
            }

        } else {
            iHunterPlayer.asEntity().displayClientMessage(Component.translatable("text.bloodlines.possesssion_entity").withStyle(ChatFormatting.BLUE), true);
            return false;
        }
        consumeSouls(iHunterPlayer);
        return true;
    }

    public static void clearPossession(Player player) {
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);
        atts.getGraveboundData().possessionActive = false;
        BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(player);
        if(state != null) {
            state.clearPossession();
        }
    }


    private boolean startPossession(Player player, LivingEntity target) {
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);

        if(target.isAlive()) {
            ((IPossessedEntity) target).bloodlines$setPossessed(player);
            if(target instanceof Creeper creeper) {
                creeper.setSwellDir(-1);
            }

        } else {
            return false;
        }
        atts.getGraveboundData().possessionActive = true;
        atts.getGraveboundData().possessedEntity = target;

        BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(player);

        if(state != null) {
            state.setPossession(target);
            BloodlineManager.get(player).sync(false);
        }



        if(player instanceof ServerPlayer serverPlayer) {
            serverPlayer.setDeltaMovement(0, 0, 0);

            serverPlayer.setCamera(target);
        }
        return true;

    }
    private void endPossession(Player player) {
        BloodlinesPlayerAttributes atts = BloodlinesPlayerAttributes.get(player);

        atts.getGraveboundData().possessionActive = false;
        LivingEntity entity = atts.getGraveboundData().possessedEntity;

        if(entity != null && entity.isAlive()) {
            ((IPossessedEntity) entity).bloodlines$clearPossession();
        }
        atts.getGraveboundData().possessedEntity = null;

        if(player instanceof ServerPlayer serverPlayer) {
            serverPlayer.setCamera(player);
        }
        BloodlineGravebound.State state = BloodlineGravebound.getGraveboundState(player);
        if(state != null) {
            state.clearPossession();
            BloodlineManager.get(player).sync(false);
        }

    }

    @Override
    public int getCooldown(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.possessionCooldown.get() * 20;
    }

    @Override
    public int getDuration(IHunterPlayer iHunterPlayer) {
        return HunterBloodlinesConfig.possessionDuration.get() * 20;
    }


    @Override
    public void onActivatedClient(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().possessionActive = true;
        iHunterPlayer.asEntity().setDeltaMovement(0, 0, 0);

    }

    @Override
    public void onDeactivated(IHunterPlayer iHunterPlayer) {
        endPossession(iHunterPlayer.asEntity());

    }

    @Override
    public void onReActivated(IHunterPlayer iHunterPlayer) {
        BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().possessionActive = true;
    }


    @Override
    public boolean onUpdate(IHunterPlayer iHunterPlayer) {

        return !BloodlinesPlayerAttributes.get(iHunterPlayer.asEntity()).getGraveboundData().possessionActive;
    }

    @Override
    public boolean showHudDuration(Player player) {
        return true;
    }

    public static void handlePossessionInteraaction(@NotNull Player player, @NotNull ServerboundPossessionInteractPacket packet, @NotNull LivingEntity possessed) {
        // If it's not an attack, it's an interaction
        if(packet.attack()) {

            if(possessed instanceof Creeper creeper) {
                creeper.ignite();
                return;
            }

            int dist = possessed instanceof RangedAttackMob ? 100 : 10;
            Vec3 eyePos = possessed.getEyePosition();
            Vec3 view = possessed.getViewVector(1.0F).scale(dist);
            Vec3 viewPos = eyePos.add(view);
            AABB aabb = possessed.getBoundingBox().expandTowards(view).inflate(1.0);
            EntityHitResult result = ProjectileUtil.getEntityHitResult(possessed, eyePos, viewPos, aabb, p -> p instanceof LivingEntity, dist);
            if(result != null && result.getEntity() instanceof LivingEntity living && possessed.canAttack(living)) {

                if(possessed instanceof RangedAttackMob ranged) {
                    ranged.performRangedAttack(living, 1.0f);
                } else if(possessed.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)){
                    float damage = (float) possessed.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    possessed.swing(InteractionHand.MAIN_HAND);
                    living.hurt(possessed.damageSources().mobAttack(possessed), damage);
                }

            }


        } else {
            int dist = 8;
            Vec3 eyePos = possessed.getEyePosition();
            Vec3 view = possessed.getViewVector(1.0F).scale(dist);
            Vec3 viewPos = eyePos.add(view);
            AABB aabb = possessed.getBoundingBox().expandTowards(view).inflate(1.0);
            EntityHitResult entityResult = ProjectileUtil.getEntityHitResult(possessed, eyePos, viewPos, aabb, p -> p instanceof LivingEntity, dist);

            BlockHitResult blockResult = possessed.level().clip(new ClipContext(eyePos, viewPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, possessed));

            if(entityResult == null || eyePos.distanceTo(blockResult.getLocation()) < eyePos.distanceTo(entityResult.getLocation())) {
                BlockPos hitPos = blockResult.getBlockPos();
                BlockState block = possessed.level().getBlockState(hitPos);
                block.useWithoutItem(possessed.level(), player, blockResult);
            }

        }
    }



    public static void handlePossessionMovement(@NotNull ServerboundPossessionInputPacket packet, @NotNull LivingEntity possessed) {
        possessed.setYHeadRot(packet.yRotHead());
        possessed.absRotateTo(packet.yRot(), packet.xRot());
        boolean flying = possessed instanceof FlyingMob || possessed.isNoGravity();
        float speedFactor = flying ? 2f : packet.sprint() ? 0.4f : 0.15f;
        possessed.setSprinting(packet.sprint());
        possessed.setSpeed((float) possessed.getAttributeValue(Attributes.MOVEMENT_SPEED) * speedFactor);

        float verticalMotion = 0f;

        if(flying && packet.shift()) {
            verticalMotion = -1.0f;
        }

        if(packet.jumping()) {
            if(flying) {
                verticalMotion = 1.0f;
            } else if(possessed.onGround()) {
                possessed.jumpFromGround();
            }

        }

        travel(new Vec3(packet.sideways(), verticalMotion, packet.forward()), possessed);

    }

    // LivingEntity#travel
    // We need to make a few changes for possession to work cleanly, notably differences in Entity#isControlledByLocalInstance, cleaner solution than with mixins
    // We would otherwise have to redirect #isControlledByLocalInstance in #travel, since modifying it's return value would cause other issues.
    //

    private static void travel(Vec3 pTravelVector, LivingEntity possessed) {

        if(possessed instanceof FlyingMob mob) {
            flyingTravel(pTravelVector, mob);
            return;
        }

        double d0 = possessed.getGravity();
        boolean flag = possessed.getDeltaMovement().y <= 0.0;
        if (flag && possessed.hasEffect(MobEffects.SLOW_FALLING)) {
            d0 = Math.min(d0, 0.01);
        }

        LivingEntityAccessor lAccessor = ((LivingEntityAccessor) possessed);
        EntityInvoker eAccessor = ((EntityInvoker) possessed);

        FluidState fluidstate = possessed.level().getFluidState(possessed.blockPosition());
        if ((possessed.isInWater() || (possessed.isInFluidType(fluidstate) && fluidstate.getFluidType() != net.neoforged.neoforge.common.NeoForgeMod.LAVA_TYPE.value())) && lAccessor.invokeAffectedByFluids() && !possessed.canStandOnFluid(fluidstate)) {
            if (possessed.isInWater() || (possessed.isInFluidType(fluidstate) && !possessed.moveInFluid(fluidstate, pTravelVector, d0))) {
                double d9 = possessed.getY();
                float f4 = possessed.isSprinting() ? 0.9F : lAccessor.invokeGetWaterSlowDown();
                float f5 = 0.02F;
                float f6 = (float) possessed.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
                if (!possessed.onGround()) {
                    f6 *= 0.5F;
                }

                if (f6 > 0.0F) {
                    f4 += (0.54600006F - f4) * f6;
                    f5 += (possessed.getSpeed() - f5) * f6;
                }

                if (possessed.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                    f4 = 0.96F;
                }

                f5 *= (float) possessed.getAttributeValue(net.neoforged.neoforge.common.NeoForgeMod.SWIM_SPEED);
                possessed.moveRelative(f5, pTravelVector);
                possessed.move(MoverType.SELF, possessed.getDeltaMovement());
                Vec3 vec36 = possessed.getDeltaMovement();
                if (possessed.horizontalCollision && possessed.onClimbable()) {
                    vec36 = new Vec3(vec36.x, 0.2, vec36.z);
                }

                possessed.setDeltaMovement(vec36.multiply((double) f4, 0.8F, (double) f4));
                Vec3 vec32 = possessed.getFluidFallingAdjustedMovement(d0, flag, possessed.getDeltaMovement());
                possessed.setDeltaMovement(vec32);
                if (possessed.horizontalCollision && possessed.isFree(vec32.x, vec32.y + 0.6F - possessed.getY() + d9, vec32.z)) {
                    possessed.setDeltaMovement(vec32.x, 0.3F, vec32.z);
                }
            }
        } else if (possessed.isInLava() && lAccessor.invokeAffectedByFluids() && !possessed.canStandOnFluid(fluidstate)) {
            double d8 = possessed.getY();
            possessed.moveRelative(0.02F, pTravelVector);
            possessed.move(MoverType.SELF, possessed.getDeltaMovement());
            if (possessed.getFluidHeight(FluidTags.LAVA) <= possessed.getFluidJumpThreshold()) {
                possessed.setDeltaMovement(possessed.getDeltaMovement().multiply(0.5, 0.8F, 0.5));
                Vec3 vec33 = possessed.getFluidFallingAdjustedMovement(d0, flag, possessed.getDeltaMovement());
                possessed.setDeltaMovement(vec33);
            } else {
                possessed.setDeltaMovement(possessed.getDeltaMovement().scale(0.5));
            }

            if (d0 != 0.0) {
                possessed.setDeltaMovement(possessed.getDeltaMovement().add(0.0, -d0 / 4.0, 0.0));
            }

            Vec3 vec34 = possessed.getDeltaMovement();
            if (possessed.horizontalCollision && possessed.isFree(vec34.x, vec34.y + 0.6F - possessed.getY() + d8, vec34.z)) {
                possessed.setDeltaMovement(vec34.x, 0.3F, vec34.z);
            }
        } else if (possessed.isFallFlying()) {
            possessed.checkSlowFallDistance();
            Vec3 vec3 = possessed.getDeltaMovement();
            Vec3 vec31 = possessed.getLookAngle();
            float f = possessed.getXRot() * (float) (Math.PI / 180.0);
            double d1 = Math.sqrt(vec31.x * vec31.x + vec31.z * vec31.z);
            double d3 = vec3.horizontalDistance();
            double d4 = vec31.length();
            double d5 = Math.cos((double) f);
            d5 = d5 * d5 * Math.min(1.0, d4 / 0.4);
            vec3 = possessed.getDeltaMovement().add(0.0, d0 * (-1.0 + d5 * 0.75), 0.0);
            if (vec3.y < 0.0 && d1 > 0.0) {
                double d6 = vec3.y * -0.1 * d5;
                vec3 = vec3.add(vec31.x * d6 / d1, d6, vec31.z * d6 / d1);
            }

            if (f < 0.0F && d1 > 0.0) {
                double d10 = d3 * (double) (-Mth.sin(f)) * 0.04;
                vec3 = vec3.add(-vec31.x * d10 / d1, d10 * 3.2, -vec31.z * d10 / d1);
            }

            if (d1 > 0.0) {
                vec3 = vec3.add((vec31.x / d1 * d3 - vec3.x) * 0.1, 0.0, (vec31.z / d1 * d3 - vec3.z) * 0.1);
            }

            possessed.setDeltaMovement(vec3.multiply(0.99F, 0.98F, 0.99F));
            possessed.move(MoverType.SELF, possessed.getDeltaMovement());
            if (possessed.horizontalCollision && !possessed.level().isClientSide) {
                double d11 = possessed.getDeltaMovement().horizontalDistance();
                double d7 = d3 - d11;
                float f1 = (float) (d7 * 10.0 - 3.0);
                if (f1 > 0.0F) {
                    possessed.playSound(lAccessor.invokeGetFallDamageSound((int) f1), 1.0F, 1.0F);
                    possessed.hurt(possessed.damageSources().flyIntoWall(), f1);
                }
            }

            if (possessed.onGround() && !possessed.level().isClientSide) {
                eAccessor.invokeSetFlag(7, false);
            }
        } else {
            BlockPos blockpos = possessed.getBlockPosBelowThatAffectsMyMovement();
            float f2 = possessed.level().getBlockState(possessed.getBlockPosBelowThatAffectsMyMovement()).getFriction(possessed.level(), possessed.getBlockPosBelowThatAffectsMyMovement(), possessed);
            float f3 = possessed.onGround() ? f2 * 0.91F : 0.91F;
            Vec3 vec35 = possessed.handleRelativeFrictionAndCalculateMovement(pTravelVector, f2);
            double d2 = vec35.y;
            if (possessed.hasEffect(MobEffects.LEVITATION)) {
                d2 += (0.05 * (double) (possessed.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec35.y) * 0.2;
            } else if (!possessed.level().isClientSide || possessed.level().hasChunkAt(blockpos)) {
                d2 -= d0;
            } else if (possessed.getY() > (double) possessed.level().getMinBuildHeight()) {
                d2 = -0.1;
            } else {
                d2 = 0.0;
            }

            if (possessed.shouldDiscardFriction()) {
                possessed.setDeltaMovement(vec35.x, d2, vec35.z);
            } else {
                possessed.setDeltaMovement(vec35.x * (double) f3, possessed instanceof FlyingAnimal ? d2 * (double) f3 : d2 * 0.98F, vec35.z * (double) f3);
            }
        }

        possessed.calculateEntityAnimation(possessed instanceof FlyingAnimal);

    }
    public static void flyingTravel(Vec3 pTravelVector, FlyingMob mob) {
        if (mob.isInWater()) {
            mob.moveRelative(0.02F, pTravelVector);
            mob.move(MoverType.SELF, mob.getDeltaMovement());
            mob.setDeltaMovement(mob.getDeltaMovement().scale(0.8F));
        } else if (mob.isInLava()) {
            mob.moveRelative(0.02F, pTravelVector);
            mob.move(MoverType.SELF, mob.getDeltaMovement());
            mob.setDeltaMovement(mob.getDeltaMovement().scale(0.5));
        } else {
            BlockPos ground = mob.getBlockPosBelowThatAffectsMyMovement();
            float f = 0.91F;
            if (mob.onGround()) {
                f = mob.level().getBlockState(ground).getFriction(mob.level(), ground, mob) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (mob.onGround()) {
                f = mob.level().getBlockState(ground).getFriction(mob.level(), ground, mob) * 0.91F;
            }

            mob.moveRelative(mob.onGround() ? 0.1F * f1 : 0.02F, pTravelVector);
            mob.move(MoverType.SELF, mob.getDeltaMovement());
            mob.setDeltaMovement(mob.getDeltaMovement().scale((double)f));
        }

        mob.calculateEntityAnimation(false);
    }


}
