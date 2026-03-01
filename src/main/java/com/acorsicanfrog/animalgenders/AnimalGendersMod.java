package com.acorsicanfrog.animalgenders;

import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(AnimalGendersMod.MOD_ID)
public class AnimalGendersMod 
{
    public static final String MOD_ID = "animalgenders";

    public AnimalGendersMod(IEventBus modEventBus, ModContainer modContainer) 
    {
        // Registers the attachment type during game startup (mod bus)
        GenderAttachment.ATTACHMENT_TYPES.register(modEventBus);

        // Register the common config (animalgenders-common.toml in the config folder)
        modContainer.registerConfig(ModConfig.Type.COMMON, AnimalGendersConfig.SPEC);
    }
}