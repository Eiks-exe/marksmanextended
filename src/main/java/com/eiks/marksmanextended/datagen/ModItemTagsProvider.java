package com.eiks.marksmanextended.datagen;

import com.eiks.marksmanextended.MarksmanExtended;
import com.eiks.marksmanextended.enchantment.ModEnchantment;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.intellij.lang.annotations.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags){
        super(output, lookupProvider, blockTags);

    }
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        this.tag(ModEnchantment.SHARPSHOOTER_COMPATIBLE)
                .add(Items.BOW)
                .add(Items.CROSSBOW)
                .addOptional(ResourceLocation.fromNamespaceAndPath("musketmod", "musket"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("musketmod", "musket_with_scope"));
    }
}
