package com.acorsicanfrog.animalgenders.events;

import com.acorsicanfrog.animalgenders.AnimalGendersMod;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;

// not strictly required, but useful as a safety net. If the mixin fails for some reason, this will still prevent same gender breeding on the server side, which is where the actual baby spawning logic happens. The mixin just prevents the breeding process from starting in the first place, which is more efficient and also prevents any client-side desync issues.

@EventBusSubscriber(modid = AnimalGendersMod.MOD_ID)
public class MatingEvents 
{
    @SubscribeEvent
    public static void onBabySpawn(BabyEntitySpawnEvent event) 
    {
        // Ensure this logic only runs on the server
        if (event.getParentA() == null || event.getParentA().level().isClientSide()) return;
        if (!(event.getParentA() instanceof Animal parentA)) return;
        if (event.getParentB() == null || event.getParentB().level().isClientSide()) return;
        if (!(event.getParentB() instanceof Animal parentB)) return;

        // If either parent doesn't have an assigned gender yet, fall back to vanilla behavior
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