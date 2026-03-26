package com.acorsicanfrog.animalgenders.fabric;

import com.acorsicanfrog.animalgenders.GenderAssignment;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.fabric.network.GenderNetworking;
import com.acorsicanfrog.animalgenders.platform.Services;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;

public class AnimalGendersModFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        // Load config
        AnimalGendersConfigFabric.load();

        // Register networking (server → client payload type)
        GenderNetworking.registerServer();

        // Assign gender when an animal is loaded into a ServerLevel
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) ->
        {
            if (world.isClientSide()) return;

            if (!(entity instanceof Animal animal)) return;

            boolean hadGender = Services.PLATFORM.hasGender(animal);
            GenderAssignment.assignIfMissing(animal, null);

            // Sync to all players currently tracking this entity
            if (!hadGender && Services.PLATFORM.hasGender(animal))
            {
                GenderNetworking.sendToTracking(animal, Services.PLATFORM.getGender(animal));
            }
        });

        // Send gender data when a player starts tracking an entity
        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) ->
        {
            if (trackedEntity instanceof Animal animal)
            {
                GenderAssignment.assignIfMissing(animal, null);
            }

            if (!Services.PLATFORM.hasGender(trackedEntity)) return;

            Gender g = Services.PLATFORM.getGender(trackedEntity);
            if (g == Gender.UNKNOWN) return;

            GenderNetworking.sendToPlayer((ServerPlayer) player, trackedEntity, g);
        });
    }
}