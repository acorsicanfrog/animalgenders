package com.acorsicanfrog.animalgenders.client;

import com.acorsicanfrog.animalgenders.Constants;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.attachment.GenderAttachment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class GenderHudOverlay {

	private static final ResourceLocation MALE_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "gender_male");
	private static final ResourceLocation FEMALE_SPRITE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "gender_female");

	private static final int ICON_SIZE = 16;

	@SubscribeEvent
	public static void onRenderOverlay(final net.neoforged.neoforge.client.event.RenderGuiEvent.Post event) 
	{
		Minecraft mc = Minecraft.getInstance();
		var player = mc.player;

		if (player == null || mc.level == null || mc.screen != null || mc.options.hideGui
				|| ModList.get().isLoaded("jade"))
			return;

		Entity target = resolveTarget(mc);
		if (target == null)
			return;

		if (!GenderAttachment.hasGender(target))
			return;

		Gender g = GenderAttachment.getGender(target);
		if (g == null || g == Gender.UNKNOWN)
			return;

		ResourceLocation sprite = (g == Gender.MALE) ? MALE_SPRITE : FEMALE_SPRITE;

		int screenW = mc.getWindow().getGuiScaledWidth();
		int screenH = mc.getWindow().getGuiScaledHeight();

		double distance = player.distanceTo(target);
		
		int yOffset = 40 + (int) Math.min(40, distance * 2.0);

		int x = (screenW - ICON_SIZE) / 2;
		int y = (screenH / 2) - yOffset - ICON_SIZE;

		var gui = event.getGuiGraphics();

		gui.blitSprite(RenderType::guiTextured, sprite, x, y, ICON_SIZE, ICON_SIZE);

		gui.drawString(mc.font, net.minecraft.network.chat.Component.translatable(g.getTranslationKey()), x + ICON_SIZE + 4, y + (ICON_SIZE - mc.font.lineHeight) / 2, 0xFFFFFF);
	}

	private static Entity resolveTarget(Minecraft mc) 
	{
		if (mc.crosshairPickEntity != null)
			return mc.crosshairPickEntity;

		HitResult hr = mc.hitResult;
		if (hr instanceof EntityHitResult ehr)
			return ehr.getEntity();

		return null;
	}
}