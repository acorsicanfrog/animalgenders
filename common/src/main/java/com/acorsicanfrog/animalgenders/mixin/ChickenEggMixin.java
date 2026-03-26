package com.acorsicanfrog.animalgenders.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.platform.Services;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.level.ItemLike;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chicken.class)
public abstract class ChickenEggMixin 
{
    @Shadow public int eggTime;

    // Prevent male chickens from actually dropping an egg item
    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemEntity redirectSpawnAtLocation(Entity entity, ItemLike item) 
    {
        if (entity instanceof Chicken chicken) 
        {
            if (!chicken.level().isClientSide() && Services.PLATFORM.hasGender(chicken)) 
            {
                Gender g = Services.PLATFORM.getGender(chicken);

                if (g == Gender.MALE)
                    return null;
            }
        }

        if (entity.level() instanceof ServerLevel serverLevel)
            return entity.spawnAtLocation(serverLevel, item);

        return null;
    }

    // Reset the egg timer for male chickens before it reaches zero,
    // preventing sounds & game-events from firing
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void onAiStepResetEggTimer(CallbackInfo ci)
    {
        Chicken self = (Chicken) (Object) this;

        if (self.level().isClientSide())
            return;

        if (!Services.PLATFORM.hasGender(self))
            return;

        Gender g = Services.PLATFORM.getGender(self);

        if (g == Gender.MALE && eggTime <= 1)
            eggTime = self.getRandom().nextInt(6000) + 6000;
    }
}