package com.eiks.marksmanextended.mixin;

import com.eiks.marksmanextended.HeadshotPayload;
import com.eiks.marksmanextended.enchantment.ModEnchantment;
import com.eiks.marksmanextended.utils.HeadShotUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "ewewukek.musketmod.BulletEntity")
public abstract class BulletEntityMixin {

    @Shadow public float damage;


    @Unique
    private float marksman$originalDamage = -1.0f;

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void marksman$onHitEntity(EntityHitResult result, CallbackInfo ci) {
        marksman$originalDamage = 1.0f;

        Projectile projectile = (Projectile) (Object) this;
        if (projectile.level().isClientSide) return;

        Entity target = result.getEntity();
        if (!(target instanceof LivingEntity)) return;
        if (!(projectile.getOwner() instanceof LivingEntity shooter)) return;

        if (!HeadShotUtils.isHeadshot(target, projectile)) return;

        float multiplier = 1.25f + 0.35f * marksman$sharpshooterLevel(shooter);

        marksman$originalDamage = this.damage;
        this.damage = this.damage * multiplier;
        HeadShotUtils.critFeedback(target);
        if (shooter instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new HeadshotPayload());
        }
    }

    @Inject(method = "onHitEntity", at = @At("RETURN"))
    private void marksman$reset(EntityHitResult result, CallbackInfo ci) {
        if (marksman$originalDamage >= 0.0f) {
            this.damage = marksman$originalDamage;
            marksman$originalDamage = -1.0f;
        }
    }

    @Unique
    private static int marksman$sharpshooterLevel(LivingEntity shooter) {
        Holder<Enchantment> enchantment = shooter.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantment.SHARPSHOOTER);
        int level = EnchantmentHelper.getTagEnchantmentLevel(enchantment, shooter.getMainHandItem());
        if (level == 0) {
            level = EnchantmentHelper.getTagEnchantmentLevel(enchantment, shooter.getOffhandItem());
        }
        return level;
    }
}