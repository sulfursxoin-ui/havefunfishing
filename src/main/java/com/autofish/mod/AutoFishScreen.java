package com.autofish.mod;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class AutoFishScreen extends Screen {

    private final AutoFishConfig config;
    private final Screen parent;

    public AutoFishScreen(AutoFishConfig config, Screen parent) {
        super(Text.literal("AutoFish Settings"));
        this.config = config;
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 4;

        // --- Min Delay Slider ---
        this.addDrawableChild(new SliderWidget(centerX - 100, startY, 200, 20,
                Text.literal("Min Delay: " + config.getMinDelayMs() + "ms"),
                (config.getMinDelayMs() - 100) / 4900.0) {
            @Override
            protected void updateMessage() {
                int val = 100 + (int)(this.value * 4900);
                setMessage(Text.literal("Min Delay: " + val + "ms"));
            }
            @Override
            protected void applyValue() {
                int val = 100 + (int)(this.value * 4900);
                config.setMinDelayMs(val);
                if (config.getMaxDelayMs() < val + 100) config.setMaxDelayMs(val + 100);
            }
        });

        // --- Max Delay Slider ---
        this.addDrawableChild(new SliderWidget(centerX - 100, startY + 30, 200, 20,
                Text.literal("Max Delay: " + config.getMaxDelayMs() + "ms"),
                (config.getMaxDelayMs() - 200) / 4800.0) {
            @Override
            protected void updateMessage() {
                int val = 200 + (int)(this.value * 4800);
                setMessage(Text.literal("Max Delay: " + val + "ms"));
            }
            @Override
            protected void applyValue() {
                int val = 200 + (int)(this.value * 4800);
                config.setMaxDelayMs(val);
            }
        });

        // --- Jitter Slider ---
        this.addDrawableChild(new SliderWidget(centerX - 100, startY + 60, 200, 20,
                Text.literal("Jitter: " + config.getJitterMs() + "ms"),
                config.getJitterMs() / 1000.0) {
            @Override
            protected void updateMessage() {
                int val = (int)(this.value * 1000);
                setMessage(Text.literal("Jitter: " + val + "ms"));
            }
            @Override
            protected void applyValue() {
                config.setJitterMs((int)(this.value * 1000));
            }
        });

        // --- Toggle Enable/Disable ---
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(config.isEnabled() ? "§aAutoFish: ON" : "§cAutoFish: OFF"),
                btn -> {
                    config.setEnabled(!config.isEnabled());
                    btn.setMessage(Text.literal(config.isEnabled() ? "§aAutoFish: ON" : "§cAutoFish: OFF"));
                })
                .dimensions(centerX - 100, startY + 100, 200, 20)
                .build());

        // --- Done ---
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Done"),
                btn -> this.client.setScreen(parent))
                .dimensions(centerX - 50, startY + 140, 100, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 4 - 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
