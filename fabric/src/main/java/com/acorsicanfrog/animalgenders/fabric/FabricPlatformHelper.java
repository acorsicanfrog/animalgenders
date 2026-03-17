package com.acorsicanfrog.animalgenders.fabric;

import java.util.List;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.platform.IPlatformHelper;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.Entity;

public class FabricPlatformHelper implements IPlatformHelper
{
    @Override
    public Gender getGender(Entity entity)
    {
        return ((GenderAccessor) entity).animalgenders$getGender();
    }

    @Override
    public void setGender(Entity entity, Gender gender)
    {
        ((GenderAccessor) entity).animalgenders$setGender(gender);
    }

    @Override
    public boolean hasGender(Entity entity)
    {
        return ((GenderAccessor) entity).animalgenders$hasGender();
    }

    @Override
    public boolean isModLoaded(String modId)
    {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public List<String> getBlacklist()
    {
        return AnimalGendersConfigFabric.get().getBlacklist();
    }

    @Override
    public List<String> getWhitelist()
    {
        return AnimalGendersConfigFabric.get().getWhitelist();
    }

    @Override
    public double getFemaleChance()
    {
        return AnimalGendersConfigFabric.get().getFemaleChance();
    }
}
