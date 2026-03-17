package com.acorsicanfrog.animalgenders.fabric.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.fabric.GenderAccessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class GenderDataMixin implements GenderAccessor
{
    @Unique
    private Gender animalgenders$gender = Gender.UNKNOWN;

    @Unique
    private boolean animalgenders$hasGender = false;

    @Override
    public Gender animalgenders$getGender()
    {
        return animalgenders$gender;
    }

    @Override
    public void animalgenders$setGender(Gender gender)
    {
        animalgenders$gender = gender;
        animalgenders$hasGender = true;
    }

    @Override
    public boolean animalgenders$hasGender()
    {
        return animalgenders$hasGender;
    }

    @Inject(method = "saveWithoutId", at = @At("RETURN"))
    private void onSaveWithoutId(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir)
    {
        if (animalgenders$hasGender)
        {
            tag.putString("animalgenders:gender", animalgenders$gender.name());
        }
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void onLoad(CompoundTag tag, CallbackInfo ci)
    {
        if (tag.contains("animalgenders:gender"))
        {
            try
            {
                animalgenders$gender = Gender.valueOf(tag.getString("animalgenders:gender").toUpperCase());
                animalgenders$hasGender = true;
            }
            catch (IllegalArgumentException e)
            {
                animalgenders$gender = Gender.UNKNOWN;
                animalgenders$hasGender = false;
            }
        }
    }
}
