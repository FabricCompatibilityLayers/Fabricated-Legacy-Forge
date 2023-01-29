package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import fr.catcore.fabricatedforge.mixininterface.Iclass_50;
import net.minecraft.structure.class_50;
import net.minecraft.world.LayeredBiomeSource;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Random;

@Mixin(class_50.class)
public class class_50Mixin implements Iclass_50 {
    public Biome biome;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void getBiomeFromCtr(LayeredBiomeSource par1WorldChunkManager, int par2, Random par3Random, int par4, int par5, ArrayList par6ArrayList, int par7, CallbackInfo ci) {
        this.biome = par1WorldChunkManager.method_3853(par4, par5);
    }

    @Override
    public Biome getBiome() {
        return this.biome;
    }
}
