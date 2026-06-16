# AutoFish Mod — Minecraft 1.21.8 (Fabric / Feather Client)

Automatic fishing mod for **single-player survival** with fully randomised
cast timings so each fishing action feels natural and varied.

---

## Features

- **Auto-cast & auto-reel** — casts the rod, waits for a bite, reels in, and recasts automatically
- **Randomised delays** — every recast uses a freshly rolled random delay between your configured min and max (plus optional jitter), so no two casts are identical
- **In-game settings screen** — sliders for Min Delay, Max Delay, and Jitter, no config file editing needed
- **HUD overlay** — shows current state, delay range, and keybinds in the top-left corner
- **Feather Client compatible** — plain Fabric mod, no mixins that conflict with Feather's own systems

---

## Controls

| Key | Action |
|-----|--------|
| `F7` | Toggle AutoFish ON / OFF |
| `H`  | Open settings screen |

Both keys can be rebound in **Options → Controls → AutoFish Mod**.

---

## How to Install

### Requirements
- Minecraft **Java Edition 1.21.8**
- [Fabric Loader](https://fabricmc.net/use/installer/) **0.16.14+**
- [Fabric API](https://modrinth.com/mod/fabric-api) for 1.21.8

### With Feather Client
Feather Client supports Fabric mods natively. Simply:
1. Drop `autofish-1.0.0.jar` into your `.minecraft/mods` folder
2. Make sure Fabric API is also in `/mods`
3. Launch Minecraft through Feather as normal

---

## How to Build from Source

### Prerequisites
- JDK 21
- Internet connection (Gradle downloads Fabric dependencies)

### Steps
```bash
# Clone / extract the project folder
cd autofish

# On Linux/Mac
./gradlew build

# On Windows
gradlew.bat build
```

The compiled jar will appear at:
```
build/libs/autofish-1.0.0.jar
```

Copy that jar to your `.minecraft/mods` folder.

---

## Settings Explained

| Setting   | Default | Description |
|-----------|---------|-------------|
| Min Delay | 800 ms  | Shortest possible wait before recasting |
| Max Delay | 2500 ms | Longest possible wait before recasting |
| Jitter    | 400 ms  | Extra random time added on top of the base roll |

Each cast rolls: `random(min, max) + random(0, jitter)`

So with defaults you get anywhere from **800 ms to ~2900 ms** between each catch
and recast, with a different value every single time.

---

## Usage Tips

1. Hold a **fishing rod** in your main hand
2. Stand near water
3. Press **F7** to enable — the mod casts automatically
4. Adjust timings with **H** while fishing to taste
5. Press **F7** again to stop

> **Note:** Designed for single-player survival worlds only.
