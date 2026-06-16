package com.autofish.mod;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class AutoFishHud implements HudRenderCallback {

    private final AutoFishConfig config;
    private final AutoFishController controller;

    public AutoFishHud(AutoFishConfig config, AutoFishController controller) {
        this.config = config;
        this.controller = controller;
    }

    @Override
    public void onHudRender(DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        if (!config.isEnabled()) {
            context.drawText(client.textRenderer,
                Text.literal("§7[AutoFish: §cOFF§7]"),
                4, 4, 0xFFFFFF, true);
            return;
        }

        String stateName = controller.getState().toString();
        String stateLabel = switch (stateName) {
            case "IDLE"               -> "§eCasting...";
            case "WAITING_FOR_BOBBER" -> "§eWaiting for bobber...";
            case "FISHING"            -> "§aFishing...";
            case "REELING_IN"         -> "§bReeling in!";
            case "WAITING_TO_RECAST"  -> "§6Waiting to recast (" + controller.getLastDelay() + "ms)";
            default                   -> "§7Unknown";
        };

        context.drawText(client.textRenderer, Text.literal("§a[AutoFish: §2ON§a]"), 4, 4, 0xFFFFFF, true);
        context.drawText(client.textRenderer, Text.literal("§fState: " + stateLabel), 4, 14, 0xFFFFFF, true);
        context.drawText(client.textRenderer, Text.literal("§fDelay: §e" + config.getMinDelayMs() + "ms §f- §e" + config.getMaxDelayMs() + "ms"), 4, 24, 0xFFFFFF, true);
        context.drawText(client.textRenderer, Text.literal("§7F7 §ftoggle | §7H §fsettings"), 4, 34, 0xFFFFFF, true);
    }
}
