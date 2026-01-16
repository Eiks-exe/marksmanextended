package com.eiks.marksmanextended;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // a list of strings that are treated as resource locations for items
    public static final ModConfigSpec.BooleanValue HEADSHOT_VISUAL_INDICATOR = BUILDER
            .comment("headshot visual indicator")
            .define("headshot_visual_indicator", true);
    static final ModConfigSpec SPEC = BUILDER.build();

}
