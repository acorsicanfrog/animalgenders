package com.acorsicanfrog.animalgenders.fabric;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.fabric.network.GenderNetworking;
import com.acorsicanfrog.animalgenders.platform.Services;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;

import java.util.List;

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

            String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(animal.getType()).toString();

            List<String> blacklist = Services.PLATFORM.getBlacklist();
            if (blacklist.contains(entityId)) return;

            List<String> whitelist = Services.PLATFORM.getWhitelist();
            if (!whitelist.isEmpty() && !whitelist.contains(entityId)) return;

            if (Services.PLATFORM.hasGender(animal)) return;

            double femaleChance = animal.isBaby() ? 0.5 : Services.PLATFORM.getFemaleChance();
            Gender gender = animal.getRandom().nextDouble() < femaleChance ? Gender.FEMALE : Gender.MALE;

            Services.PLATFORM.setGender(animal, gender);

            // Sync to all players currently tracking this entity
            GenderNetworking.sendToTracking(animal, gender);
        });

        // Send gender data when a player starts tracking an entity
        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) ->
        {
            if (!Services.PLATFORM.hasGender(trackedEntity)) return;

            Gender g = Services.PLATFORM.getGender(trackedEntity);
            if (g == Gender.UNKNOWN) return;

            GenderNetworking.sendToPlayer((ServerPlayer) player, trackedEntity, g);
        });
    }
}