/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityDragon.class)
public class EntityDragonMixin extends EntityDragonBase {
    public EntityDragonMixin(World par1World) {
        super(par1World);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private boolean destroyBlocksInAABB(AxisAlignedBB par1AxisAlignedBB) {
        int var2 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int var3 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int var4 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX);
        int var6 = MathHelper.floor_double(par1AxisAlignedBB.maxY);
        int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxZ);
        boolean var8 = false;
        boolean var9 = false;

        for (int var10 = var2; var10 <= var5; var10++) {
            for (int var11 = var3; var11 <= var6; var11++) {
                for (int var12 = var4; var12 <= var7; var12++) {
                    int var13 = this.worldObj.getBlockId(var10, var11, var12);
                    Block block = Block.blocksList[var13];

                    if (block != null)
                    {
                        if (((BlockExtension) block).canDragonDestroy(worldObj, var10, var11, var12)) {
                            var9 = true;
                            this.worldObj.setBlockWithNotify(var10, var11, var12, 0);
                        } else {
                            var8 = true;
                        }
                    }
                }
            }
        }

        if (var9) {
            double var16 = par1AxisAlignedBB.minX + (par1AxisAlignedBB.maxX - par1AxisAlignedBB.minX) * this.rand.nextFloat();
            double var17 = par1AxisAlignedBB.minY + (par1AxisAlignedBB.maxY - par1AxisAlignedBB.minY) * this.rand.nextFloat();
            double var14 = par1AxisAlignedBB.minZ + (par1AxisAlignedBB.maxZ - par1AxisAlignedBB.minZ) * this.rand.nextFloat();
            this.worldObj.spawnParticle("largeexplode", var16, var17, var14, 0.0, 0.0, 0.0);
        }

        return var8;
    }
}
