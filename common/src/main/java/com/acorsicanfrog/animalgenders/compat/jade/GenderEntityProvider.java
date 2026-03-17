package com.acorsicanfrog.animalgenders.compat.jade;

import com.acorsicanfrog.animalgenders.Constants;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.platform.Services;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum GenderEntityProvider implements IEntityComponentProvider {

	INSTANCE;

	private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entity_gender");
	private static final ResourceLocation MALE_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "gender_male");
	private static final ResourceLocation FEMALE_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "gender_female");

	private static final int ICON_SIZE = 10;

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		var entity = accessor.getEntity();

		if (!Services.PLATFORM.hasGender(entity))
			return;

		Gender gender = Services.PLATFORM.getGender(entity);
		if (gender == null || gender == Gender.UNKNOWN)
			return;

		ResourceLocation sprite = (gender == Gender.MALE) ? MALE_SPRITE : FEMALE_SPRITE;
		IElementHelper helper = IElementHelper.get();
		tooltip.add(helper.sprite(sprite, ICON_SIZE, ICON_SIZE));
		tooltip.append(helper.spacer(4, 0));
		tooltip.append(Component.translatable(gender.getTranslationKey()));
	}
}
