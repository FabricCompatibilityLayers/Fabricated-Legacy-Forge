/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderPlayer;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin {

    // Pattern E (@WrapOperation INVOKE): intercepts the loadTexture call-site inside
    // setArmorModel to route the path through ForgeHooksClient.getArmorTexture — 1:1 translation.
    @WrapOperation(
        method = {"setArmorModel", "func_82439_b"},
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderPlayer;loadTexture(Ljava/lang/String;)V")
    )
    private void forge$getArmorTexture(RenderPlayer instance, String path, Operation<Void> original,
                                       @Local ItemStack var4) {
        original.call(instance, ForgeHooksClient.getArmorTexture(var4, path));
    }

    @WrapOperation(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemStack;getItem()Lnet/minecraft/src/Item;", ordinal = 0))
    private Item forge$nullFix(ItemStack instance, Operation<Item> original) {
        return instance == null ? null : original.call(instance);
    }

    @WrapOperation(method = "renderSpecials", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Item;shiftedIndex:I", ordinal = 0, opcode = Opcodes.GETFIELD))
    private int forge$nullFix(Item instance, Operation<Integer> original) {
        return instance == null ? 0 : original.call(instance);
    }

    // Pattern E (@WrapOperation GETFIELD, ordinal = 0): intercepts the first Item.shiftedIndex
    // read in renderSpecials to replace `shiftedIndex < 256` with `instanceof ItemBlock` —
    // same technique as RenderBipedMixin. Receiver is the Item from var3.getItem().
    @Definition(id = "shiftedIndex", field = "Lnet/minecraft/src/Item;shiftedIndex:I")
    @Expression("?.shiftedIndex < 256")
    @WrapOperation(
            method = "renderSpecials",
            at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0)
    )
    private boolean forge$instanceOfItemBlockCheckHelmet(int left, int right, Operation<Boolean> original,
                                                         @Local ItemStack var4) {
        return var4 != null && var4.getItem() instanceof ItemBlock;
    }

    // Pattern E (@WrapOperation INVOKE, ordinal = 0): wraps the first renderItemIn3d call
    // (helmet block, var3) to OR-combine with a Forge custom-renderer is3D check — 1:1 translation.
    @WrapOperation(
        method = "renderSpecials",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBlocks;renderItemIn3d(I)Z",
                 ordinal = 0)
    )
    private boolean forge$wrapRenderItemIn3dHelmet(int renderType, Operation<Boolean> original,
            @Local ItemStack var4) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(
            var4, IItemRenderer.ItemRenderType.EQUIPPED);
        boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(
            IItemRenderer.ItemRenderType.EQUIPPED, var4, IItemRenderer.ItemRendererHelper.BLOCK_3D);
        return is3D || original.call(renderType);
    }

    // Pattern E (@WrapOperation GETFIELD, ordinal = 1): intercepts the second ItemStack.itemID
    // read in renderSpecials (the `var21.itemID < 256` guard) to replace it with `instanceof
    // ItemBlock` — ordinal 0 is var3.itemID inside the helmet renderItemIn3d call (line 123).
    @WrapOperation(
        method = "renderSpecials",
        at = @At(value = "FIELD", target = "Lnet/minecraft/src/ItemStack;itemID:I",
                 opcode = Opcodes.GETFIELD, ordinal = 1)
    )
    private int forge$instanceOfItemBlockCheckEquipped(ItemStack itemStack, Operation<Integer> original) {
        return (itemStack.getItem() instanceof ItemBlock) ? 1 : Integer.MAX_VALUE;
    }

    // Pattern E (@WrapOperation INVOKE, ordinal = 1): wraps the second renderItemIn3d call
    // (equipped item, var21) to OR-combine with a Forge custom-renderer is3D check — 1:1 translation.
    @WrapOperation(
        method = "renderSpecials",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBlocks;renderItemIn3d(I)Z",
                 ordinal = 1)
    )
    private boolean forge$wrapRenderItemIn3dEquipped(int renderType, Operation<Boolean> original,
            @Local(ordinal = 1) ItemStack var21) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(
            var21, IItemRenderer.ItemRenderType.EQUIPPED);
        boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(
            IItemRenderer.ItemRenderType.EQUIPPED, var21, IItemRenderer.ItemRendererHelper.BLOCK_3D);
        return is3D || original.call(renderType);
    }

    @Expression("? <= 1")
    @WrapOperation(method = "renderSpecials",
            slice = @Slice(
                    from = @At(value = "INVOKE",
                            target = "Lnet/minecraft/src/Item;requiresMultipleRenderPasses()Z")
            ),
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean forge$getRenderPasses(int left, int right, Operation<Boolean> original,
                                          @Local(ordinal = 1) ItemStack var21) {
        return original.call(left, ((ItemExtension) var21.getItem()).getRenderPasses(var21.getItemDamage()) - 1);
    }
}