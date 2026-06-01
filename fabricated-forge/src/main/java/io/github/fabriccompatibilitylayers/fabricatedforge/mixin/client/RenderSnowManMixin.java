/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderSnowMan;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderSnowMan.class)
public abstract class RenderSnowManMixin {

    // Pattern E (@WrapOperation GETFIELD): intercepts the Item.shiftedIndex read in
    // renderSnowmanPumpkin to replace `shiftedIndex < 256` with `instanceof ItemBlock` —
    // same technique as RenderPlayerMixin.forge$instanceOfItemBlockCheckHelmet.
    @WrapOperation(
        method = "renderSnowmanPumpkin",
        at = @At(value = "FIELD", target = "Lnet/minecraft/src/Item;shiftedIndex:I",
                 opcode = Opcodes.GETFIELD, ordinal = 0)
    )
    private int forge$instanceOfItemBlockCheck(Item item, Operation<Integer> original) {
        return (item instanceof ItemBlock) ? 1 : Integer.MAX_VALUE;
    }

    // Pattern E (@WrapOperation INVOKE): wraps the renderItemIn3d call in renderSnowmanPumpkin
    // to prepend the Forge custom-renderer is3D check — OR-combines with the original result.
    // Same pattern as RenderBipedMixin.forge$wrapRenderItemIn3d.
    @WrapOperation(
        method = "renderSnowmanPumpkin",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBlocks;renderItemIn3d(I)Z")
    )
    private boolean forge$wrapRenderItemIn3d(int renderType, Operation<Boolean> original,
            @Local ItemStack var3) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(
            var3, IItemRenderer.ItemRenderType.EQUIPPED);
        boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(
            IItemRenderer.ItemRenderType.EQUIPPED, var3, IItemRenderer.ItemRendererHelper.BLOCK_3D);
        return is3D || original.call(renderType);
    }
}