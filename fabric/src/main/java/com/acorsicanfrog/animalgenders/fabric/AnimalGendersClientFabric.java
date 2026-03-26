package com.acorsicanfrog.animalgenders.fabric;

import com.acorsicanfrog.animalgenders.Constants;
import com.acorsicanfrog.animalgenders.Gender;
import com.acorsicanfrog.animalgenders.fabric.network.GenderNetworking;
import com.acorsicanfrog.animalgenders.platform.Services;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class AnimalGendersClientFabric implements ClientModInitializer
{
    private static final Identifier MALE_SPRITE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gender_male");
    private static final Identifier FEMALE_SPRITE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gender_female");
    private static final int ICON_SIZE = 16;

    @Override
    @SuppressWarnings("deprecation")
    public void onInitializeClient()
    {
        // Register client-side packet receiver
        GenderNetworking.registerClient();

        // Register HUD overlay
        HudRenderCallback.EVENT.register((guiGraphics, tickCounter) ->
        {
            Minecraft mc = Minecraft.getInstance();
            var player = mc.player;

            if (player == null || mc.level == null || mc.screen != null || mc.options.hideGui)
                return;

            // Don't render when Jade is handling it
            if (FabricLoader.getInstance().isModLoaded("jade"))
                return;

            Entity target = resolveTarget(mc);
            if (target == null)
                return;

            if (!Services.PLATFORM.hasGender(target))
                return;

            Gender g = Services.PLATFORM.getGender(target);
            if (g == null || g == Gender.UNKNOWN)
                return;

            Identifier sprite = (g == Gender.MALE) ? MALE_SPRITE : FEMALE_SPRITE;

            int screenW = mc.getWindow().getGuiScaledWidth();
            int screenH = mc.getWindow().getGuiScaledHeight();

            double distance = player.distanceTo(target);
            int yOffset = 40 + (int) Math.min(40, distance * 2.0);

            int x = (screenW - ICON_SIZE) / 2;
            int y = (screenH / 2) - yOffset - ICON_SIZE;

            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, ICON_SIZE, ICON_SIZE);

            guiGraphics.drawString(mc.font,
                    net.minecraft.network.chat.Component.translatable(g.getTranslationKey()),
                    x + ICON_SIZE + 4,
                    y + (ICON_SIZE - mc.font.lineHeight) / 2,
                    0xFFFFFFFF,
                    true);
        });
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
