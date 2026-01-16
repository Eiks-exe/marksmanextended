package com.eiks.marksmanextended;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(MarksmanExtended.MOD_ID)
public final class ModNetworking {
    @EventBusSubscriber(modid = MarksmanExtended.MOD_ID)
    public class ClientNetworkHandler {
        @SubscribeEvent
        public static void register(final RegisterPayloadHandlersEvent event){
            var register = event.registrar("1");

            register.playToClient(
                    HeadshotPayload.TYPE,
                    HeadshotPayload.STREAM_CODEC,
                    ClientPayloadHandler::handleHeadshot
            );
        }
    }


}
