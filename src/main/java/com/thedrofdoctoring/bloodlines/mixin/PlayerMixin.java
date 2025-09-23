package com.thedrofdoctoring.bloodlines.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.IBloodlinesPlayer;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IBloodlinesPlayer {

    // Similar to VampirismPlayerAttributes, but for bloodlines abilities. Certain values cached for performance, capability for accuracy.
    @Unique
    private final BloodlinesPlayerAttributes bloodlines$bloodlinesPlayerAttributes = new BloodlinesPlayerAttributes();

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @ModifyReturnValue(method = "getBlockSpeedFactor", at = @At("RETURN"))
    private float zealotStoneSpeed(float originalSpeed) {
        Player player = (Player) (Object) this;
        IBloodline bloodline = bloodlines$bloodlinesPlayerAttributes.bloodline;
        if(bloodline == BloodlineRegistry.BLOODLINE_ZEALOT.get() && bloodlines$onStone(player)) {
            float speedMultiplier = bloodlines$bloodlinesPlayerAttributes.getZealotAtts().stoneSpeed;
            return originalSpeed * speedMultiplier;
        }
        else if(bloodline == BloodlineRegistry.BLOODLINE_GRAVEBOUND.get() && bloodlines$soulSpeed(player)) {
            float speedMultiplier = bloodlines$bloodlinesPlayerAttributes.getGraveboundData().soulSpeed;
            return originalSpeed * speedMultiplier;
        }
        return originalSpeed;
    }
    @Unique
    private boolean bloodlines$onStone(Player player) {
        return player.level().getBlockState(player.getOnPos()).is(BloodlinesTagsProviders.BloodlinesBlockTagProvider.ZEALOT_STONE);
    }
    @Unique
    private boolean bloodlines$soulSpeed(Player player) {
        return player.level().getBlockState(player.getOnPos()).is(BlockTags.SOUL_SPEED_BLOCKS);
    }

    @Unique
    @Override
    public BloodlinesPlayerAttributes bloodlines$getBloodlinesAtts() {
        return bloodlines$bloodlinesPlayerAttributes;
    }
}