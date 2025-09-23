package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.entity.BloodlineMobManager;
import com.thedrofdoctoring.bloodlines.capabilities.other.MistFormAttachment;
import com.thedrofdoctoring.bloodlines.capabilities.other.VampExtendedCreature;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BloodlinesAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Bloodlines.MODID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BloodlineManager>> BLOODLINE_MANAGER = ATTACHMENT_TYPES.register(BloodlineManager.BlOODLINE_KEY.getPath(), () -> AttachmentType.builder(new BloodlineManager.Factory()).serialize(new BloodlineManager.Serializer()).copyOnDeath().build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BloodlineMobManager>> BLOODLINE_MOB_MANAGER = ATTACHMENT_TYPES.register(BloodlineMobManager.BlOODLINE_KEY.getPath(), () -> AttachmentType.builder(new BloodlineMobManager.Factory()).serialize(new BloodlineMobManager.Serializer()).copyOnDeath().build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ExtendedCreature>> VAMP_EXTENDED_CREATURE = ATTACHMENT_TYPES.register(VampExtendedCreature.VAMP_CREATURE_KEY.getPath(), () -> AttachmentType.builder(new VampExtendedCreature.Factory()).serialize(new VampExtendedCreature.Serializer()).copyOnDeath().build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ShulkerBullet>> MIST_FORM = ATTACHMENT_TYPES.register(Bloodlines.rl("mistform").getPath(), () -> AttachmentType.builder(new MistFormAttachment.Factory()).build());

}
