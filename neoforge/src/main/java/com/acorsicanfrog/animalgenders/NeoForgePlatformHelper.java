package com.acorsicanfrog.animalgenders;

import java.util.ArrayList;
import java.util.List;

import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;
import com.acorsicanfrog.animalgenders.platform.IPlatformHelper;

import net.minecraft.world.entity.Entity;
import net.neoforged.fml.ModList;

public class NeoForgePlatformHelper implements IPlatformHelper
{
    @Override
    public Gender getGender(Entity entity)
    {
        return GenderAttachment.getGender(entity);
    }

    @Override
    public void setGender(Entity entity, Gender gender)
    {
        GenderAttachment.setGender(entity, gender);
    }

    @Override
    public boolean hasGender(Entity entity)
    {
        return GenderAttachment.hasGender(entity);
    }

    @Override
    public boolean isModLoaded(String modId)
    {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public List<String> getBlacklist()
    {
        return new ArrayList<>(AnimalGendersConfig.GENDER_BLACKLIST.get());
    }

    @Override
    public List<String> getWhitelist()
    {
        return new ArrayList<>(AnimalGendersConfig.GENDER_WHITELIST.get());
    }

    @Override
    public double getFemaleChance()
    {
        Double val = AnimalGendersConfig.FEMALE_CHANCE.get();
        return val != null ? val : 0.8;
    }
}
