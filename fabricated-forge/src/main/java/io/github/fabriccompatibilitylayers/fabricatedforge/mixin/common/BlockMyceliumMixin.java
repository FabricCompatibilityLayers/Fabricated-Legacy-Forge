package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockMycelium;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockMycelium.class)
public abstract class BlockMyceliumMixin extends Block implements BlockExtension {
    public BlockMyceliumMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Definition(id = "lightOpacity", field = "Lnet/minecraft/src/Block;lightOpacity:[I")
    @Expression("lightOpacity[?]")
    @WrapOperation(method = "updateTick", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private int forge$getBlockLightOpacity1(int[] array, int index, Operation<Integer> original, @Local(argsOnly = true) World par1World, @Local(argsOnly = true, ordinal = 0) int par2, @Local(argsOnly = true, ordinal = 1) int par3, @Local(argsOnly = true, ordinal = 2) int par4) {
        return par1World.getBlockLightOpacity(par2, par3 + 1, par4);
    }

    @Definition(id = "lightOpacity", field = "Lnet/minecraft/src/Block;lightOpacity:[I")
    @Expression("lightOpacity[?]")
    @WrapOperation(method = "updateTick", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private int forge$getBlockLightOpacity2(int[] array, int index, Operation<Integer> original, @Local(argsOnly = true) World par1World, @Local(ordinal = 4) int var7, @Local(ordinal = 5) int var8, @Local(ordinal = 6) int var9) {
        return par1World.getBlockLightOpacity(var7, var8 + 1, var9);
    }
}
