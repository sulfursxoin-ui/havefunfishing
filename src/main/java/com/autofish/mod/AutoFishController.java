package com.autofish.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class AutoFishController {

    private final AutoFishConfig config;
    private final MinecraftClient client;

    // State machine
    private enum State { IDLE, WAITING_FOR_BOBBER, FISHING, REELING_IN, WAITING_TO_RECAST }

    private State state = State.IDLE;
    private long actionTime = 0; // when to perform the next action (ms epoch)
    private long lastDelay = 0;  // last computed random delay (for HUD display)

    public AutoFishController(AutoFishConfig config, MinecraftClient client) {
        this.config = config;
        this.client = client;
    }

    /** Called every client tick (~20/s). */
    public void tick() {
        if (!config.isEnabled()) {
            state = State.IDLE;
            return;
        }

        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return;

        // Make sure the player is holding a fishing rod
        ItemStack held = player.getMainHandStack();
        if (!(held.getItem() instanceof FishingRodItem)) {
            // Try off-hand
            ItemStack offHand = player.getOffHandStack();
            if (!(offHand.getItem() instanceof FishingRodItem)) {
                return; // No rod in hand
            }
        }

        long now = System.currentTimeMillis();
        FishingBobberEntity bobber = player.fishHook;

        switch (state) {
            case IDLE -> {
                // Cast the line immediately
                castLine();
                state = State.WAITING_FOR_BOBBER;
                actionTime = now + 1500; // give bobber time to land
            }

            case WAITING_FOR_BOBBER -> {
                if (bobber != null) {
                    state = State.FISHING;
                } else if (now > actionTime) {
                    // Bobber never appeared — try casting again
                    castLine();
                    actionTime = now + 1500;
                }
            }

            case FISHING -> {
                if (bobber == null) {
                    // Bobber disappeared without us reeling — recast
                    state = State.IDLE;
                    return;
                }
                // The mixin notifies us via onBobberHooked() when something bites
                // We poll here as a fallback
            }

            case REELING_IN -> {
                // Reel in (right-click again while bobber is out)
                castLine(); // second use = reel in
                lastDelay = config.nextRandomDelay();
                actionTime = now + lastDelay;
                state = State.WAITING_TO_RECAST;
            }

            case WAITING_TO_RECAST -> {
                if (now >= actionTime) {
                    state = State.IDLE;
                }
            }
        }
    }

    /** Called by the mixin when the bobber detects a caught entity. */
    public void onBobberHooked() {
        if (state == State.FISHING) {
            state = State.REELING_IN;
        }
    }

    private void castLine() {
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        // Simulate right-click on main hand (use item)
        client.interactionManager.interactItem(player, Hand.MAIN_HAND);
    }

    public State getState() { return state; }
    public long getLastDelay() { return lastDelay; }

    public void reset() {
        state = State.IDLE;
        actionTime = 0;
    }
}
