package com.acorsicanfrog.animalgenders.mixin;

import java.util.List;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.platform.Services;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.ServerLevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class AnimalSpawnReasonMixin
{
    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void onFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                                 SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir)
    {
        Mob self = (Mob) (Object) this;

        if (!(self instanceof Animal animal))
            return;

        if (spawnReason != EntitySpawnReason.SPAWN_ITEM_USE && spawnReason != EntitySpawnReason.DISPENSER)
            return;

        String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(animal.getType()).toString();

        List<String> blacklist = Services.PLATFORM.getBlacklist();
        if (blacklist.contains(entityId))
            return;

        List<String> whitelist = Services.PLATFORM.getWhitelist();
        if (!whitelist.isEmpty() && !whitelist.contains(entityId))
            return;

        if (Services.PLATFORM.hasGender(animal))
            return;

        Gender gender = animal.getRandom().nextBoolean() ? Gender.FEMALE : Gender.MALE;
        Services.PLATFORM.setGender(animal, gender);
    }
}