package com.acorsicanfrog.animalgenders.events;

import com.acorsicanfrog.animalgenders.AnimalGendersMod;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = AnimalGendersMod.MOD_ID)
public class SpawnEvents 
{
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) 
		{
				// Only run on the server side
				if (event.getLevel().isClientSide()) return;

				// Only care about animals
				if (!(event.getEntity() instanceof Animal animal)) return;

				// Don't overwrite existing gender (e.g. chunk reloads)
				if (GenderAttachment.hasGender(animal)) return;

				Gender gender = animal.getRandom().nextBoolean() ? Gender.MALE : Gender.FEMALE;
				
				GenderAttachment.setGender(animal, gender);
    }
}