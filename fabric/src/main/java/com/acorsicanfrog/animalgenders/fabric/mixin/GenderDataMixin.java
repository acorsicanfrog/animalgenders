package com.acorsicanfrog.animalgenders.fabric.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.fabric.GenderAccessor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    private void onSaveWithoutId(ValueOutput output, CallbackInfo ci)
    {
        if (animalgenders$hasGender)
        {
            output.putString("animalgenders:gender", animalgenders$gender.name());
        }
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void onLoad(ValueInput input, CallbackInfo ci)
    {
        input.getString("animalgenders:gender").ifPresent(s ->
        {
            try
            {
                animalgenders$gender = Gender.valueOf(s.toUpperCase());
                animalgenders$hasGender = true;
            }
            catch (IllegalArgumentException e)
            {
                animalgenders$gender = Gender.UNKNOWN;
                animalgenders$hasGender = false;
            }
        });
    }
}
