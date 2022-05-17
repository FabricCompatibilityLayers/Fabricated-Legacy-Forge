package fr.catcore.fabricatedforge.mixin.forgefml.world.level;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(targets = {"net/minecraft/world/level/LevelGeneratorType", "net/minecraft/class_1160"})
public interface LevelGeneratorTypeAccessor {

    @Accessor(value = "base11Biomes", remap = false)
    static Biome[] getBase11Biomes() {
        return new Biome[0];
    }

    @Accessor(value = "base11Biomes", remap = false)
    static Biome[] getBase12Biomes() {
        return new Biome[0];
    }
}
