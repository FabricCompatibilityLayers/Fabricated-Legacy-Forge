/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBiped;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderBiped.class)
public abstract class RenderBipedMixin {

    // Pattern E (@WrapOperation GETFIELD): intercepts the first itemID read (ordinal 0, the `< 256`
    // guard) and returns Integer.MAX_VALUE for non-ItemBlock items so the comparison fails —
    // replaces the vanilla `itemID < 256` guard with the semantically correct `instanceof ItemBlock`.
    @WrapOperation(
        method = "renderEquippedItems",
        at = @At(value = "FIELD", target = "Lnet/minecraft/src/ItemStack;itemID:I",
                 opcode = Opcodes.GETFIELD, ordinal = 0)
    )
    private int forge$instanceOfItemBlockCheck(ItemStack itemStack, Operation<Integer> original) {
        return (itemStack.getItem() instanceof ItemBlock)
            ? 1 : Integer.MAX_VALUE;
    }

    // Pattern E (@WrapOperation INVOKE): wraps the static renderItemIn3d call to prepend the
    // Forge custom-renderer is3D check — OR-combines with the original result.
    @WrapOperation(
        method = "renderEquippedItems",
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

    // Pattern E (@WrapOperation INVOKE, ordinal = 1): replaces the hard-coded single renderItem(…, 1)
    // call with a loop over getRenderPasses() — ordinal 1 targets only the multi-pass call,
    // leaving the always-rendered pass-0 call (ordinal 0) untouched.
    @WrapOperation(
        method = "renderEquippedItems",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/src/ItemRenderer;renderItem(Lnet/minecraft/src/EntityLiving;Lnet/minecraft/src/ItemStack;I)V",
                 ordinal = 1)
    )
    private void forge$wrapRenderItemMultiPass(ItemRenderer receiver, EntityLiving par1EntityLiving,
            ItemStack itemStack, int pass, Operation<Void> original) {
        for (int x = 1; x < ((ItemExtension) itemStack.getItem()).getRenderPasses(itemStack.getItemDamage()); x++) {
            original.call(receiver, par1EntityLiving, itemStack, x);
        }
    }
}