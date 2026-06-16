package com.autofish.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoFishMod implements ClientModInitializer {

    public static final String MOD_ID = "autofish";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static AutoFishMod instance;

    private AutoFishConfig config;
    private AutoFishController controller;

    // Keybinds
    private KeyBinding toggleKey;   // F7  — toggle on/off
    private KeyBinding settingsKey; // H   — open settings screen

    @Override
    public void onInitializeClient() {
        instance = this;

        config     = new AutoFishConfig();
        controller = new AutoFishController(config, net.minecraft.client.MinecraftClient.getInstance());

        // --- Register keybinds ---
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autofish.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
                "category.autofish"
        ));

        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autofish.settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.autofish"
        ));

        // --- Client tick: handle keys + controller ---
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            // Toggle AutoFish on/off with F7
            while (toggleKey.wasPressed()) {
                config.setEnabled(!config.isEnabled());
                if (config.isEnabled()) {
                    controller.reset();
                    LOGGER.info("[AutoFish] Enabled");
                } else {
                    LOGGER.info("[AutoFish] Disabled");
                }
                if (client.player != null) {
                    client.player.sendMessage(
                        net.minecraft.text.Text.literal(
                            config.isEnabled()
                                ? "§a[AutoFish] §2Enabled"
                                : "§c[AutoFish] §4Disabled"
                        ), true // action bar message
                    );
                }
            }

            // Open settings screen with H
            while (settingsKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new AutoFishScreen(config, null));
                }
            }

            // Run the fishing controller every tick
            controller.tick();
        });

        // --- Register HUD overlay ---
        HudRenderCallback.EVENT.register(new AutoFishHud(config, controller));

        LOGGER.info("[AutoFish] Mod loaded! F7 = toggle, H = settings.");
    }

    public static AutoFishMod getInstance() { return instance; }
    public AutoFishController getController() { return controller; }
    public AutoFishConfig getConfig() { return config; }
}
