package com.acorsicanfrog.animalgenders.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.platform.Services;

import net.minecraft.world.entity.animal.Animal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalCanMateMixin 
{
	@Inject(method = "canMate", at = @At("HEAD"), cancellable = true)
	private void onCanMate(Animal other, CallbackInfoReturnable<Boolean> cir) 
	{
		Animal self = (Animal) (Object) this;

		if (self.level().isClientSide())
			return;

		if (!Services.PLATFORM.hasGender(self) || !Services.PLATFORM.hasGender(other))
			return;

		Gender g1 = Services.PLATFORM.getGender(self);
		Gender g2 = Services.PLATFORM.getGender(other);

		if (g1 == Gender.UNKNOWN || g2 == Gender.UNKNOWN)
			return;

		if (g1 == g2) 
		{
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}