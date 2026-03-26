package com.acorsicanfrog.animalgenders.mixin;

import com.acorsicanfrog.animalgenders.GenderAssignment;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.ServerLevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobGenderSpawnMixin
{
    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void animalgenders$assignGenderOnFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir)
    {
        Mob self = (Mob) (Object) this;

        if (!(self instanceof Animal animal))
        {
            return;
        }

        if (level.isClientSide())
        {
            return;
        }

        GenderAssignment.assignIfMissing(animal, spawnType);
    }
}