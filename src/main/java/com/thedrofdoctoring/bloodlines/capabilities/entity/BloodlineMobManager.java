package com.thedrofdoctoring.bloodlines.capabilities.entity;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.IBloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.core.BloodlineAttachments;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.lib.lib.storage.IAttachment;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class BloodlineMobManager implements IBloodlineManager, IAttachment {

    public static final String NBT_KEY = "bloodline_mob_manager";
    public static final ResourceLocation BlOODLINE_KEY = Bloodlines.rl(NBT_KEY);

    public static @NotNull Optional<BloodlineMobManager> getSafe(@NotNull Entity mob) {
        if (mob instanceof PathfinderMob pathfinderMob) {
            return Optional.of(pathfinderMob.getData(BloodlineAttachments.BLOODLINE_MOB_MANAGER.get()));
        }
        return Optional.empty();
    }
    public static BloodlineMobManager get(PathfinderMob mob) {
        return mob.getData(BloodlineAttachments.BLOODLINE_MOB_MANAGER.get());
    }


    @Override
    public String nbtKey() {
        return NBT_KEY;
    }


    public static class Serializer implements IAttachmentSerializer<CompoundTag, BloodlineMobManager> {

        @Override
        public @NotNull BloodlineMobManager read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
            if (holder instanceof PathfinderMob entity) {
                BloodlineMobManager handler = new BloodlineMobManager(entity);
                handler.deserializeNBT(provider, tag);
                return handler;
            }
            throw new IllegalStateException("Cannot deserialize Bloodline Manager for non PathfinderMob");
        }

        @Override
        public CompoundTag write(BloodlineMobManager attachment, HolderLookup.@NotNull Provider provider) {
            return attachment.serializeNBT(provider);
        }
    }

    public static class Factory implements Function<IAttachmentHolder, BloodlineMobManager> {

        @Override
        public BloodlineMobManager apply(IAttachmentHolder holder) {
            if (holder instanceof PathfinderMob mob) {
                return new BloodlineMobManager(mob);
            }
            throw new IllegalArgumentException("Cannot create bloodline manager attachment for holder " + holder.getClass() + ". Expected PathfinderMob");
        }
    }

    private int bloodlineRank;
    private final PathfinderMob entity;
    private ResourceLocation bloodlineName;
    private IBloodline bloodline;

    public BloodlineMobManager(PathfinderMob entity) {
        this.entity = entity;

    }

    @Override
    public int getRank() {
        return this.bloodlineRank;
    }

    @Override
    public void setRank(int bloodlineRank) {
        this.bloodlineRank = bloodlineRank;
        if(bloodlineRank == 0) {
            bloodlineName = null;
            bloodline = null;
        }


    }

    @Override
    public IBloodline getBloodline() {
        if(BloodlineRegistry.BLOODLINE_REGISTRY.containsKey(this.bloodlineName)) {
            return BloodlineRegistry.BLOODLINE_REGISTRY.get(this.bloodlineName);
        }
        return null;
    }

    @Override
    public ResourceLocation getBloodlineId() {
        return this.bloodlineName;
    }

    @Override
    public void setBloodline(IBloodline bloodline) {
        if(bloodline != null) {
            this.bloodline = bloodline;
            this.bloodlineName = bloodline.getBloodlineId();
        } else {
            this.bloodline = null;
            this.bloodlineName = null;
            this.bloodlineRank = 0;
        }
    }

    @Override
    public void updateAttributes(IBloodline oldBloodline) {
        if (oldBloodline != null && oldBloodline != bloodline && !entity.getCommandSenderWorld().isClientSide) {
            Map<Holder<Attribute>, AttributeModifier> oldAttributes = oldBloodline.getBloodlineAttributes(1, entity, true);
            oldAttributes.forEach((attribute, modifier) -> {
                AttributeInstance att = entity.getAttribute(attribute);
                if(att == null) return;
                removeModifier(att, modifier.id());
            });
        }
        if (bloodline != null && !entity.getCommandSenderWorld().isClientSide) {
            Map<Holder<Attribute>, AttributeModifier> attributes = bloodline.getBloodlineAttributes(getRank(), entity, false);
            attributes.forEach((attribute, modifier) -> {
                AttributeInstance att = entity.getAttribute(attribute);
                if(att == null) return;
                removeModifier(att, modifier.id());
                entity.getAttribute(attribute).addPermanentModifier(modifier);
            });
        }
    }

    @Override
    public void onBloodlineChange(IBloodline oldBloodline, int oldRank) {

        if(oldBloodline != null && oldBloodline != bloodline) {
            if(bloodline != null) {
                oldBloodline.onBloodlineChange(entity, 0);
                bloodline.onBloodlineChange(entity, this.getRank());
            }
        } else if(bloodlineRank != oldRank && bloodline != null) {
            bloodline.onBloodlineChange(entity, this.getRank());
        }

        updateAttributes(oldBloodline);
        sync(true);
    }

    @Override
    public PathfinderMob getEntity() {
        return this.entity;
    }

    private static void removeModifier(@NotNull AttributeInstance att, @NotNull ResourceLocation location) {

        AttributeModifier m = att.getModifier(location);
        if (m != null) {
            att.removeModifier(m);
        }
    }


    @Override
    public ResourceLocation getAttachedKey() {
        return BlOODLINE_KEY;
    }

    @Override
    public Entity asEntity () {
        return entity;
    }
    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        if(nbt.contains("bloodline")) {
            this.bloodlineRank = nbt.getInt("rank");
            this.bloodlineName = ResourceLocation.parse(nbt.getString("bloodline"));
            this.bloodline = getBloodline();

        }
    }
    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        if(bloodline != null) {
            nbt.putString("bloodline", bloodlineName.toString());
            nbt.putInt("rank", bloodlineRank);
        }
        return nbt;
    }

    @Override
    public void deserializeUpdateNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        if(nbt.getString("bloodline").isEmpty()) {
            this.bloodline = null;
            this.bloodlineRank = 0;
            this.bloodlineName = null;
        } else {
            this.bloodlineName = ResourceLocation.parse(nbt.getString("bloodline"));
            this.bloodline = getBloodline();
            this.bloodlineRank = nbt.getInt("rank");
        }

    }

    @Override
    public @NotNull CompoundTag serializeUpdateNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("bloodline", bloodline == null ? "" : bloodline.getBloodlineId().toString());
        nbt.putInt("rank", bloodlineRank);
        return nbt;
    }

}
