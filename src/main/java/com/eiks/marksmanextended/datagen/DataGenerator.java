package com.eiks.marksmanextended.datagen;

import com.eiks.marksmanextended.MarksmanExtended;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MarksmanExtended.MOD_ID)
public class DataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        var blockTagsProvider = new BlockTagsProvider(packOutput, lookupProvider,MarksmanExtended.MOD_ID ,helper) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {}
        };

        generator.addProvider(event.includeServer(), new DataPackProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModSoundDefinitionProvider(packOutput, helper));
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter())
        );

    }
}
