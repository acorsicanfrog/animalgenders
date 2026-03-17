package com.acorsicanfrog.animalgenders.fabric;

import com.acorsicanfrog.animalgenders.Gender;
import net.minecraft.world.entity.Entity;

/**
 * Duck interface injected into Entity via GenderDataMixin.
 * Used by the Fabric platform to store/retrieve gender without a third-party API.
 */
public interface GenderAccessor
{
    Gender animalgenders$getGender();
    void animalgenders$setGender(Gender gender);
    boolean animalgenders$hasGender();
}
