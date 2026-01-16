package com.eiks.marksmanextended.utils;

import com.eiks.marksmanextended.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class HeadShotUtils {
    public static boolean isHeadshot(Entity entity, Projectile projectile){
        AABB boundingBox = entity.getBoundingBox();
        Vec3 eyesPos = entity.getEyePosition();
        double headTopY = boundingBox.maxY;
        double r = headTopY - eyesPos.y;
        Vec3 hitVec = projectile.position();
        return (hitVec.y >= (eyesPos.y - r)) && projectile.getOwner()!= null ;
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

            lastHeadshotTime = System.currentTimeMillis();
        }

        public static boolean isActive() {
            return System.currentTimeMillis() - lastHeadshotTime < DISPLAY_TIME_MS;
        }

    }
}
