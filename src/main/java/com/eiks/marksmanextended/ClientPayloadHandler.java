package com.eiks.marksmanextended;

import com.eiks.marksmanextended.utils.HeadShotUtils;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    //@Todo method referencing
    public static void handleHeadshot(final HeadshotPayload payload, final IPayloadContext context){
        context.enqueueWork(()->{
            HeadShotUtils.onHeadshot();
        });
    }
}
