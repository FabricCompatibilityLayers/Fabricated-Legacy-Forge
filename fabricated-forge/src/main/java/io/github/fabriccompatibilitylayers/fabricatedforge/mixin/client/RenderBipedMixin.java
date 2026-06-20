/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;

@Mixin(RenderBiped.class)
public abstract class RenderBipedMixin {
    @WrapOperation(method = "shouldRenderPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBiped;loadTexture(Ljava/lang/String;)V"))
    private void forge$getArmorTexture(RenderBiped instance, String s, Operation<Void> original,
                                       @Local(ordinal = 0) ItemStack var4) {
        original.call(instance, ForgeHooksClient.getArmorTexture(var4, s));
    }

    @Expression("? < 256")
    @WrapOperation(method = "renderEquippedItems", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$isItemBlock(int left, int right, Operation<Boolean> original,
                                      @Local(ordinal = 1) ItemStack var5) {
        return var5.getItem() instanceof ItemBlock;
    }

    @Expression("? < 256")
    @WrapOperation(method = "renderEquippedItems", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean forge$isItemBlock(int left, int right, Operation<Boolean> original) {
        return Item.itemsList[left] instanceof ItemBlock;
    }

    @WrapOperation(method = "renderEquippedItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBlocks;renderItemIn3d(I)Z", ordinal = 0))
    private boolean forge$renderItemIn3d(int renderType, Operation<Boolean> original,
                                         @Local(ordinal = 1) ItemStack var5) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(var5, EQUIPPED);
        boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, var5, BLOCK_3D));
        return is3D || original.call(renderType);
    }

    // Pattern E (@WrapOperation INVOKE): wraps the static renderItemIn3d call to prepend the
    // Forge custom-renderer is3D check — OR-combines with the original result.
    @WrapOperation(
        method = "renderEquippedItems",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBlocks;renderItemIn3d(I)Z", ordinal = 1)
    )
    private boolean forge$wrapRenderItemIn3d(int renderType, Operation<Boolean> original,
            @Local(ordinal = 0) ItemStack var4) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(
            var4, EQUIPPED);
        boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(
            EQUIPPED, var4, BLOCK_3D);
        return is3D || original.call(renderType);
    }

    // Pattern E (@WrapOperation INVOKE, ordinal = 1): replaces the hard-coded single renderItem(…, 1)
    // call with a loop over getRenderPasses() — ordinal 1 targets only the multi-pass call,
    // leaving the always-rendered pass-0 call (ordinal 0) untouched.
    @WrapOperation(
        method = "renderEquippedItems",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/src/ItemRenderer;renderItem(Lnet/minecraft/src/EntityLiving;Lnet/minecraft/src/ItemStack;I)V",
                 ordinal = 2)
    )
    private void forge$wrapRenderItemMultiPass(ItemRenderer receiver, EntityLiving par1EntityLiving,
            ItemStack itemStack, int pass, Operation<Void> original) {
        for (int x = 1; x < ((ItemExtension) itemStack.getItem()).getRenderPasses(itemStack.getItemDamage()); x++) {
            original.call(receiver, par1EntityLiving, itemStack, x);
        }
    }
}