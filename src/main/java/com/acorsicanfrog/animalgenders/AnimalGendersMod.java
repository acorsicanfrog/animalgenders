package com.acorsicanfrog.animalgenders;

import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(AnimalGendersMod.MOD_ID)
public class AnimalGendersMod 
{
    public static final String MOD_ID = "animalgenders";

    public AnimalGendersMod(IEventBus modEventBus) 
    {
        // Registers the attachment type during game startup (mod bus)
        GenderAttachment.ATTACHMENT_TYPES.register(modEventBus);
    }
}