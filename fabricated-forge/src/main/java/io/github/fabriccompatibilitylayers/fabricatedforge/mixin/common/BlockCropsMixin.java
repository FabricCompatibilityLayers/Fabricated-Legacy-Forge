/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(BlockCrops.class)
public abstract class BlockCropsMixin extends BlockFlower implements BlockExtension, IPlantable {
    @Shadow
    protected abstract int func_82532_h();

    protected BlockCropsMixin(int par1, int par2, Material par3Material) {
        super(par1, par2, par3Material);
    }

    @Redirect(method = "getGrowthRate", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Block;blockID:I", ordinal = 0, opcode = Opcodes.GETFIELD))
    private int forge$canSustainPlant(Block instance,
                                      @Local(argsOnly = true) World par1World,
                                      @Local(argsOnly = true, ordinal = 1) int par3,
                                      @Local(ordinal = 11) int var17,
                                      @Local(ordinal = 12) int var18,
                                      @Local(ordinal = 13) int var19) {
        return (blocksList[var19] != null && ((BlockExtension) blocksList[var19]).canSustainPlant(par1World, var17, par3 - 1, var18, ForgeDirection.UP, this))
                ? var19 : -2;
    }

    @Redirect(method = "getGrowthRate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getBlockMetadata(III)I"))
    private int forge$isFertile(World par1World, int i, int j, int k, @Local(ordinal = 13) int var19) {
        return (((BlockExtension) blocksList[var19]).isFertile(par1World, i, j, k))
                ? 1 : 0;
    }

    @Inject(method = "dropBlockAsItemWithChance", at = @At(value = "FIELD", target = "Lnet/minecraft/src/World;isRemote:Z", opcode = Opcodes.GETFIELD), cancellable = true)
    private void forge$cancelVanilla(World par2, int par3, int par4, int par5, int par6, float par7, int par8, CallbackInfo ci) {
        ci.cancel();
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        if (metadata == 7)
        {
            int count = quantityDropped(metadata, fortune, world.rand);
            for(int i = 0; i < count; i++)
            {
                int id = idDropped(metadata, world.rand, 0);
                if (id > 0)
                {
                    ret.add(new ItemStack(id, 1, damageDropped(metadata)));
                }
            }
        }

        if (metadata >= 7)
        {
            for (int n = 0; n < 3 + fortune; n++)
            {
                if (world.rand.nextInt(15) <= metadata)
                {
                    ret.add(new ItemStack(this.func_82532_h(), 1, 0));
                }
            }
        }

        return ret;
    }
}
