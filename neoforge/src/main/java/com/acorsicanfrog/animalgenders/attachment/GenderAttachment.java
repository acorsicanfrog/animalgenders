package com.acorsicanfrog.animalgenders.attachment;

import java.util.function.Supplier;

import com.acorsicanfrog.animalgenders.Constants;
import com.acorsicanfrog.animalgenders.Gender;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class GenderAttachment 
{
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final Supplier<AttachmentType<Gender>> GENDER =
        ATTACHMENT_TYPES.register("gender", () ->
            AttachmentType.builder(() -> Gender.UNKNOWN)
                .serialize(Gender.CODEC)
                .sync((holder, to) -> true,
                    net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8
                        .map((java.util.function.Function<String, Gender>) s -> {
                                try 
                                {
                                    return Gender.valueOf(s.toUpperCase());
                                } 
                                catch (IllegalArgumentException e) 
                                {
                                    return Gender.UNKNOWN;
                                }
                            },
                             (java.util.function.Function<Gender, String>) Gender::name)
                )
                .build()
        );

    public static Gender getGender(Entity entity) 
    {
        return entity.getData(GENDER.get());
    }

    public static void setGender(Entity entity, Gender gender) 
    {
        entity.setData(GENDER.get(), gender);
    }

    public static boolean hasGender(Entity entity) 
    {
        return entity.hasData(GENDER.get());
    }
}
