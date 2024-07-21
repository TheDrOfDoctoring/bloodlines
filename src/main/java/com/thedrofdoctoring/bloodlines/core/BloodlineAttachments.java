package com.thedrofdoctoring.bloodlines.core;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BloodlineAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Bloodlines.MODID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BloodlineManager>> BLOODLINE_MANAGER = ATTACHMENT_TYPES.register(BloodlineManager.BlOODLINE_KEY.getPath(), () -> AttachmentType.builder(new BloodlineManager.Factory()).serialize(new BloodlineManager.Serializer()).copyOnDeath().build());
}
