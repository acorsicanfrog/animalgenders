package com.acorsicanfrog.animalgenders;

import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MOD_ID)
public class AnimalGendersMod 
{
    public AnimalGendersMod(IEventBus modEventBus, ModContainer modContainer) 
    {
        GenderAttachment.ATTACHMENT_TYPES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, AnimalGendersConfig.SPEC);
    }
}
