package com.eiks.marksmanextended;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public record HeadshotPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<HeadshotPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(MarksmanExtended.MOD_ID, "headshot_payload")
    );

    public static final StreamCodec<ByteBuf, HeadshotPayload> STREAM_CODEC = StreamCodec.unit(new HeadshotPayload());
    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
