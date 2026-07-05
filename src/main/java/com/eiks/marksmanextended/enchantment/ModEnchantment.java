package com.eiks.marksmanextended.enchantment;

import com.eiks.marksmanextended.MarksmanExtended;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import com.eiks.marksmanextended.enchantment.ModEnchantmentUtils.NameSpace;

import java.util.ArrayList;
import java.util.List;

public class ModEnchantment {

    public static final String MOD_ID = MarksmanExtended.MOD_ID;
    public static final ResourceKey<Enchantment> SHARPSHOOTER = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "sharpshooter")
    );

    public static List<Holder<Item>> sharpshooterHolderList;
    public static HolderSet<Item> sharpshooterHolderSet;
    public static void initItemLists() {
        List<NameSpace> items = new ArrayList<>();

        items.add(new NameSpace("minecraft", "bow"));
        items.add(new NameSpace("minecraft", "crossbow"));

        if(MarksmanExtended.MusketModLoaded) {
            items.add(new NameSpace("musketmod", "musket"));
            items.add(new NameSpace("musketmod", "musket_with_scope"));
        }

        sharpshooterHolderList = ModEnchantmentUtils.createItemHolderList(items);
        sharpshooterHolderSet = ModEnchantmentUtils.createHolderSet(sharpshooterHolderList);
    }




    public static void bootstrap(BootstrapContext<Enchantment> context){

        initItemLists();
        var enchantments = context.lookup(Registries.ENCHANTMENT);

        register(context, SHARPSHOOTER,
                Enchantment.enchantment(
                    Enchantment.definition(
                        sharpshooterHolderSet,
                        5,
                        5,
                        Enchantment.dynamicCost(10, 10),
                        Enchantment.dynamicCost(25, 10),
                        5,
                        EquipmentSlotGroup.MAINHAND

                    )
                )
                        .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
        );

    }

    public static void register(
            BootstrapContext<Enchantment> context,
            ResourceKey<Enchantment> key,
            Enchantment.Builder builder
    ) {
        context.register(key, builder.build(key.location()));
    }

}
