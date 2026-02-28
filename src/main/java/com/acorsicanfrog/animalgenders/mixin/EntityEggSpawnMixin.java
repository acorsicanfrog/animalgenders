package com.acorsicanfrog.animalgenders.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityEggSpawnMixin {

    @Inject(method = "spawnAtLocation(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void onSpawnAtLocation(Item item, CallbackInfoReturnable<ItemEntity> cir) 
		{
        Entity self = (Entity) (Object) this;

        // Only care about vanilla eggs
        if (item != Items.EGG)
            return;

        if (!(self instanceof Animal animal))
            return;

        // Server-side only and only when gender is known
        if (animal.level().isClientSide())
            return;

        if (!GenderAttachment.hasGender(animal))
            return;

        Gender g = GenderAttachment.getGender(animal);

        if (g == Gender.MALE) 
					{
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}