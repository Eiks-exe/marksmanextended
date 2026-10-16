package com.eiks.marksmanextended.utils;

import com.eiks.marksmanextended.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import com.eiks.marksmanextended.Config;

public class HeadShotUtils {

    public static void critFeedback(Entity target) {
        if (!Config.CRIT_PARTICLES.get()) return;
        if (!(target.level() instanceof ServerLevel level)) return;

        // vanilla crit particles on the target, visible to everyone tracking it
        level.getChunkSource().broadcastAndSend(
                target, new ClientboundAnimatePacket(target, ClientboundAnimatePacket.CRITICAL_HIT));
        level.playSound(null, target.getX(), target.getEyeY(), target.getZ(),
                SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    public static boolean isHeadshot(Entity entity, Vec3 hitPos) {
        double eyeY = entity.getEyeY();
        double headHeight = Math.max(0.0, entity.getBoundingBox().maxY - eyeY);
        return hitPos.y >= eyeY - headHeight;
    }
    public static boolean isHeadshot(Entity entity, Projectile projectile){
        return isHeadshot(entity, projectile.position());
    }


    @OnlyIn(Dist.CLIENT)
    public static void onHeadshot() {
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.player instanceof Player player)) return ;
        player.playSound(ModSounds.MUSKET_HEADSHOT.get(), 20f, 1f);
        ClientTracker.trigger();
    }
    public static class ClientTracker {
        private static long lastHeadshotTime = 0;
        private static final long DISPLAY_TIME_MS = 300;

        public static void trigger(){
            if (!Config.HEADSHOT_MARKER.get()) return;
            lastHeadshotTime = System.currentTimeMillis();
        }

        public static boolean isActive() {
            return System.currentTimeMillis() - lastHeadshotTime < DISPLAY_TIME_MS;
        }

    }
}
