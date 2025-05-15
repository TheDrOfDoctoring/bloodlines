package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.entity.player.vampire.DefaultVampireAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.config.VampirismConfig;
import de.teamlapen.vampirism.core.ModRefinements;
import de.teamlapen.vampirism.core.ModSounds;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class NobleFlankAction extends DefaultVampireAction {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean activate(@NotNull IVampirePlayer vampire, ActivationContext context) {
        Player player = vampire.asEntity();
        int dist = CommonConfig.nobleFlankRange.get();
        if (vampire.getSkillHandler().isRefinementEquipped(ModRefinements.TELEPORT_DISTANCE.get())) {
            dist *= VampirismConfig.BALANCE.vrTeleportDistanceMod.get();
        }


        Vec3 eyePos = player.getEyePosition();
        Vec3 view = player.getViewVector(1.0F).scale(dist);
        Vec3 viewPos = eyePos.add(view);
        AABB aabb = player.getBoundingBox().expandTowards(view).inflate(1.0);
        EntityHitResult result = ProjectileUtil.getEntityHitResult(player, eyePos, viewPos, aabb, p -> p instanceof LivingEntity, dist);
        double ox = player.getX();
        double oy = player.getY();
        double oz = player.getZ();
        if (result == null) {
            player.playSound(SoundEvents.NOTE_BLOCK_BASS.value(), 1, 1);
            return false;
        }
        Vec3 pos = null;
        LivingEntity resultEntity = (LivingEntity) result.getEntity();

        if(player.isInvisible() || !UtilLib.canReallySee(resultEntity, player, false)) {
            Vec3 lookAngle = resultEntity.getViewVector(1);
            Vec3 behindPos = lookAngle.reverse().multiply(1.2f, 0, 1.2f);
            pos = resultEntity.position().add(behindPos).add(0, 0.2f, 0);

        }

        if (pos != null) {
            player.setPos(pos);
        }



        if (pos == null) {
            player.setPos(ox, oy, oz);
            player.playSound(SoundEvents.NOTE_BLOCK_BASEDRUM.value(), 1, 1);
            return false;
        }
        if (player instanceof ServerPlayer playerMp) {
            playerMp.disconnect();
            playerMp.teleportTo(pos.x, pos.y, pos.z);
        }
        player.getCommandSenderWorld().playSound(null, ox,oy,oz, ModSounds.TELEPORT_AWAY.get(), SoundSource.PLAYERS, 1f, 1f);
        player.getCommandSenderWorld().playSound(null, player.getX(), player.getY(), player.getZ(),ModSounds.TELEPORT_HERE.get(), SoundSource.PLAYERS, 1f, 1f);
        return true;
    }

    @Override
    public int getCooldown(IVampirePlayer iVampirePlayer) {
        return CommonConfig.nobleFlankCooldown.get() * 20;
    }

    @Override
    public boolean canBeUsedBy(IVampirePlayer vampire) {
        return !vampire.getActionHandler().isActionActive(VampireActions.BAT.get());

    }
    public boolean showHudCooldown(Player player) {
        return true;
    }


}
