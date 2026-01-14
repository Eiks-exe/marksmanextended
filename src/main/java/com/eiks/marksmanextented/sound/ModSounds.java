package com.eiks.marksmanextented.sound;


import com.eiks.marksmanextented.MarksmanExtended;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModSounds {
    private static final String MOD_ID = MarksmanExtended.MOD_ID;
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MOD_ID);

    public static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(
                name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, name))
        );
    }
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> MUSKET_HEADSHOT =
            registerSoundEvent("musket_headshot");
}
