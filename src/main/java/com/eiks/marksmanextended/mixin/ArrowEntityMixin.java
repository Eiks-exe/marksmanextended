package com.eiks.marksmanextended.mixin;

import com.eiks.marksmanextended.HeadshotPayload;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.eiks.marksmanextended.utils.HeadShotUtils;
import com.eiks.marksmanextended.enchantment.ModEnchantment;


@Mixin(AbstractArrow.class)
public abstract class ArrowEntityMixin {
    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void onHitEntityInject(EntityHitResult result, CallbackInfo ci) {
        // Defensive: the cast should be valid because mixin target is AbstractArrow.
        AbstractArrow self = (AbstractArrow)(Object)this;
        Entity target = result.getEntity();
        Entity owner = self.getOwner();

        if (!(target instanceof LivingEntity)) return;
        if (!(owner instanceof LivingEntity shooter)) return;
        boolean headshot = HeadShotUtils.isHeadshot(target, self);
        if (!headshot) return;

        if (owner instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new HeadshotPayload()
            );
        }
        Holder<Enchantment> sharpshooter_enchant = shooter.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantment.SHARPSHOOTER);
        int level;
        // try main hand
        ItemStack main = shooter.getMainHandItem();
        level = EnchantmentHelper.getTagEnchantmentLevel(
                sharpshooter_enchant,
                main);
        if (level == 0) {
            ItemStack off = shooter.getOffhandItem();
            level = EnchantmentHelper.getTagEnchantmentLevel(sharpshooter_enchant, off);
        }
        if (level <= 0) return;

        // Apply multiplier: choose formula (example: +15% damage per level)
        float multiplier = 1.0f + 0.5f * level;

        self.setBaseDamage(self.getBaseDamage() * multiplier);

    }


}
