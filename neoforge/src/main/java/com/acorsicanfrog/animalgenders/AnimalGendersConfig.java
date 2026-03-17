package com.acorsicanfrog.animalgenders;

import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;

public class AnimalGendersConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> GENDER_BLACKLIST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> GENDER_WHITELIST;
    
    public static final ModConfigSpec.DoubleValue FEMALE_CHANCE;

    static
    {
        BUILDER.comment("Animal Genders – gender assignment config").push("gender_assignment");

        GENDER_BLACKLIST = BUILDER
            .comment(
                "Entity types that should NEVER receive a gender.",
                "Entries are registry names, e.g. \"minecraft:bee\" or \"mymod:custom_animal\".",
                "Takes precedence over the whitelist."
            )
            .defineListAllowEmpty(
                "blacklist",
                List.of("minecraft:bee"),
                () -> "minecraft:bee",
                e -> e instanceof String s && !s.isBlank()
            );

        GENDER_WHITELIST = BUILDER
            .comment(
                "When non-empty, ONLY the entity types listed here will receive a gender.",
                "All other animals are skipped unless they are also in the blacklist.",
                "Leave empty (default) to apply genders to every eligible animal."
            )
            .defineListAllowEmpty(
                "whitelist",
                List.of(),
                () -> "modid:entity_name",
                e -> e instanceof String s && !s.isBlank()
            );

        FEMALE_CHANCE = BUILDER
            .comment(
                "Probability (0.0 to 1.0) that a newly spawned animal is assigned female.",
                "The remaining probability is assigned male.",
                "Default: 0.8 (80% female, 20% male)."
            )
            .defineInRange("female_chance", 0.8, 0.0, 1.0);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}
