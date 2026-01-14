package com.eiks.marksmanextented.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.eiks.marksmanextented.utils.HeadShotUtils;
import com.eiks.marksmanextented.enchantment.ModEnchantment;

import java.lang.reflect.Field;

@Mixin(AbstractArrow.class)
public abstract class ArrowEntityMixin {
    // Intercept the method that handles hitting an entity. Method name depends on mappings.
    // In Yarn it's usually onHitEntity(EntityHitResult).
    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void onHitEntityInject(EntityHitResult result, CallbackInfo ci) {
        // Defensive: the cast should be valid because mixin target is AbstractArrow.
        AbstractArrow self = (AbstractArrow)(Object)this;
        Entity target = result.getEntity();
        Entity owner = self.getOwner();

        if (!(target instanceof LivingEntity)) return;
        if (owner == null || !(owner instanceof LivingEntity)) return;

        // compute start and end ray similar to musket logic. Use previous position and current.
        Vec3 start = new Vec3(self.xOld, self.yOld, self.zOld);
        Vec3 end = new Vec3(self.getX(), self.getY(), self.getZ());
        boolean headshot = HeadShotUtils.isHeadshot(target, self);
        if (!headshot) return;
        // get enchant level from shooter's held item(s)
        LivingEntity shooter = (LivingEntity)owner;
        Holder<Enchantment> sharpshooter_enchant = shooter.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantment.SHARPSHOOTER);
        int level = 0;
        // try main hand
        ItemStack main = shooter.getMainHandItem();
        level = EnchantmentHelper.getItemEnchantmentLevel(
                sharpshooter_enchant,
                main);
        if (level == 0) {
            ItemStack off = shooter.getOffhandItem();
            level = EnchantmentHelper.getItemEnchantmentLevel(sharpshooter_enchant, off);
        }
        if (level <= 0) return;

        // Apply multiplier: choose formula (example: +15% damage per level)
        float multiplier = 1.0f + 0.5f * level;

        // Now we need to increase the damage that will be applied.
        // AbstractArrow typically calls target.hurt(DamageSource.arrow(this, owner), damage)
        // We'll apply a small trick: scale the arrow's base damage field if present.
        // Many mappings expose setBaseDamage/getBaseDamage or a "damage" field.
        HeadShotUtils.ClientTracker.trigger();
        if (Minecraft.getInstance().player != null) {
            HeadShotUtils.playHeadshotSound(Minecraft.getInstance().player);
        }
        try {
            // try setter via reflection: look for field "baseDamage" or "damage"
            Field f;
            try {
                f = AbstractArrow.class.getDeclaredField("baseDamage");
            } catch (NoSuchFieldException e1) {
                f = AbstractArrow.class.getDeclaredField("damage");
            }
            f.setAccessible(true);
            double current = f.getDouble(self);
            f.setDouble(self, current * multiplier);
        } catch (Throwable t) {
            // If we can't mutate arrow internal damage, as fallback immediately deal extra damage
            try {
                float extra = (float)(
                        (self instanceof AbstractArrow) ? ((AbstractArrow)self).getBaseDamage() * (multiplier - 1.0f) : 0.0f
                );
                // Directly apply extra damage using same damage source
                target.hurt(target.level().damageSources().arrow(self, owner), extra);

            } catch (Throwable ignored) {}
        }
    }


}
