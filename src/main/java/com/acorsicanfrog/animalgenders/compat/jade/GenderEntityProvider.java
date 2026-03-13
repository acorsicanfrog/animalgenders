package com.acorsicanfrog.animalgenders.compat.jade;

import com.acorsicanfrog.animalgenders.AnimalGendersMod;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum GenderEntityProvider implements IEntityComponentProvider {

	INSTANCE;

	private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(AnimalGendersMod.MOD_ID, "entity_gender");

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		var entity = accessor.getEntity();

		if (!GenderAttachment.hasGender(entity))
			return;

		Gender gender = GenderAttachment.getGender(entity);
		if (gender == null || gender == Gender.UNKNOWN)
			return;

		tooltip.add(Component.translatable(gender.getTranslationKey()));
	}
}
