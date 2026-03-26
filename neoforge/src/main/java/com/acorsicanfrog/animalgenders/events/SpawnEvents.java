package com.acorsicanfrog.animalgenders.events;

import com.acorsicanfrog.animalgenders.Constants;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.GenderAssignment;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;
import com.acorsicanfrog.animalgenders.network.GenderSyncPayload;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

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

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event)
    {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        var target = event.getTarget();
        if (!(target instanceof Animal animal)) return;

        GenderAssignment.assignIfMissing(animal, null);

        if (!GenderAttachment.hasGender(animal)) return;
        Gender gender = GenderAttachment.getGender(animal);
        if (gender == Gender.UNKNOWN) return;

        PacketDistributor.sendToPlayer(serverPlayer,
                new GenderSyncPayload(animal.getId(), gender.name()));
    }
}