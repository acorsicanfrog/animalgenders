package com.acorsicanfrog.animalgenders;

import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;
import com.acorsicanfrog.animalgenders.network.GenderSyncPayload;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(Constants.MOD_ID)
public class AnimalGendersMod 
{
    public AnimalGendersMod(IEventBus modEventBus, ModContainer modContainer) 
    {
        GenderAttachment.ATTACHMENT_TYPES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, AnimalGendersConfig.SPEC);
        modEventBus.addListener(AnimalGendersMod::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar(Constants.MOD_ID);
        registrar.playToClient(
            GenderSyncPayload.TYPE,
            GenderSyncPayload.STREAM_CODEC,
            (payload, context) -> context.enqueueWork(() ->
            {
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.level == null) return;
                net.minecraft.world.entity.Entity entity = mc.level.getEntity(payload.entityId());
                if (entity == null) return;
                try
                {
                    Gender gender = Gender.valueOf(payload.genderName().toUpperCase());
                    GenderAttachment.setGender(entity, gender);
                }
                catch (IllegalArgumentException ignored) {}
            })
        );
    }
}
