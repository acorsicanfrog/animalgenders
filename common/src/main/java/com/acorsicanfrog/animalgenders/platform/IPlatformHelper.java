package com.acorsicanfrog.animalgenders.platform;

import java.util.List;

import com.acorsicanfrog.animalgenders.Gender;

import net.minecraft.world.entity.Entity;

public interface IPlatformHelper
{
    Gender getGender(Entity entity);
    void setGender(Entity entity, Gender gender);
    boolean hasGender(Entity entity);
    boolean isModLoaded(String modId);
    List<String> getBlacklist();
    List<String> getWhitelist();
    double getFemaleChance();
}