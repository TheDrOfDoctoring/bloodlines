package com.thedrofdoctoring.bloodlines.capabilities.other;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.function.Function;

public class MistFormAttachment {

    public static class Factory implements Function<IAttachmentHolder, ShulkerBullet> {

        @Override
        public ShulkerBullet apply(IAttachmentHolder holder) {
            if (holder instanceof Entity entity) {
                var bullet = EntityType.SHULKER_BULLET.create(entity.getCommandSenderWorld());
                if (bullet != null) {
                    return bullet;
                }
                throw new IllegalArgumentException("Cannot create mist form attachment for holder " + holder.getClass() + ". Shulker Bullet entity could not be created");
            }
            throw new IllegalArgumentException("Cannot create mist form attachment for holder " + holder.getClass() + ". Expected Entity");
        }
    }
}
