/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.client.osl.items.forge;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraftforge.client.ForgeHooksClient;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import net.ornithemc.osl.blocks.api.BlockRegistry;
import net.ornithemc.osl.items.api.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {
    @Definition(id = "blocksList", field = "Lnet/minecraft/src/Block;blocksList:[Lnet/minecraft/src/Block;")
    @Expression("blocksList[?]")
    @WrapOperation(method = "renderEntityItem", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static Block osl$fixBlockItemEntity(Block[] array, int index, Operation<Block> original) {
        return BlockRegistry.getBlock(((ItemBlock) ItemRegistry.getItem(index)).getBlockID());
    }
}
