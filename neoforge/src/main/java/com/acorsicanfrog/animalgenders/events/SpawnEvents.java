package com.acorsicanfrog.animalgenders.events;

import java.util.List;

import com.acorsicanfrog.animalgenders.AnimalGendersConfig;
import com.acorsicanfrog.animalgenders.Constants;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.core.registries.BuiltInRegistries;
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

        String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(animal.getType()).toString();

        List<? extends String> blacklist = AnimalGendersConfig.GENDER_BLACKLIST.get();
        if (blacklist.contains(entityId)) return;

        List<? extends String> whitelist = AnimalGendersConfig.GENDER_WHITELIST.get();
        if (!whitelist.isEmpty() && !whitelist.contains(entityId)) return;

        if (GenderAttachment.hasGender(animal)) return;

        Double configChance = AnimalGendersConfig.FEMALE_CHANCE.get();
        double femaleChance = animal.isBaby() ? 0.5 : (configChance != null ? configChance : 0.5);
        
        Gender gender = animal.getRandom().nextDouble() < femaleChance ? Gender.FEMALE : Gender.MALE;
        
        GenderAttachment.setGender(animal, gender);
    }
}
