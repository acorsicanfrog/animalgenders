package com.acorsicanfrog.animalgenders.fabric.network;

import com.acorsicanfrog.animalgenders.Constants;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record GenderSyncPayload(int entityId, String genderName) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<GenderSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "gender_sync"));

    public static final StreamCodec<FriendlyByteBuf, GenderSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, GenderSyncPayload::entityId,
                    ByteBufCodecs.STRING_UTF8, GenderSyncPayload::genderName,
                    GenderSyncPayload::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
