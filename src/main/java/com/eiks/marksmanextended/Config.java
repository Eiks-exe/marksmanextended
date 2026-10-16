package com.eiks.marksmanextended;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    public static final ModConfigSpec SERVER_SPEC;
    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec CLIENT_SPEC;


    public static final ModConfigSpec.BooleanValue ARTIFICE_SERVER_BONUS;

    public static final ModConfigSpec.BooleanValue CRIT_PARTICLES;
    public static final ModConfigSpec.BooleanValue HEADSHOT_MARKER;

    static {
        ModConfigSpec.Builder server = new ModConfigSpec.Builder();
        server.push("irons_artifice");
        ARTIFICE_SERVER_BONUS = server
                .comment("Apply the headshot damage multiplier to Iron's Arms 'n Artifice guns")
                .translation("marksmanextended.configuration.headshotDamageBonus")
                .define("headshotDamageBonus", true);
        server.pop();
        SERVER_SPEC = server.build();

        ModConfigSpec.Builder common = new ModConfigSpec.Builder();
        common.push("irons_artifice");
        common.translation("marksman.extended.configuration.irons_artifice");
        CRIT_PARTICLES = common
                .comment("Show vanilla crit particles on the target when a headshot lands")
                .translation("marksmanextended.configuration.critParticles")
                .define("critParticles", true);
        common.pop();
        COMMON_SPEC = common.build();

        ModConfigSpec.Builder client = new ModConfigSpec.Builder();
        HEADSHOT_MARKER = client
                .comment("Show the crosshair marker when you land a headshot")
                .translation("marksmanextended.configuration.showHeadshotMarker")
                .define("showHeadshotMarker", true);
        CLIENT_SPEC = client.build();
    }

}
