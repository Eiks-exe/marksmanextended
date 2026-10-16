package com.eiks.marksmanextended.datagen;

import com.eiks.marksmanextended.MarksmanExtended;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, MarksmanExtended.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        add("enchantment.marksmanextended.sharpshooter", "Sharpshooter");
        add("marksmanextended.configuration.title", "Marksman Extended");

        add("marksmanextended.configuration.irons_artifice","Arms 'n Artifice");
        add("marksmanextended.configuration.headshotDamageBonus", "Artifice headshot bonus");
        add("marksmanextended.configuration.headshotDamageBonus.tooltip",
                "Apply the headshot damage multiplier to Iron's Arms 'n Artifice guns.");

        add("marksmanextended.configuration.critParticles", "Crit particles");
        add("marksmanextended.configuration.critParticles.tooltip",
                "Show crit particles on the target when a headshot lands.");

        add("marksmanextended.configuration.showHeadshotMarker", "Headshot marker");
        add("marksmanextended.configuration.showHeadshotMarker.tooltip",
                "Show the crosshair marker when you land a headshot.");
    }
}