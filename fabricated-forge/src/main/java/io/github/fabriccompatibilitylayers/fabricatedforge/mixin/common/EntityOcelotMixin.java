/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityExtension;
import net.minecraft.src.Block;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.EntityTameable;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityOcelot.class)
public abstract class EntityOcelotMixin extends EntityTameable implements EntityExtension {
    public EntityOcelotMixin(World par1World) {
        super(par1World);
    }

    @Definition(id = "leaves", field = "Lnet/minecraft/src/Block;leaves:Lnet/minecraft/src/BlockLeaves;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/BlockLeaves;blockID:I")
    @Definition(id = "var4", local = @Local(ordinal = 3, type = int.class))
    @Expression("var4 == leaves.blockID")
    @WrapOperation(method = "getCanSpawnHere", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isLeaves(int var4, int right, Operation<Boolean> original,
                                   @Local(ordinal = 0) int var1,
                                   @Local(ordinal = 1) int var2,
                                   @Local(ordinal = 2) int var3) {
        Block block = Block.blocksList[var4];
        return block != null && ((BlockExtension) block).isLeaves(worldObj, var1, var2 - 1, var3);
    }
}
