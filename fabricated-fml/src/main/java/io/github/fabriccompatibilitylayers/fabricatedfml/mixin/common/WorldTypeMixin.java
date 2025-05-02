package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import fr.catcore.cursedmixinextensions.annotations.Public;
import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.WorldTypeExtension;
import io.github.fabriccompatibilitylayers.fabricatedfml.forged.ForgedWorldType;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

@Mixin(WorldType.class)
public class WorldTypeMixin implements WorldTypeExtension {
    @Shadow @Final public static WorldType field_77138_c;

    protected BiomeGenBase[] biomesForWorldType;

    @Inject(method = "<init>(ILjava/lang/String;I)V", at = @At("RETURN"))
    private void fml$biomesForWorldType(int p_i3738_1_, String p_i3738_2_, int p_i3738_3_, CallbackInfo ci) {
        switch (p_i3738_1_)
        {
            case 8:
                biomesForWorldType = ForgedWorldType.base11Biomes;
                break;
            default:
                biomesForWorldType = ForgedWorldType.base12Biomes;
        }
    }

    @Override
    public WorldChunkManager getChunkManager(World world)
    {
        return (Object) this == field_77138_c ? new WorldChunkManagerHell(BiomeGenBase.field_76772_c, 0.5F, 0.5F) : new WorldChunkManager(world);
    }

    @Override
    public IChunkProvider getChunkGenerator(World world)
    {
        return ((Object) this == field_77138_c ? new ChunkProviderFlat(world, world.func_72905_C(), world.func_72912_H().func_76089_r()) : new ChunkProviderGenerate(world, world.func_72905_C(), world.func_72912_H().func_76089_r()));
    }

    @Override
    public int getMinimumSpawnHeight(World world)
    {
        return (Object) this == field_77138_c ? 4 : 64;
    }

    @Override
    public double getHorizon(World world)
    {
        return (Object) this == field_77138_c ? 0.0D : 63.0D;
    }

    @Override
    public boolean hasVoidParticles(boolean var1)
    {
        return (Object) this != field_77138_c && !var1;
    }

    @Override
    public double voidFadeMagnitude()
    {
        return (Object) this == field_77138_c ? 1.0D : 0.03125D;
    }

    @Override
    public BiomeGenBase[] getBiomesForWorldType() {
        return biomesForWorldType;
    }

    @Override
    public void addNewBiome(BiomeGenBase biome)
    {
        Set<BiomeGenBase> newBiomesForWorld = Sets.newLinkedHashSet(Arrays.asList(biomesForWorldType));
        newBiomesForWorld.add(biome);
        biomesForWorldType = newBiomesForWorld.toArray(new BiomeGenBase[0]);
    }

    @Override
    public void removeBiome(BiomeGenBase biome)
    {
        Set<BiomeGenBase> newBiomesForWorld = Sets.newLinkedHashSet(Arrays.asList(biomesForWorldType));
        newBiomesForWorld.remove(biome);
        biomesForWorldType = newBiomesForWorld.toArray(new BiomeGenBase[0]);
    }

    @Override
    public boolean handleSlimeSpawnReduction(Random random, World world)
    {
        return (Object) this == field_77138_c ? random.nextInt(4) != 1 : false;
    }
    /**
     * Called when 'Create New World' button is pressed before starting game
     */
    @Override
    public void onGUICreateWorldPress() { }
}
