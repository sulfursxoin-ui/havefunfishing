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

    public enum State { IDLE, WAITING_FOR_BOBBER, FISHING, REELING_IN, WAITING_TO_RECAST }

    private State state = State.IDLE;
    private long actionTime = 0;
    private long lastDelay = 0;

    public AutoFishController(AutoFishConfig config, MinecraftClient client) {
        this.config = config;
        this.client = client;
    }

    public void tick() {
        if (!config.isEnabled()) {
            state = State.IDLE;
            return;
        }

        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return;

        ItemStack held = player.getMainHandStack();
        if (!(held.getItem() instanceof FishingRodItem)) {
            ItemStack offHand = player.getOffHandStack();
            if (!(offHand.getItem() instanceof FishingRodItem)) {
                return;
            }
        }

        long now = System.currentTimeMillis();
        FishingBobberEntity bobber = player.fishHook;

        if (state == State.IDLE) {
            castLine();
            state = State.WAITING_FOR_BOBBER;
            actionTime = now + 1500;
        } else if (state == State.WAITING_FOR_BOBBER) {
            if (bobber != null) {
                state = State.FISHING;
            } else if (now > actionTime) {
                castLine();
                actionTime = now + 1500;
            }
        } else if (state == State.FISHING) {
            if (bobber == null) {
                state = State.IDLE;
            }
        } else if (state == State.REELING_IN) {
            castLine();
            lastDelay = config.nextRandomDelay();
            actionTime = now + lastDelay;
            state = State.WAITING_TO_RECAST;
        } else if (state == State.WAITING_TO_RECAST) {
            if (now >= actionTime) {
                state = State.IDLE;
            }
        }
    }

    public void onBobberHooked() {
        if (state == State.FISHING) {
            state = State.REELING_IN;
        }
    }

    private void castLine() {
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        client.interactionManager.interactItem(player, Hand.MAIN_HAND);
    }

    public State getState() { return state; }
    public long getLastDelay() { return lastDelay; }

    public void reset() {
        state = State.IDLE;
        actionTime = 0;
    }
}
