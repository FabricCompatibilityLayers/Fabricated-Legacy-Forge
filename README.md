# Fabricated Legacy Forge

A compatibility layer that lets legacy Minecraft Forge/FML mods run on [Fabric Loader](https://fabricmc.net/) for Minecraft **1.3.2**.

Available on Modrinth: https://modrinth.com/mod/fabricated-forge

---

## What it does

Forge mods written for Minecraft 1.3.2 expect the old Forge/FML (Forge Mod Loader) APIs and use obfuscated class names. Fabric Loader uses a completely different mod-loading pipeline and the Calamus namespace. This project bridges the two:

- Bootstraps the FML mod-loading lifecycle (mod discovery, `@Mod` initialization, event bus, coremods) inside a Fabric `preLaunch` entrypoint
- Remaps class/method/field references at runtime so mods compiled against Searge/MCP names resolve correctly against the Fabric-deobfuscated game
- Intercepts reflection calls (`Class.forName`, `getDeclaredMethod`, etc.) at the bytecode level so runtime lookups are also remapped
- Reimplements the MinecraftForge API surface (block hooks, entity events, rendering extensions, IBXM audio) as Fabric Mixins

---

## Requirements

| Dependency | Version  |
|---|----------|
| Minecraft | 1.3.2    |
| Fabric Loader | ≥ 0.18.2 |
| [Mod Remapping API](https://github.com/FabricCompatibilityLayers/Mod-Remapping-API) | ≥ 1.28.3 |

---

## Project structure

```
fabricated-fml/          Core FML implementation
  src/main/java/         Mixins + mod-loading code
  src/main/resources/    fabricated-fml.mixins.json, access widener
  patches/               Legacy source patches (do not add new ones here)

fabricated-forge/        MinecraftForge API layer
  src/main/java/         Mixins + Forge hooks
  src/main/resources/    fabricated-forge.mixins.json, access widener
  patches/               Legacy source patches (do not add new ones here)

fabricated-forge-server-stubs/   Server-side versions of classes originally only available on the client
flf-mod-fixes/                   Runtime fixes for known problematic Forge mods
```

New Minecraft class modifications should be Mixins, not patches. The `patches/` directories are legacy and are being migrated.

---

## Building

Requires JDK 17 (produces Java 8 bytecode).

```bash
# Build everything
./gradlew build

# Apply code style (required before committing)
./gradlew spotlessApply

# Build a single submodule
./gradlew :fabricated-fml:build
./gradlew :fabricated-forge:build
```

CI validates with `spotlessCheck` + `build`. There are no automated tests.

---

## Contributing

- All Minecraft class modifications must use Spongepowered Mixins, not patches.
- Use access wideners to expose private members; avoid reflection inside project code.
- Run `./gradlew spotlessApply` before committing — CI enforces the style check.
- OptiFine compatibility is handled in `FabricatedFMLMixinPlugin`; keep that in mind when adding Mixins that touch rendering.

---

## License

This project bundles code from several upstream projects under different licenses. See [licensing.md](licensing.md) for the full breakdown.

| Component | License |
|---|---|
| Project code (`io.github.fabriccompatibilitylayers.*`) | OSL-3.0 |
| Forge Mod Loader (`cpw.mods.fml.*`) | LGPL-2.1 |
| MinecraftForge (`net.minecraftforge.*`) | Minecraft Forge Public License v1.0 |
| IBXM (`ibxm.*`) | BSD-3-Clause |
| Paulscode SoundSystem CodecIBXM | Paulscode-SoundSystem (custom permissive) |