package fr.catcore.fabricatedforge.mixin.forgefml.world.level;

import com.google.common.collect.Sets;
import fr.catcore.fabricatedforge.mixininterface.ILevelGeneratorType;
import fr.catcore.fabricatedforge.forged.ReflectionUtils;
import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SingletonBiomeSource;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.FlatChunkGenerator;
import net.minecraft.world.chunk.SurfaceChunkGenerator;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

@Mixin(LevelGeneratorType.class)
public class LevelGeneratorTypeMixin implements ILevelGeneratorType {
    @Shadow @Final public static LevelGeneratorType FLAT;

    @Unique
    protected Biome[] biomesForWorldType;

    @Inject(method = "<init>(ILjava/lang/String;I)V", at = @At("RETURN"))
    private void fmlCtr(int par1, String par2Str, int par3, CallbackInfo ci) {
        switch (par1) {
            case 8:
                this.biomesForWorldType = ReflectionUtils.LevelGeneratorType_base11Biomes;
                break;
            default:
                this.biomesForWorldType = ReflectionUtils.LevelGeneratorType_base12Biomes;
        }
    }

    @Override
    public LayeredBiomeSource getChunkManager(World world) {
        return (LayeredBiomeSource)((Object)this == FLAT ? new SingletonBiomeSource(Biome.PLAINS, 0.5F, 0.5F) : new LayeredBiomeSource(world));
    }

    @Override
    public ChunkProvider getChunkGenerator(World world) {
        return (ChunkProvider)((Object)this == FLAT ? new FlatChunkGenerator(world, world.getSeed(), world.getLevelProperties().hasStructures()) : new SurfaceChunkGenerator(world, world.getSeed(), world.getLevelProperties().hasStructures()));
    }

    @Override
    public int getMinimumSpawnHeight(World world) {
        return (Object)this == FLAT ? 4 : 64;
    }

    @Override
    public double getHorizon(World world) {
        return (Object)this == FLAT ? 0.0 : 63.0;
    }

    @Override
    public boolean hasVoidParticles(boolean var1) {
        return (Object)this != FLAT && !var1;
    }

    @Override
    public double voidFadeMagnitude() {
        return (Object)this == FLAT ? 1.0 : 0.03125;
    }

    @Override
    public Biome[] getBiomesForWorldType() {
        return this.biomesForWorldType;
    }

    @Override
    public void addNewBiome(Biome biome) {
        Set<Biome> newBiomesForWorld = Sets.newLinkedHashSet(Arrays.asList(this.biomesForWorldType));
        newBiomesForWorld.add(biome);
        this.biomesForWorldType = (Biome[])newBiomesForWorld.toArray(new Biome[0]);
    }

    @Override
    public void removeBiome(Biome biome) {
        Set<Biome> newBiomesForWorld = Sets.newLinkedHashSet(Arrays.asList(this.biomesForWorldType));
        newBiomesForWorld.remove(biome);
        this.biomesForWorldType = (Biome[])newBiomesForWorld.toArray(new Biome[0]);
    }

    @Override
    public boolean handleSlimeSpawnReduction(Random random, World world) {
        return (Object)this == FLAT ? random.nextInt(4) != 1 : false;
    }

    @Override
    public void onGUICreateWorldPress() {
    }

    @Override
    public int getSpawnFuzz() {
        return 20;
    }
}
