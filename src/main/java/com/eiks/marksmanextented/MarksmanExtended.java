package com.eiks.marksmanextented;

import com.eiks.marksmanextented.sound.ModSounds;
import com.eiks.marksmanextented.utils.HeadShotUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(MarksmanExtended.MOD_ID)
public class MarksmanExtended {
    public static final String MOD_ID = "marksmanextended";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation HEADSHOT_SPRITE =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/headshot/headshot_2.png");

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MarksmanExtended(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModSounds.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("MarksManExtended Loaded on Server");
    }
    @SubscribeEvent
    public void guiIndicator(RenderGuiEvent.Post event){
        if (!Config.HEADSHOT_VISUAL_INDICATOR.getAsBoolean()) return;
        if (!HeadShotUtils.ClientTracker.isActive()) return;
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        float scale = mc.getWindow().getGuiScaledHeight() / 360f;
        int size = Math.max(16, Math.round(32 * scale));;
        int cx = (mc.getWindow().getGuiScaledWidth() - size) / 2;
        int cy = (mc.getWindow().getGuiScaledHeight() - size) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, HEADSHOT_SPRITE);
        // Draw the texture from top-left corner (0,0) of the PNG
        guiGraphics.blit(HEADSHOT_SPRITE, cx, cy, 0, 0, size, size, size, size);

    }
}
