package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCactus;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockCactus.class)
public abstract class BlockCactusMixin extends Block implements BlockExtension, IPlantable {
    public BlockCactusMixin(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @ModifyReturnValue(method = "canBlockStay", at = @At(value = "RETURN", ordinal = 4))
    private boolean forge$canSustainPlant(boolean original, @Local(argsOnly = true) World par1World, @Local(argsOnly = true, ordinal = 0) int par2, @Local(argsOnly = true, ordinal = 1) int par3, @Local(argsOnly = true, ordinal = 2) int par4, @Local(ordinal = 3) int var5) {
        return (blocksList[var5] != null && ((BlockExtension) blocksList[var5]).canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this)) || original;
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z)
    {
        return EnumPlantType.Desert;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z)
    {
        return blockID;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z)
    {
        return -1;
    }
}
