package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.BlockChest;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockChest.class)
public class BlockChestMixin {
    @Redirect(method = "onBlockActivated", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;isBlockNormalCube(III)Z"))
    private boolean forge$isBlockSolidOnSide(World instance, int par2, int par3, int i) {
        return instance.isBlockSolidOnSide(par2, par3, i, ForgeDirection.DOWN);
    }
}
