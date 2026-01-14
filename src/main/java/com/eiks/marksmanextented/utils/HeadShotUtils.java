package com.eiks.marksmanextented.utils;

import com.eiks.marksmanextented.sound.ModSounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HeadShotUtils {
    public static boolean isHeadshot(Entity entity, Projectile projectile){
        AABB boundingbox = entity.getBoundingBox();
        Vec3 eyesPos = entity.getEyePosition();
        double headTopY = boundingbox.maxY;
        double r = headTopY - eyesPos.y;
        Vec3 hitVec = projectile.position();
        return (hitVec.y >= (eyesPos.y - r)) && projectile.getOwner()!= null ;
    };


    public static void playHeadshotSound(Player player) {
        player.playSound(ModSounds.MUSKET_HEADSHOT.get(), 10.0f, 1.0f);
    }

    public class ClientTracker {
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
