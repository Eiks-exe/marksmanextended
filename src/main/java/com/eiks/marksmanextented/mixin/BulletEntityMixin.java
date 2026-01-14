package com.eiks.marksmanextented.mixin;

import com.eiks.marksmanextented.enchantment.ModEnchantment;
import com.eiks.marksmanextented.utils.HeadShotUtils;
import ewewukek.musketmod.BulletEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(BulletEntity.class)
public abstract class BulletEntityMixin {
    @Shadow public float damage;

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void onHitEntityInject(EntityHitResult result, CallbackInfo ci) {
        BulletEntity self = (BulletEntity)(Object)this;
        Entity target = result.getEntity();
        Entity owner = self.getOwner();

        if (!(target instanceof LivingEntity)) return;
        if (owner == null || !(owner instanceof LivingEntity shooter)) return;

        boolean isHeadshot = HeadShotUtils.isHeadshot(target, self);
        if (!isHeadshot) return;
        HeadShotUtils.ClientTracker.trigger();
        if(Minecraft.getInstance().player != null ) {
             HeadShotUtils.playHeadshotSound(Minecraft.getInstance().player);
        }
        Holder<Enchantment> sharpshooter_enchantment = shooter.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantment.SHARPSHOOTER);
        int SS_level = 0;
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
        if (SS_level<=0) return;

        float multiplier = 1.0f + 0.35f * SS_level;
        try {
            Field f;
            try {
                f = BulletEntity.class.getDeclaredField("baseDamage");
            } catch(NoSuchFieldException e1) {
                f = BulletEntity.class.getDeclaredField("damage");
            }
            f.setAccessible(true);
            double current = f.getDouble(self);
            f.setDouble(self, current * multiplier);
        } catch (Throwable t) {
            try {
                DamageSource source = self.getDamageSource();
                float extra = (float)(
                        (self instanceof BulletEntity) ? ((BulletEntity)self).damage * (multiplier - 1.0f) : 0.0f
                );
                target.hurt(source, extra);

            } catch (Throwable ignored) {};
        }
    }
}
