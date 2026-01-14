package com.eiks.marksmanextented.datagen;

import com.eiks.marksmanextented.MarksmanExtended;
import com.eiks.marksmanextented.sound.ModSounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundDefinitionProvider extends SoundDefinitionsProvider {

    public ModSoundDefinitionProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, MarksmanExtended.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {

        add( ModSounds.MUSKET_HEADSHOT, SoundDefinition.definition()
                .with(
                        sound(MarksmanExtended.MOD_ID + ":musket_headshot_2", SoundDefinition.SoundType.SOUND)
                                .volume(5.0f)
                                .pitch(1.0f)
                                .weight(2)
                                .attenuationDistance(8)
                                .stream(false)
                                .preload(true)
                )
                .subtitle("sound.marksmanexetended.musket_headshot")
                .replace(true)
        );
    }
}
