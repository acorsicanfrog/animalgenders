package com.acorsicanfrog.animalgenders.fabric.network;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.fabric.GenderAccessor;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class GenderNetworking
{
    public static void registerServer()
    {
        PayloadTypeRegistry.playS2C().register(GenderSyncPayload.TYPE, GenderSyncPayload.STREAM_CODEC);
    }

    public static void registerClient()
    {
        ClientPlayNetworking.registerGlobalReceiver(GenderSyncPayload.TYPE, (payload, context) ->
        {
            Minecraft client = Minecraft.getInstance();
            client.execute(() ->
            {
                if (client.level == null) return;

                Entity entity = client.level.getEntity(payload.entityId());
                if (entity == null) return;

                try
                {
                    Gender gender = Gender.valueOf(payload.genderName().toUpperCase());
                    ((GenderAccessor) entity).animalgenders$setGender(gender);
                }
                catch (IllegalArgumentException ignored) {}
            });
        });
    }

    public static void sendToPlayer(ServerPlayer player, Entity entity, Gender gender)
    {
        ServerPlayNetworking.send(player, new GenderSyncPayload(entity.getId(), gender.name()));
    }

    public static void sendToTracking(Entity entity, Gender gender)
    {
        for (ServerPlayer player : PlayerLookup.tracking(entity))
        {
            sendToPlayer(player, entity, gender);
        }
    }
}
