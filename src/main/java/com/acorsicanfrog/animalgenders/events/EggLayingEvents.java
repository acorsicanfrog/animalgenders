package com.acorsicanfrog.animalgenders.events;

import com.acorsicanfrog.animalgenders.AnimalGendersMod;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.world.entity.animal.Chicken;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = AnimalGendersMod.MOD_ID)
public class EggLayingEvents 
{
    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event) 
    {
        // Only run on the server side
        if (event.getEntity().level().isClientSide())
            return;

        // Only target Chickens
        if (!(event.getEntity() instanceof Chicken chicken))
            return;

        // Only male chickens should have their egg time reset. If gender is unknown or not set, fall back to vanilla.
        if (!GenderAttachment.hasGender(chicken))
            return;

        Gender g = GenderAttachment.getGender(chicken);
        
        if (g != Gender.MALE)
            return;

        // Safety-net: only kick in when the countdown is about to reach 0.
        // The ChickenEggMixin / EntityEggSpawnMixin already prevent the actual item
        // from spawning, but resetting the timer here prevents noisy log entries
        // from any third-party mod that may listen to the egg-lay moment.
        // Running this every tick for every male chicken would be needlessly expensive.
        if (chicken.eggTime <= 1)
            chicken.eggTime = chicken.getRandom().nextInt(6000) + 6000;
    }
}