package com.eiks.marksmanextended.mixin;

import com.eiks.marksmanextended.Config;
import com.eiks.marksmanextended.HeadshotPayload;
import com.eiks.marksmanextended.utils.HeadShotUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "io.redspace.irons_artifice.entity.Bullet")
public abstract class AnaBulletEntityMixin {

    @Unique
    private static final float MARKSMAN$HEADSHOT_MULTIPLIER = 1.25f;

    @Unique
    private float marksman$headshotMultiplier = 1.0f;

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void marksman$onHitEntity(EntityHitResult result, CallbackInfo ci) {
        marksman$headshotMultiplier = 1.0f;

        Projectile projectile = (Projectile) (Object) this;
        if (projectile.level().isClientSide) return;

        Entity target = result.getEntity();
        if (!(target instanceof LivingEntity)) return;
        if (!(projectile.getOwner() instanceof LivingEntity shooter)) return;

        if (!HeadShotUtils.isHeadshot(target, result.getLocation())) return;

        boolean bonusEnabled = Config.ARTIFICE_SERVER_BONUS.get();
        marksman$headshotMultiplier = bonusEnabled
                ? MARKSMAN$HEADSHOT_MULTIPLIER
                : 1.0f;

        HeadShotUtils.critFeedback(target);
        if (shooter instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new HeadshotPayload());
        }
    }

    @ModifyReturnValue(method = "resolveDamage", at = @At("RETURN"))
    private float marksman$boostDamage(float original) {
        return original * marksman$headshotMultiplier;
    }

    @Inject(method = "onHitEntity", at = @At("RETURN"))
    private void marksman$reset(EntityHitResult result, CallbackInfo ci) {
        marksman$headshotMultiplier = 1.0f;
    }
}