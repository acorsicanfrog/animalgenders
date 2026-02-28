package com.acorsicanfrog.animalgenders.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

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

		// Only perform mating checks on the server to avoid client-side desyncs
		if (self.level().isClientSide())
			return;

		// If either entity doesn't have a gender assigned yet, fall back to vanilla behavior
		if (!GenderAttachment.hasGender(self) || !GenderAttachment.hasGender(other))
			return;

		Gender g1 = GenderAttachment.getGender(self);
		Gender g2 = GenderAttachment.getGender(other);

		if (g1 == Gender.UNKNOWN || g2 == Gender.UNKNOWN)
			return;

		if (g1 == g2) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}