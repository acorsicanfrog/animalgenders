package com.acorsicanfrog.animalgenders.mixin;

import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerInteractMixin 
{
	private static final Logger LOGGER = LogManager.getLogger("animalgenders.PlayerInteractMixin");

	// try several likely method names (covers mapping differences)
	@Inject(method = { "interact", "interactAt", "interactOn" }, at = @At("HEAD"), cancellable = true)
	private void onInteract(net.minecraft.world.entity.Entity target, InteractionHand hand,
			CallbackInfoReturnable<InteractionResult> cir) 
			{
		try {
			if (!(target instanceof Animal))
				return;

			Player self = (Player) (Object) this;
			ItemStack stack = self.getItemInHand(hand);
			
			if (!stack.is(Items.BUCKET))
				return;

			Animal animal = (Animal) target;
			// If the client doesn't have attachment data yet, let the server decide.
			if (!GenderAttachment.hasGender(animal))
				return;

			Gender g = GenderAttachment.getGender(animal);

			if (g == Gender.MALE) 
			{
				if (!self.level().isClientSide() && self instanceof ServerPlayer sp) 
				{
					sp.sendSystemMessage(Component.translatable("message.animalgenders.milking_male_impossible"));
				}

				cir.setReturnValue(InteractionResult.FAIL);
				cir.cancel();
			}
		} catch (Throwable t)
		{
			LOGGER.error("Error in PlayerInteractMixin.onInteract", t);
		}
	}
}