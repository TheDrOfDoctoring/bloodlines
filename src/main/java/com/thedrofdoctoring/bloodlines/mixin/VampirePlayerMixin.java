package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineZealot;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.IVampSpecialAttributes;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.FactionBasePlayer;
import de.teamlapen.vampirism.entity.player.vampire.BloodStats;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayerSpecialAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(VampirePlayer.class)
public abstract class VampirePlayerMixin extends FactionBasePlayer<IVampirePlayer> implements IVampirePlayer{
    @Shadow private int ticksInSun;

    @Shadow @NotNull public abstract ISkillHandler<IVampirePlayer> getSkillHandler();

    @Shadow @NotNull public abstract VampirePlayerSpecialAttributes getSpecialAttributes();

    @Shadow private boolean sundamage_cache;
    @Shadow @Final private @NotNull BloodStats bloodStats;
    @Unique
    private final int bloodlines$sunTicksPerIncrease = CommonConfig.sunTicksPerIncrease.get();

    public VampirePlayerMixin(Player player) {
        super(player);
    }
    //Increases the amount of time for a player to have ticksInSun increased when they have the correct skill enabled.
    @WrapOperation(method = "handleSunDamage", at = @At(value = "FIELD", target = "Lde/teamlapen/vampirism/entity/player/vampire/VampirePlayer;ticksInSun:I", opcode = Opcodes.PUTFIELD, ordinal = 0))
    private void modifyTicksInSun(VampirePlayer instance, int value, Operation<Void> original) {
        if(player.tickCount % bloodlines$sunTicksPerIncrease == 0 &&((IVampSpecialAttributes)getSpecialAttributes()).bloodlines$getSlowSun()) {
            original.call(instance, value + 1);
        } else if(!((IVampSpecialAttributes)getSpecialAttributes()).bloodlines$getSlowSun()){
            original.call(instance, value + 1);
        }
        if(BloodlineManager.get(player).getBloodline() == BloodlineRegistry.BLOODLINE_ZEALOT && BloodlineManager.get(player).getRank() >= CommonConfig.zealotDoubleSunTickRank.get()) {
            original.call(instance, value + 1);
        }

    }
    @Inject(method = "isGettingSundamage", at = @At("RETURN"), cancellable = true)
    private void ectothermDiffraction(LevelAccessor iWorld, boolean forcerefresh, CallbackInfoReturnable<Boolean> cir) {
        if(((IVampSpecialAttributes)getSpecialAttributes()).bloodlines$getRefraction()) {
            BlockPos eyePos = BlockPos.containing(player.getEyePosition());
            if(player.level().getBlockState(eyePos).is(Blocks.WATER)) {
                this.sundamage_cache = false;
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setAirSupply(I)V", ordinal = 1), remap = false)
    public void nobleWorsenedWaterEffects(CallbackInfo ci) {
        if(CommonConfig.nobleWaterWeakness.get() && this.getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_RANK_3.get())) {
            if (this.player.tickCount % 16 == 4 && !this.player.getAbilities().instabuild && this.player.isInWater()) {
                Level world = this.player.getCommandSenderWorld();
                FluidState state1 = world.getFluidState(this.player.blockPosition());
                FluidState state2 = world.getFluidState(this.player.blockPosition().above());
                if (state1.is(FluidTags.WATER) && state1.getFlow(world, this.player.blockPosition()).lengthSqr() > 0.0 || state2.is(FluidTags.WATER) && state2.getFlow(world, this.player.blockPosition().above()).lengthSqr() > 0.0) {
                    int amplifier = getSpecialAttributes().waterResistance ? 1 : 2;
                    this.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, amplifier));
                }
            }
        }
    }
}
