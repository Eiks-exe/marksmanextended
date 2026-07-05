package com.eiks.marksmanextended.mixin;

import com.eiks.marksmanextended.HeadshotPayload;
import com.eiks.marksmanextended.enchantment.ModEnchantment;
import com.eiks.marksmanextended.utils.HeadShotUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "ewewukek.musketmod.BulletEntity")
public abstract class BulletEntityMixin {

    @Shadow public float damage;

    @Shadow public boolean headshot;

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void onHitEntityInject(EntityHitResult result, CallbackInfo ci) {
        Object self = this;
        Object bullet = this;
        Entity target = result.getEntity();
        Entity owner;
        try {
            owner = (Entity) self.getClass().getMethod("getOwner").invoke(self);
        } catch (Exception e) {
            return;
        }

        if (!(target instanceof LivingEntity)) return;
        if (!(owner instanceof LivingEntity shooter)) return;


        Holder<Enchantment> sharpshooter_enchantment = shooter.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantment.SHARPSHOOTER);

        int SS_level;
        ItemStack main = shooter.getMainHandItem();
        SS_level = EnchantmentHelper.getTagEnchantmentLevel(
                sharpshooter_enchantment,
                main
        );

        if (SS_level == 0) {
            ItemStack off = shooter.getOffhandItem();
            SS_level = EnchantmentHelper.getTagEnchantmentLevel(
                    sharpshooter_enchantment,
                    off
            );
        }

        if (!(bullet instanceof Projectile projectile)){
            return;
        }
        boolean isHeadshot = HeadShotUtils.isHeadshot(target, projectile);
        if (!isHeadshot) return;
        if (owner instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new HeadshotPayload()
            );
        }
        float base_headshot_dmg = 1.25f;
        float multiplier = base_headshot_dmg;
        if (SS_level > 0) {
            multiplier += 0.35f * SS_level;
        }
        try {
            DamageSource source =(DamageSource)
                    self.getClass().getMethod("getDamageSource").invoke(self);
            float extra = 0.0f;
            target.hurt(source, this.damage * multiplier - this.damage);
            ci.cancel();
        } catch (Throwable ignored) {}
    }
}

