package com.acorsicanfrog.animalgenders.compat.jade;

import java.util.List;

import com.acorsicanfrog.animalgenders.Constants;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.platform.Services;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

public enum GenderEntityProvider implements IEntityComponentProvider {

	INSTANCE;

	private static final Identifier UID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "entity_gender");
	private static final Identifier MALE_SPRITE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gender_male");
	private static final Identifier FEMALE_SPRITE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gender_female");
	private static final int ICON_SIZE = 10;

	@Override
	public Identifier getUid() {
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

		Identifier sprite = (gender == Gender.MALE) ? MALE_SPRITE : FEMALE_SPRITE;
		tooltip.add(List.of(
			JadeUI.sprite(sprite, ICON_SIZE, ICON_SIZE),
			JadeUI.spacer(4, 0),
			JadeUI.text(Component.translatable(gender.getTranslationKey())).tag(UID)
		));
	}
}
