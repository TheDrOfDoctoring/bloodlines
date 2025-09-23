package com.thedrofdoctoring.bloodlines.capabilities.bloodlines;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlineState;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.data.BloodlinesPlayerAttributes;
import com.thedrofdoctoring.bloodlines.core.BloodlinesAttachments;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.skills.BloodlineParentSkill;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.lib.lib.storage.IAttachment;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import java.util.function.Function;

public class BloodlineManager implements IBloodlineManager, IAttachment {
    public static final String NBT_KEY = "bloodline_manager";
    public static final ResourceLocation BlOODLINE_KEY = Bloodlines.rl(NBT_KEY);
    public static @NotNull BloodlineManager get(@NotNull Player player) {
        return player.getData(BloodlinesAttachments.BLOODLINE_MANAGER.get());
    }

    public static @NotNull Optional<BloodlineManager> getOpt(@NotNull Player player) {
        return Optional.of(player.getData(BloodlinesAttachments.BLOODLINE_MANAGER.get()));
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
    private BloodlineState bloodlineState;
    private IBloodline bloodline;

    public BloodlineManager(Player player) {
        this.player = player;
        this.skillHandler = new BloodlineSkillHandler();
        skillHandler.setEnabledSkills(0);

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
            skillHandler.clearSkillPoints();
        }


    }

    @Override
    public IBloodline getBloodline() {
        if(this.bloodline == null) {
            if(BloodlineRegistry.BLOODLINE_REGISTRY.containsKey(this.bloodlineName)) {
                return (this.bloodline = BloodlineRegistry.BLOODLINE_REGISTRY.get(this.bloodlineName));
            }
        }
        return this.bloodline;
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
            if(this.bloodlineState != null) this.bloodlineState.clear(player.level());
            this.bloodlineState = null;
            skillHandler.setEnabledSkills(0);
            skillHandler.clearSkillPoints();
        }
    }

    @Override
    public void onBloodlineChange(IBloodline oldBloodline, int oldRank) {
        if(oldBloodline != null && oldBloodline != bloodline) {
            ArrayList<ISkill<?>> blSkills = BloodlineSkills.getBloodlineTypeSkills(oldBloodline);

            FactionPlayerHandler.get(player).getCurrentFactionPlayer().ifPresent(pl -> {
                //noinspection rawtypes
                for(ISkill blSkill : blSkills) {
                    if(pl.getSkillHandler().isSkillEnabled(blSkill)) {
                        //noinspection unchecked
                        pl.getSkillHandler().disableSkill(blSkill);
                    }
                }
            });
            FactionPlayerHandler.get(player).checkSkillTreeLocks();
            skillHandler.clearSkillPoints();
            if(this.bloodlineState != null) this.bloodlineState.clear(player.level());
            this.bloodlineState = null;
            if(bloodline != null) {
                if(bloodline.getNewBloodlineState(player).isPresent()) {
                    this.bloodlineState = bloodline.getNewBloodlineState(player).get();
                }
                oldBloodline.onBloodlineChange(player, 0);
                bloodline.onBloodlineChange(player, this.getRank());
                FactionPlayerHandler.get(player).checkSkillTreeLocks();
            }
            skillHandler.clearSkillPoints();


        } else if(bloodlineRank != oldRank && bloodline != null) {
            bloodline.onBloodlineChange(player, this.getRank());
            if(oldRank == 0) {
                FactionPlayerHandler.get(player).checkSkillTreeLocks();
            }
            if(oldRank > bloodlineRank) {
                ISkillHandler<?> skillHandler = bloodline.getSkillHandler(player);
                disableUntilEligible(skillHandler);
            }
            if(bloodline.getNewBloodlineState(player).isPresent() && this.bloodlineState == null) {
                this.bloodlineState = bloodline.getNewBloodlineState(player).get();
            }

        }
        updateAttributes(oldBloodline);
        sync(true);
        updatePlayerCache();
    }

    @Override
    public Optional<BloodlineState> getBloodlineState() {
        return Optional.ofNullable(this.bloodlineState);
    }

    public void updatePlayerCache() {
        BloodlinesPlayerAttributes attributes = BloodlinesPlayerAttributes.get(player);
        attributes.bloodline = this.bloodline;
        attributes.bloodlineRank = this.bloodlineRank;
        if(this.bloodlineState != null) {
            this.bloodlineState.updateCache(bloodlineRank);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void disableUntilEligible(ISkillHandler<?> skillHandler) {

        for(ISkill skill : BloodlineSkills.getBloodlineTypeSkills(this.bloodline)) {

            if(!skillHandler.isSkillEnabled(skill)) continue;

            BloodlineParentSkill<?> nodesClosestParent = closestParent(skill, skillHandler);

            if (nodesClosestParent != null && skillHandler.isSkillEnabled(skill) && nodesClosestParent.getRank() > bloodlineRank) {
                skillHandler.disableSkill(skill);
            }
        }
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    private BloodlineParentSkill<?> closestParent(ISkill skill, ISkillHandler<?> skillHandler) {

        if(skill instanceof BloodlineParentSkill<?> blSkill) {
            return blSkill;
        }

        ISkill[] parentSkills = skillHandler.getParentSkills(skill);
        for(ISkill parentSkill : parentSkills) {
            if(parentSkill instanceof BloodlineParentSkill<?> bloodlineParentSkill) {
                return bloodlineParentSkill;
            } else {
                return closestParent(parentSkill, skillHandler);
            }
        }
        return null;
    }
    @SuppressWarnings("ConstantConditions")
    @Override
    public void updateAttributes(IBloodline oldBloodline) {
        if (oldBloodline != null && oldBloodline != bloodline && !player.getCommandSenderWorld().isClientSide) {
            Map<Holder<Attribute>, AttributeModifier> oldAttributes = oldBloodline.getBloodlineAttributes(1, player, true);
            oldAttributes.forEach((attribute, modifier) -> removeModifier(player.getAttribute(attribute), modifier.id()));
        }
        if (bloodline != null && !player.getCommandSenderWorld().isClientSide) {
            Map<Holder<Attribute>, AttributeModifier> attributes = bloodline.getBloodlineAttributes(getRank(), player, false);
            attributes.forEach((attribute, modifier) -> {
                removeModifier(player.getAttribute(attribute), modifier.id());
                player.getAttribute(attribute).addPermanentModifier(modifier);
            });
        }
    }


    @Override
    public Player getEntity() {
        return this.player;
    }

    public static void removeModifier(@NotNull AttributeInstance att, @NotNull ResourceLocation location) {
        AttributeModifier m = att.getModifier(location);
        if (m != null) {
            att.removeModifier(m);
        }
    }

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
            this.bloodline = BloodlineHelper.getBloodlineById(bloodlineName);
            skillHandler.deserializeNBT(nbt);
            if(this.bloodlineState == null && this.bloodline.getNewBloodlineState(player).isPresent()) {
                this.bloodlineState = this.bloodline.getNewBloodlineState(player).get();
            }
            if(this.bloodlineState != null) {
                this.bloodlineState.deserializeNBT(provider, nbt);
            }
            onBloodlineChange(bloodline, bloodlineRank);
            updatePlayerCache();
        } else {
            this.bloodline = null;
            this.bloodlineRank = 0;
            this.bloodlineName = null;
            this.bloodlineState = null;
            skillHandler.clearSkillPoints();
            updatePlayerCache();
        }
    }
    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        if(bloodline != null) {
            nbt.putString("bloodline", bloodlineName.toString());
            nbt.putInt("rank", bloodlineRank);
            nbt = skillHandler.serializeNBT(nbt);
            if(this.bloodlineState != null) {
                nbt = bloodlineState.serializeNBT(provider, nbt);
            }
        }
        return nbt;
    }

    @Override
    public void deserializeUpdateNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        if(nbt.getString("bloodline").isEmpty()) {
            this.bloodline = null;
            this.bloodlineRank = 0;
            this.bloodlineName = null;
            this.bloodlineState = null;
            skillHandler.clearSkillPoints();
            updatePlayerCache();
        } else {
            this.bloodlineName = ResourceLocation.parse(nbt.getString("bloodline"));
            this.bloodline = BloodlineHelper.getBloodlineById(bloodlineName);
            this.bloodlineRank = nbt.getInt("rank");
            skillHandler.deserializeUpdateNBT(nbt);
            if(this.bloodlineState == null && this.bloodline.getNewBloodlineState(player).isPresent()) {
                this.bloodlineState = this.bloodline.getNewBloodlineState(player).get();
            }
            if(this.bloodlineState != null) {
                this.bloodlineState.deserializeUpdateNBT(provider, nbt);
            }
            bloodline.onBloodlineChange(player, this.bloodlineRank);
            updatePlayerCache();
        }

    }


    @Override
    public @NotNull CompoundTag serializeUpdateNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("bloodline", bloodline == null ? "" : bloodline.getBloodlineId().toString());
        nbt.putInt("rank", bloodlineRank);
        nbt = skillHandler.serializeUpdateNBT(nbt);
        if(this.bloodlineState != null) {
            nbt = bloodlineState.serializeUpdateNBT(provider, nbt);
        }
        return nbt;
    }
}
