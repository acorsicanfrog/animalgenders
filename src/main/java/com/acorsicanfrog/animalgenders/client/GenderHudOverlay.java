package com.acorsicanfrog.animalgenders.client;

import com.acorsicanfrog.animalgenders.AnimalGendersMod;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import com.mojang.blaze3d.systems.RenderSystem;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * Renders a gender icon on the HUD when the player's crosshair is aimed at an
 * entity with a known gender.
 *
 * Texture requirements: assets/<modid>/textures/gui/gender_male.png and
 * gender_female.png must be saved as 32-bit RGBA PNGs (not indexed/paletted).
 * GuiGraphics.blit handles loading them automatically from the resource pack.
 */
@EventBusSubscriber(modid = AnimalGendersMod.MOD_ID)
public class GenderHudOverlay {
	// private static final Logger LOGGER = LoggerFactory.getLogger(AnimalGendersMod.MOD_ID);

	private static final ResourceLocation MALE_TEX = ResourceLocation.fromNamespaceAndPath(AnimalGendersMod.MOD_ID,
			"textures/gui/gender_male.png");
	private static final ResourceLocation FEMALE_TEX = ResourceLocation.fromNamespaceAndPath(AnimalGendersMod.MOD_ID,
			"textures/gui/gender_female.png");

	private static final int ICON_SIZE = 16;

	@SubscribeEvent
	public static void onRenderOverlay(final net.neoforged.neoforge.client.event.RenderGuiEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();

		// Don't render while a screen is open (inventory, chat, etc.)
		if (mc.player == null || mc.level == null || mc.screen != null)
			return;

		Entity target = resolveTarget(mc);
		if (target == null)
			return;

		if (!GenderAttachment.hasGender(target))
			return;

		Gender g = GenderAttachment.getGender(target);
		if (g == null || g == Gender.UNKNOWN)
			return;

		ResourceLocation tex = (g == Gender.MALE) ? MALE_TEX : FEMALE_TEX;

		int screenW = mc.getWindow().getGuiScaledWidth();
		int screenH = mc.getWindow().getGuiScaledHeight();

		double distance = mc.player.distanceTo(target);
		// Push the icon higher above the crosshair as the entity gets further away
		int yOffset = 40 + (int) Math.min(40, distance * 2.0);

		int x = (screenW - ICON_SIZE) / 2;
		int y = (screenH / 2) - yOffset - ICON_SIZE;

		var gui = event.getGuiGraphics();

		// GuiGraphics manages its own shader state — do NOT call
		// RenderSystem.setShader / setShaderTexture before blit, it will conflict.
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		// blit(ResourceLocation, destX, destY, srcU, srcV, width, height, texW, texH)
		// For a 16x16 texture used in full: srcU=0, srcV=0, texW=16, texH=16.
		gui.blit(tex, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

		RenderSystem.disableBlend();

		// Draw gender label to the right of the icon, vertically centred on it
		gui.drawString(mc.font, net.minecraft.network.chat.Component.translatable(g.getTranslationKey()), x + ICON_SIZE + 4, y + (ICON_SIZE - mc.font.lineHeight) / 2, 0xFFFFFF);

		// LOGGER.debug("GenderHudOverlay: rendered {} icon at ({}, {})", g, x, y);
	}

	/**
	 * Returns the entity currently under the player's crosshair, or null.
	 * Prefers mc.crosshairPickEntity (updated every tick by the engine) and
	 * falls back to the geometry hit result for edge cases.
	 */
	private static Entity resolveTarget(Minecraft mc) {
		if (mc.crosshairPickEntity != null)
			return mc.crosshairPickEntity;

		HitResult hr = mc.hitResult;
		if (hr instanceof EntityHitResult ehr)
			return ehr.getEntity();

		return null;
	}
}