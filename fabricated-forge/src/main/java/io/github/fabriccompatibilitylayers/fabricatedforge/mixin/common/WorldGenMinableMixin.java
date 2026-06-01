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
import fr.catcore.cursedmixinextensions.annotations.NewConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowConstructor;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenMinable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldGenMinable.class)
public abstract class WorldGenMinableMixin {
    private int minableBlockMeta = 0;

    @ShadowConstructor
    public abstract void constructor(int id, int number);

    @NewConstructor
    public void constructor(int id, int meta, int number)
    {
        constructor(id, number);
        minableBlockMeta = meta;
    }

    @Definition(id = "stone", field = "Lnet/minecraft/src/Block;stone:Lnet/minecraft/src/Block;")
    @Definition(id = "blockID", field = "Lnet/minecraft/src/Block;blockID:I")
    @Expression("? == stone.blockID")
    @WrapOperation(method = "generate", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isGenMineableReplaceable(int blockId, int right, Operation<Boolean> original,
                                                   @Local World par1World,
                                                   @Local(index = 38) int var38,
                                                   @Local(index = 41) int var41,
                                                   @Local(index = 44) int var44) {
        Block block = Block.blocksList[blockId];

        return block != null && ((BlockExtension) block).isGenMineableReplaceable(par1World, var38, var41, var44);
    }

    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;setBlock(IIII)Z"))
    private boolean forge$setBlockAndMetadata(World instance, int par2, int par3, int par4, int i) {
        return instance.setBlockAndMetadata(par2, par3, par4, i, minableBlockMeta);
    }
}
