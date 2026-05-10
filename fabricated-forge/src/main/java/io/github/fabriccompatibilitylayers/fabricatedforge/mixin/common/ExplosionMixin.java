package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.Explosion;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow private World worldObj;
    @Shadow public double explosionX;
    @Shadow public double explosionY;
    @Shadow public double explosionZ;

    @WrapOperation(
        method = "doExplosionA",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;getExplosionResistance(Lnet/minecraft/src/Entity;)F")
    )
    private float forge$getExplosionResistance(Block block, Entity exploder, Operation<Float> original,
                                               @Local(index = 22) int x,
                                               @Local(index = 23) int y,
                                               @Local(index = 24) int z) {
        return ((BlockExtension) block).getExplosionResistance(exploder, worldObj, x, y, z, explosionX, explosionY, explosionZ);
    }
}