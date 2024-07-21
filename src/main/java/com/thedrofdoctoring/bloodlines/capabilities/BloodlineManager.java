package com.thedrofdoctoring.bloodlines.capabilities;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.core.BloodlineAttachments;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.lib.HelperLib;
import de.teamlapen.lib.lib.storage.IAttachment;
import de.teamlapen.lib.lib.storage.ISyncable;
import de.teamlapen.lib.lib.storage.ISyncableSaveData;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.teamlapen.vampirism.entity.factions.FactionPlayerHandler.getCurrentFactionPlayer;

public class BloodlineManager implements IBloodlineManager, IAttachment {
    public static final String NBT_KEY = "bloodline_manager";
    public static final ResourceLocation BlOODLINE_KEY = Bloodlines.rl(NBT_KEY);
    public static @NotNull BloodlineManager get(@NotNull Player player) {
        return player.getData(BloodlineAttachments.BLOODLINE_MANAGER.get());
    }

    public static @NotNull Optional<BloodlineManager> getOpt(@NotNull Player player) {
        return Optional.of(player.getData(BloodlineAttachments.BLOODLINE_MANAGER.get()));
    }

    @Override
    public String nbtKey() {
        return NBT_KEY;
    }


    public static class Serializer implements IAttachmentSerializer<CompoundTag, BloodlineManager> {

        @Override
        public @NotNull BloodlineManager read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
            if (holder instanceof Player player) {
                BloodlineManager handler = new BloodlineManager(player);
                handler.deserializeNBT(provider, tag);
                return handler;
            }
            throw new IllegalStateException("Cannot deserialize Bloodline Manager for non player entity");
        }

        @Override
        public CompoundTag write(BloodlineManager attachment, HolderLookup.@NotNull Provider provider) {
            return attachment.serializeNBT(provider);
        }
    }

    public static class Factory implements Function<IAttachmentHolder, BloodlineManager> {

        @Override
        public BloodlineManager apply(IAttachmentHolder holder) {
            if (holder instanceof Player player) {
                return new BloodlineManager(player);
            }
            throw new IllegalArgumentException("Cannot create bloodline manager attachment for holder " + holder.getClass() + ". Expected Player");
        }
    }

    private int bloodlineRank;
    private final Player player;
    private ResourceLocation bloodlineName;
    private final BloodlineSkillHandler skillHandler;
    private IBloodline bloodline;

    public BloodlineManager(Player player) {
        this.player = player;
        this.skillHandler = new BloodlineSkillHandler();

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
            skillHandler.setEnabledSkills(0);
            skillHandler.setSkillPoints(0);
        }


    }

    @Override
    public IBloodline getBloodline() {
        for(IBloodline bl : BloodlineRegistry.getBloodlines()) {
            if(bl.getBloodlineId().equals(this.bloodlineName)) {
                return bl;
            }
        }
        return null;
    }
    @Override
    public IBloodline getBloodlineById(ResourceLocation id) {
        for(IBloodline bl : BloodlineRegistry.getBloodlines()) {
            if(bl.getBloodlineId().equals(id)) {
                return bl;
            }
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
            skillHandler.setEnabledSkills(0);
            skillHandler.setSkillPoints(0);
        }
    }

    @Override
    public void onBloodlineChange(IBloodline oldBloodline, int oldRank) {
        //If the oldbloodline was a proper bloodline and different from current one, we need to remove all bloodline skills of that type
        if(oldBloodline != null && oldBloodline != bloodline) {
            ArrayList<ISkill<?>> blSkills = BloodlineSkills.getBloodlineTypeSkills(oldBloodline.getSkillType());

            FactionPlayerHandler.get(player).getCurrentFactionPlayer().ifPresent(pl -> {
                //noinspection rawtypes
                for(ISkill blSkill : blSkills) {
                    if(pl.getSkillHandler().isSkillEnabled(blSkill)) {
                        //noinspection unchecked
                        pl.getSkillHandler().disableSkill(blSkill);
                    }
                }
            });
            FactionPlayerHandler.get(player).checkSkillTreeLocks();;
            this.skillHandler.setSkillPoints(0);
            if(bloodline != null) {
                oldBloodline.onBloodlineChange(player, 0);
                bloodline.onBloodlineChange(player, this.getRank());
                FactionPlayerHandler.get(player).checkSkillTreeLocks();;
            }
        } else if(bloodlineRank != oldRank && bloodline != null) {
            bloodline.onBloodlineChange(player, this.getRank());
            if(oldRank == 0) {
                FactionPlayerHandler.get(player).checkSkillTreeLocks();
            }

        }

        updateAttributes(oldBloodline);
        sync(true);
    }
    @Override
    public void updateAttributes(IBloodline oldBloodline) {
        if (oldBloodline != null && oldBloodline != bloodline && !player.getCommandSenderWorld().isClientSide) {
            Map<Holder<Attribute>, AttributeModifier> oldAttributes = oldBloodline.getBloodlineAttributes(1, player);
            oldAttributes.forEach((attribute, modifier) -> {
                removeModifier(player.getAttribute(attribute), modifier.id());
            });
        }
        if (bloodline != null && !player.getCommandSenderWorld().isClientSide) {
            Map<Holder<Attribute>, AttributeModifier> attributes = bloodline.getBloodlineAttributes(getRank(), player);
            attributes.forEach((attribute, modifier) -> {
                removeModifier(player.getAttribute(attribute), modifier.id());
                player.getAttribute(attribute).addPermanentModifier(modifier);
            });
        }
    }


    @Override
    public Player getPlayer() {
        return this.player;
    }

    public static void removeModifier(@NotNull AttributeInstance att, @NotNull ResourceLocation location) {
        AttributeModifier m = att.getModifier(location);
        if (m != null) {
            att.removeModifier(m);
        }
    }

    @Override
    public BloodlineSkillHandler getSkillHandler() {
        return this.skillHandler;
    }

    @Override
    public ResourceLocation getAttachedKey() {
        return BlOODLINE_KEY;
    }

    @Override
    public Entity asEntity () {
        return player;
    }
    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        if(nbt.contains("bloodline")) {
            this.bloodlineRank = nbt.getInt("rank");
            this.bloodlineName = ResourceLocation.parse(nbt.getString("bloodline"));
            this.bloodline = getBloodline();
            skillHandler.deserializeNBT(nbt);
            onBloodlineChange(bloodline, bloodlineRank);
        }
    }
    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        if(bloodline != null) {
            nbt.putString("bloodline", bloodlineName.toString());
            nbt.putInt("rank", bloodlineRank);
            nbt = skillHandler.serializeNBT(nbt);
        }
        return nbt;
    }

    @Override
    public void deserializeUpdateNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        if(nbt.getString("bloodline").isEmpty()) {
            this.bloodline = null;
            this.bloodlineRank = 0;
            this.bloodlineName = null;
            skillHandler.setSkillPoints(0);
        } else {
            this.bloodlineName = ResourceLocation.parse(nbt.getString("bloodline"));
            this.bloodline = getBloodline();
            this.bloodlineRank = nbt.getInt("rank");
            skillHandler.deserializeUpdateNBT(nbt);
            bloodline.onBloodlineChange(player, this.bloodlineRank);
        }

    }


    @Override
    public @NotNull CompoundTag serializeUpdateNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("bloodline", bloodline == null ? "" : bloodline.getBloodlineId().toString());
        nbt.putInt("rank", bloodlineRank);
        nbt = skillHandler.serializeUpdateNBT(nbt);
        return nbt;
    }
    public void sync(boolean all) {
        HelperLib.sync(this, player, all);
    }
}
