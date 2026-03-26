package com.acorsicanfrog.animalgenders.events;

import com.acorsicanfrog.animalgenders.Constants;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;

// Safety net: prevents same-gender breeding at the event level, 
// even if the common AnimalCanMateMixin is bypassed for any reason.

@EventBusSubscriber(modid = Constants.MOD_ID)
public class MatingEvents 
{
    @SubscribeEvent
    public static void onBabySpawn(BabyEntitySpawnEvent event) 
    {
        if (event.getParentA() == null || event.getParentA().level().isClientSide()) return;
        if (!(event.getParentA() instanceof Animal parentA)) return;
        if (event.getParentB() == null || event.getParentB().level().isClientSide()) return;
        if (!(event.getParentB() instanceof Animal parentB)) return;

        if (!GenderAttachment.hasGender(parentA) || !GenderAttachment.hasGender(parentB))
            return;

        Gender genderA = GenderAttachment.getGender(parentA);
        Gender genderB = GenderAttachment.getGender(parentB);

        if (genderA == Gender.UNKNOWN || genderB == Gender.UNKNOWN)
            return;

        if (genderA == genderB)
            event.setCanceled(true);
    }
}