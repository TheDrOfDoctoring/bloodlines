package com.thedrofdoctoring.bloodlines.capabilities.other;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineSkillHandler;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.lib.HelperLib;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.vampire.IVampire;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.vampire.VampireBaseEntity;
import de.teamlapen.vampirism.util.DamageHandler;
import de.teamlapen.vampirism.world.ModDamageSources;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * This acts as a replacement for the typical ExtendedCreature, to allow support for the death knight vampire feeding.
 * Not sure if there's a way to prevent duplicate extended creature attachments for other mobs
 */
public class VampExtendedCreature extends ExtendedCreature {

    public static final String NBT_KEY = "vamp_extended_creature";
    public static final ResourceLocation VAMP_CREATURE_KEY = Bloodlines.rl(NBT_KEY);


    public VampExtendedCreature(PathfinderMob entity) {
        super(entity);
    }


    @Override
    public @NotNull ResourceLocation getAttachedKey() {
        return VAMP_CREATURE_KEY;
    }
    @Override
    public String nbtKey() {
        return NBT_KEY;
    }
    @Override
    public void sync() {
        HelperLib.sync(this, this.getEntity(), true);
    }


    @Override
    public int onBite(IVampire biter) {
        if (this.getBlood() <= 0) {
            return 0;
        } else {
            int blood = this.getBlood();
            int amt = Math.max(1, this.getMaxBlood() / (biter instanceof VampirePlayer ? 6 : 2));
            if (amt >= blood) {
                if (blood > 1 && biter.isAdvancedBiter()) {
                    amt = blood - 1;
                } else {
                    amt = blood;
                }
            }

            blood = blood - amt;
            this.setBlood(blood);
            PathfinderMob mob = this.getEntity();
            if (blood == 0) {
                DamageHandler.hurtModded(mob, ModDamageSources::noBlood, 1000.0F);
            }

            this.sync();
            mob.setLastHurtByMob(biter.asEntity());

            if (biter.isAdvancedBiter() && mob.getRandom().nextInt(4) == 0) {
                amt = 2 * amt;
            }

            return amt;
        }
    }

    @Override
    public void setBlood(int blood) {
        super.setBlood(blood);
    }

    @Override
    public boolean canBeBitten(IVampire biter) {
        PathfinderMob mob = this.getEntity();

        if(biter instanceof VampirePlayer vp) {
            Player player = vp.asEntity();
            IBloodline bloodline = BloodlineManager.get(player).getBloodline();
            if(bloodline != BloodlineRegistry.BLOODLINE_BLOODKNIGHT.get()) return false;

            boolean modifiedDuration = vp.getSkillHandler().isSkillEnabled(BloodlineSkills.BLOODKNIGHT_FEIGNED_MERCY.get());
            float percentageHealth = modifiedDuration ? CommonConfig.bloodknightFeignedVampireMaxHealthFeedingAmount.get().floatValue() : CommonConfig.bloodknightVampireMinHealthFeedingPercentage.get().floatValue();

            return (mob.getHealth() / mob.getMaxHealth()) <= percentageHealth;
        }
        return false;
    }

    @Override
    public boolean canBeInfected(IVampire vampire) {
        return false;
    }


    @Override
    public void setPoisonousBlood(boolean poisonous) {}



    @Override
    public boolean hasPoisonousBlood() {
        return false;
    }

    @Override
    public boolean canBecomeVampire() {
        return false;
    }

    @Override
    public @Nullable IConvertedCreature<?> makeVampire() {
        return null;
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, ExtendedCreature> {

        @Override
        public @NotNull ExtendedCreature read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
            if (holder instanceof VampireBaseEntity mob) {
                ExtendedCreature creature = new VampExtendedCreature(mob);
                creature.deserializeNBT(provider, tag);
                return creature;
            } else if(holder instanceof PathfinderMob mob) {
                ExtendedCreature creature = new ExtendedCreature(mob);
                creature.deserializeNBT(provider, tag);
                return creature;
            }

            throw new IllegalArgumentException("Expected PathfinderMob, got " + holder.getClass().getSimpleName());
        }

        @Override
        public CompoundTag write(ExtendedCreature attachment, HolderLookup.@NotNull Provider provider) {
            return attachment.serializeNBT(provider);
        }
    }

    public static class Factory implements Function<IAttachmentHolder, ExtendedCreature> {

        @Override
        public ExtendedCreature apply(IAttachmentHolder holder) {
            if (holder instanceof VampireBaseEntity mob) {
                return new VampExtendedCreature(mob);
            } else if(holder instanceof PathfinderMob mob) {
                return new ExtendedCreature(mob);
            }
            throw new IllegalArgumentException("Cannot create extended creature handler attachment for holder " + holder.getClass() + ". Expected PathfinderMob}");


        }
    }
}
