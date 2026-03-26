package com.acorsicanfrog.animalgenders.events;

import com.acorsicanfrog.animalgenders.Constants;
import com.acorsicanfrog.animalgenders.GenderAssignment;

import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class SpawnEvents 
{
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) 
    {
        if (event.getLevel().isClientSide()) return;

        if (!(event.getEntity() instanceof Animal animal)) return;

        GenderAssignment.assignIfMissing(animal, null);
    }
}
