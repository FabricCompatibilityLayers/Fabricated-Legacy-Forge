package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderManager.class)
public class RenderManagerMixin {

    // Pattern E (@WrapOperation) + H (@Share), Step 1:
    // Intercepts getBlockId to evaluate block.isBed() in place of the vanilla "var7 == Block.bed.blockID"
    // comparison. Returns Block.bed.blockID only when isBed() passes so the existing if-branch acts as
    // a proxy for the Forge check. Stores the block in @Share for step 2.
    @WrapOperation(
        method = "cacheActiveRenderInfo",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockId(III)I")
    )
    private int forge$isBed(World world, int x, int y, int z, Operation<Integer> original,
                            @Local(argsOnly = true) EntityLiving par4EntityLiving,
                            @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef) {
        int blockId = original.call(world, x, y, z);
        Block block = Block.blocksList[blockId];

        if (block != null && ((BlockExtension) block).isBed(world, x, y, z, par4EntityLiving)) {
            blockRef.set(block);
            return Block.bed.blockID;
        }

        return 0;
    }

    @Definition(id = "playerViewY", field = "Lnet/minecraft/src/RenderManager;playerViewY:F")
    @Expression("?.playerViewY = ?")
    @WrapOperation(
            method = "cacheActiveRenderInfo",
            at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0)
    )
    private void forge$getBedDirection(RenderManager instance, float value, Operation<Void> original,
                                       @Share(value = "block", namespace = "fabricated-forge") LocalRef<Block> blockRef,
                                       @Local(argsOnly = true) World par1World,
                                       @Local(argsOnly = true) EntityLiving par4EntityLiving) {
        int x = MathHelper.floor_double(par4EntityLiving.posX);
        int y = MathHelper.floor_double(par4EntityLiving.posY);
        int z = MathHelper.floor_double(par4EntityLiving.posZ);

        Block block = blockRef.get();

        if (block != null) {
            original.call(instance, (float)((BlockExtension) block).getBedDirection(par1World, x, y, z) * 90 + 100);
            return;
        }

        original.call(instance, value);
    }
}