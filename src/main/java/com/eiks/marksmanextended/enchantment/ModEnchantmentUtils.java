package com.eiks.marksmanextended.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModEnchantmentUtils {
    public static final Registry<Item> itemRegistry = BuiltInRegistries.ITEM;
    public record NameSpace(String name_space, String path) {}
    public static List<Holder<Item>> createItemHolderList(List<NameSpace> nameSpaces) {
        List<Holder<Item>> itemHolderList = new ArrayList<>();
        for (NameSpace nameSpace : nameSpaces) {
           itemHolderList.add(
                itemRegistry.getHolderOrThrow(
                        ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath(nameSpace.name_space, nameSpace.path)
                        )
                )
           );
        }
        return itemHolderList;
    }
    public static HolderSet<Item> createHolderSet(List<Holder<Item>> itemHolderList) {
        return HolderSet.direct(itemHolderList);
    }

}
