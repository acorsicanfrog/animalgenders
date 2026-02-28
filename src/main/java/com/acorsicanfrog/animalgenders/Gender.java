package com.acorsicanfrog.animalgenders;

import com.mojang.serialization.Codec;

public enum Gender 
{
    UNKNOWN,
    MALE,
    FEMALE;

    public static final Codec<Gender> CODEC = Codec.STRING.xmap(
        s -> {
            try {
                return Gender.valueOf(s.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNKNOWN;
            }
        },
        Gender::name
    );

    public String getTranslationKey() 
    {
        return "gender." + AnimalGendersMod.MOD_ID + "." + this.name().toLowerCase();
    }
}