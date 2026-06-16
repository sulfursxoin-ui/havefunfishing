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
        if (client.player == null || client.options.debugEnabled) return;

        // Only render when mod is enabled
        if (!config.isEnabled()) {
            // Show a small "AutoFish: OFF" hint
            context.drawText(client.textRenderer,
                Text.literal("§7[AutoFish: §cOFF§7]"),
                4, 4, 0xFFFFFF, true);
            return;
        }

        String stateLabel = switch (controller.getState().name()) {
            case "IDLE"              -> "§eCasting...";
            case "WAITING_FOR_BOBBER"-> "§eWaiting for bobber...";
            case "FISHING"           -> "§aFishing...";
            case "REELING_IN"        -> "§bReeling in!";
            case "WAITING_TO_RECAST" -> "§6Waiting to recast (" + controller.getLastDelay() + "ms)";
            default                  -> "§7Unknown";
        };

        context.drawText(client.textRenderer,
            Text.literal("§a[AutoFish: §2ON§a]"), 4, 4, 0xFFFFFF, true);
        context.drawText(client.textRenderer,
            Text.literal("§fState: " + stateLabel), 4, 14, 0xFFFFFF, true);
        context.drawText(client.textRenderer,
            Text.literal("§fDelay range: §e" + config.getMinDelayMs() + "ms §f- §e" + config.getMaxDelayMs() + "ms"),
            4, 24, 0xFFFFFF, true);
        context.drawText(client.textRenderer,
            Text.literal("§7Press §fH §7to open settings | §fF7 §7to toggle"),
            4, 34, 0xFFFFFF, true);
    }
}
