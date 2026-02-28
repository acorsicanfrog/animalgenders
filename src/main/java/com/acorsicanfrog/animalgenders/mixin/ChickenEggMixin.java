package com.acorsicanfrog.animalgenders.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Chicken.class)
public abstract class ChickenEggMixin {

    @Redirect(method = {"aiStep", "tick"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;spawnAtLocation(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/entity/ItemEntity;"))
    private ItemEntity redirectSpawnAtLocation(Entity entity, Item item) {
        if (entity instanceof Chicken chicken) {
            // If chicken has a male gender, skip spawning the egg
            if (!chicken.level().isClientSide() && GenderAttachment.hasGender(chicken)) {
                Gender g = GenderAttachment.getGender(chicken);
                if (g == Gender.MALE) {
                    return null;
                }
            }
        }

        return entity.spawnAtLocation(item);
    }
}