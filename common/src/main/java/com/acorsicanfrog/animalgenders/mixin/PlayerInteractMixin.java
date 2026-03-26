package com.acorsicanfrog.animalgenders.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.GenderAssignment;
import com.acorsicanfrog.animalgenders.platform.Services;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerInteractMixin
{
	@Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
	private void onInteractOn(Entity target, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir)
	{
		if (!(target instanceof Animal animal))
			return;

		if (!animal.level().isClientSide())
		{
			GenderAssignment.assignIfMissing(animal, null);
		}

		Player self = (Player) (Object) this;
		ItemStack stack = self.getItemInHand(hand);

		if (!stack.is(Items.BUCKET))
			return;

		if (!Services.PLATFORM.hasGender(animal))
			return;

		Gender g = Services.PLATFORM.getGender(animal);

		if (g == Gender.MALE)
		{
			if (!self.level().isClientSide() && self instanceof ServerPlayer sp)
			{
				sp.sendSystemMessage(Component.translatable("message.animalgenders.milking_male_impossible"));
			}

			cir.setReturnValue(InteractionResult.FAIL);
			cir.cancel();
		}
	}
}