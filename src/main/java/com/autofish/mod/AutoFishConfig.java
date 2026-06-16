package com.autofish.mod;

public class AutoFishConfig {

    // Minimum delay in milliseconds before recasting after a catch
    private int minDelayMs = 800;

    // Maximum delay in milliseconds before recasting after a catch
    private int maxDelayMs = 2500;

    // Extra random jitter added on top of base random delay (ms)
    private int jitterMs = 400;

    // Whether the mod is currently active
    private boolean enabled = false;

    public int getMinDelayMs() { return minDelayMs; }
    public void setMinDelayMs(int v) { this.minDelayMs = Math.max(100, v); }

    public int getMaxDelayMs() { return maxDelayMs; }
    public void setMaxDelayMs(int v) { this.maxDelayMs = Math.max(minDelayMs + 100, v); }

    public int getJitterMs() { return jitterMs; }
    public void setJitterMs(int v) { this.jitterMs = Math.max(0, v); }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    /**
     * Computes a fresh randomised delay every call so each cast has
     * a different wait time, making behaviour harder to pattern-detect.
     */
    public long nextRandomDelay() {
        long base = minDelayMs + (long)(Math.random() * (maxDelayMs - minDelayMs));
        long jitter = (long)(Math.random() * jitterMs);
        return base + jitter;
    }
}
