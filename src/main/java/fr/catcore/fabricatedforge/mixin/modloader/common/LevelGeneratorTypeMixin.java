package fr.catcore.fabricatedforge.mixin.modloader.common;

import fr.catcore.fabricatedforge.mixininterface.ILevelGeneratorType;
import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.SingletonBiomeSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.FlatChunkGenerator;
import net.minecraft.world.chunk.SurfaceChunkGenerator;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelGeneratorType.class)
public class LevelGeneratorTypeMixin implements ILevelGeneratorType {
    @Shadow @Final public static LevelGeneratorType FLAT;

    @Override
    public LayeredBiomeSource getChunkManager(World world) {
        return (LayeredBiomeSource)(((LevelGeneratorType)(Object)this) == FLAT ? new SingletonBiomeSource(Biome.PLAINS, 0.5F, 0.5F) : new LayeredBiomeSource(world));
    }

    @Override
    public ChunkProvider getChunkGenerator(World world) {
        return (ChunkProvider)(((LevelGeneratorType)(Object)this) == FLAT ? new FlatChunkGenerator(world, world.getSeed(), world.getLevelProperties().hasStructures()) : new SurfaceChunkGenerator(world, world.getSeed(), world.getLevelProperties().hasStructures()));
    }

    @Override
    public int getSeaLevel(World world) {
        return ((LevelGeneratorType)(Object)this) != FLAT ? 64 : 4;
    }

    @Override
    public boolean hasVoidParticles(boolean flag) {
        return ((LevelGeneratorType)(Object)this) != FLAT && !flag;
    }

    @Override
    public double voidFadeMagnitude() {
        return ((LevelGeneratorType)(Object)this) != FLAT ? 0.03125 : 1.0;
    }
}
