package com.acorsicanfrog.animalgenders;

import java.util.List;

import com.acorsicanfrog.animalgenders.platform.Services;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;

public final class GenderAssignment
{
    private GenderAssignment()
    {
    }

    public static void assignIfMissing(Animal animal, MobSpawnType spawnType)
    {
        if (!shouldAssignGender(animal))
        {
            return;
        }

        double femaleChance = getFemaleChance(animal, spawnType);
        Gender gender = animal.getRandom().nextDouble() < femaleChance ? Gender.FEMALE : Gender.MALE;
        Services.PLATFORM.setGender(animal, gender);
    }

    public static boolean shouldAssignGender(Animal animal)
    {
        String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(animal.getType()).toString();

        List<String> blacklist = Services.PLATFORM.getBlacklist();
        if (blacklist.contains(entityId))
        {
            return false;
        }

        List<String> whitelist = Services.PLATFORM.getWhitelist();
        if (!whitelist.isEmpty() && !whitelist.contains(entityId))
        {
            return false;
        }

        return !Services.PLATFORM.hasGender(animal);
    }

    private static double getFemaleChance(Animal animal, MobSpawnType spawnType)
    {
        if (spawnType == MobSpawnType.SPAWN_EGG || spawnType == MobSpawnType.DISPENSER || spawnType == MobSpawnType.BREEDING)
        {
            return 0.5;
        }

        if (animal.isBaby())
        {
            return 0.5;
        }

        return Services.PLATFORM.getFemaleChance();
    }
}