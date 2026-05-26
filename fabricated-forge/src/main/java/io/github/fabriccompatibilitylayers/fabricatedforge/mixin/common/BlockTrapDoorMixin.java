package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.cursedmixinextensions.annotations.Public;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockTrapDoor;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockTrapDoor.class)
public abstract class BlockTrapDoorMixin extends Block implements BlockExtension {
    public BlockTrapDoorMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    /** Set this to allow trapdoors to remain free-floating */
    @Public
    private static boolean disableValidation = false;

    @WrapOperation(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockTrapDoor;isValidSupportBlock(I)Z"))
    private boolean forge$isBlockSolidOnSide$onNeighborBlockChange(int par0, Operation<Boolean> original,
                                             @Local(argsOnly = true) World par1World,
                                             @Local(ordinal = 1, argsOnly = true) int par3,
                                             @Local(ordinal = 4) int var6,
                                             @Local(ordinal = 5) int var7,
                                             @Local(ordinal = 6) int var8) {
        return !(original.call(par0) || ((WorldExtension) par1World).isBlockSolidOnSide(var7, par3, var8, ForgeDirection.getOrientation((var6 & 3) + 2)));
    }

    @Inject(method = "canPlaceBlockOnSide", at = @At("HEAD"), cancellable = true)
    private void forge$disableValidation(World par2, int par3, int par4, int par5, int par6, CallbackInfoReturnable<Boolean> cir) {
        if (disableValidation) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isValidSupportBlock", at = @At("HEAD"), cancellable = true)
    private static void forge$disableValidation(int par0, CallbackInfoReturnable<Boolean> cir) {
        if (disableValidation) {
            cir.setReturnValue(true);
        }
    }

    @WrapOperation(method = "canPlaceBlockOnSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/BlockTrapDoor;isValidSupportBlock(I)Z"))
    private boolean forge$isBlockSolidOnSide$canPlaceBlockOnSide(int par0, Operation<Boolean> original,
                                                                   @Local(argsOnly = true) World par1World,
                                                                   @Local(ordinal = 0, argsOnly = true) int par2,
                                                                   @Local(ordinal = 1, argsOnly = true) int par3,
                                                                   @Local(ordinal = 2, argsOnly = true) int par4) {
        return original.call(par0) || ((WorldExtension) par1World).isBlockSolidOnSide(par2, par3, par4, ForgeDirection.UP);
    }
}
