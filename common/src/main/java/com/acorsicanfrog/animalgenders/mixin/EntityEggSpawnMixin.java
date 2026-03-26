package com.acorsicanfrog.animalgenders.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.platform.Services;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityEggSpawnMixin {

    @Inject(method = "spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void onSpawnAtLocation(ServerLevel serverLevel, ItemLike item, CallbackInfoReturnable<ItemEntity> cir) 
    {
        Entity self = (Entity) (Object) this;

        if (item.asItem() != Items.EGG)
            return;

        if (!(self instanceof Animal animal))
            return;

        if (animal.level().isClientSide())
            return;

        if (!Services.PLATFORM.hasGender(animal))
            return;

        Gender g = Services.PLATFORM.getGender(animal);

        if (g == Gender.MALE) 
        {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}