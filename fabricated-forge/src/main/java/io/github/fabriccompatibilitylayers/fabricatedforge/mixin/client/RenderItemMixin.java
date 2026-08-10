/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.src.*;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin extends Render {

    @Shadow private RenderBlocks renderBlocks;
    @Shadow private Random random;
    @Shadow public boolean field_77024_a;

    @Shadow public float zLevel;

    @Definition(id = "blocksList", field = "Lnet/minecraft/src/Block;blocksList:[Lnet/minecraft/src/Block;")
    @Expression("blocksList[?]")
    @WrapOperation(method = "doRenderItem", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Block forge$fixOOB(Block[] array, int index, Operation<Block> original, @Local ItemStack var10) {
        if (var10.getItem() instanceof ItemBlock)
            return original.call(array, index);
        return null;
    }

    @Expression("? != null")
    @WrapOperation(method = "doRenderItem", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$customRendererAndIsItemBlock(Object left, Object right, Operation<Boolean> original,
                                                       @Local(argsOnly = true) EntityItem par1EntityItem,
                                                       @Local ItemStack var10,
                                                       @Local(ordinal = 2) float var11,
                                                       @Local(ordinal = 3) float var12,
                                                       @Cancellable CallbackInfo ci) {
        if (ForgeHooksClient.renderEntityItem(par1EntityItem, var10, var11, var12, random, renderManager.renderEngine, renderBlocks)) {
            GL11.glDisable(32826);
            GL11.glPopMatrix();
            ci.cancel();
            return false;
        }

        return original.call(left, right);
    }

    @WrapOperation(method = "doRenderItem", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;loadTexture(Ljava/lang/String;)V", ordinal = 0)
    })
    private void forge$getTextureFile$block(RenderItem instance, String s, Operation<Void> original,
                                            @Local Block var14) {
        original.call(instance, ((BlockExtension) var14).getTextureFile());
    }

    @WrapOperation(method = "doRenderItem", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;loadTexture(Ljava/lang/String;)V", ordinal = 1),
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;loadTexture(Ljava/lang/String;)V", ordinal = 2),
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;loadTexture(Ljava/lang/String;)V", ordinal = 3)
    })
    private void forge$getTextureFile$item(RenderItem instance, String s, Operation<Void> original,
                                            @Local ItemStack var10) {
        original.call(instance, ((ItemExtension) var10.getItem()).getTextureFile());
    }

    @Expression("? <= 1")
    @WrapOperation(method = "doRenderItem", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;loadTexture(Ljava/lang/String;)V", ordinal = 1)
    ))
    private boolean forge$getRenderPasses$1(int left, int right, Operation<Boolean> original,
                                          @Local ItemStack var10) {
        return original.call(left, ((ItemExtension) var10.getItem()).getRenderPasses(var10.getItemDamage()) - 1);
    }

    @Inject(method = "doRenderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Item;getIconFromDamageForRenderPass(II)I"))
    private void forge$fixVanillaBug(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9, CallbackInfo ci) {
        this.random.setSeed(187L); //Fixes Vanilla bug where layers would not render aligns properly.
    }

    @Expression("? < 256")
    @WrapOperation(method = "renderItemIntoGUI", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$isBlockItem(int left, int right, Operation<Boolean> original,
                                      @Local(argsOnly = true) ItemStack stack) {
        return stack.getItem() instanceof ItemBlock;
    }

    @WrapOperation(method = "renderItemIntoGUI", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 0)
    })
    private int forge$getTextureFile$block(RenderEngine instance, String s, Operation<Integer> original,
                                           @Local(ordinal = 2) int var6) {
        return original.call(instance, ((BlockExtension) Block.blocksList[var6]).getTextureFile());
    }

    @WrapOperation(method = "renderItemIntoGUI", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 1),
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 2),
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 3)
    })
    private int forge$getTextureFile$item(RenderEngine instance, String s, Operation<Integer> original,
                                           @Local(ordinal = 2) int var6) {
        return original.call(instance, ((ItemExtension) Item.itemsList[var6]).getTextureFile());
    }

    @Expression("? <= 1")
    @WrapOperation(method = "renderItemIntoGUI", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;bindTexture(I)V", ordinal = 1)
    ))
    private boolean forge$getRenderPasses$2(int left, int right, Operation<Boolean> original,
                                            @Local(argsOnly = true) ItemStack par3ItemStack) {
        return original.call(left, ((ItemExtension) par3ItemStack.getItem()).getRenderPasses(par3ItemStack.getItemDamage()) - 1);
    }

    @WrapWithCondition(
        method = "func_82406_b",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;renderItemIntoGUI(Lnet/minecraft/src/FontRenderer;Lnet/minecraft/src/RenderEngine;Lnet/minecraft/src/ItemStack;II)V")
    )
    private boolean forge$renderInventoryItem(RenderItem instance, FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
        return !ForgeHooksClient.renderInventoryItem(renderBlocks, par2RenderEngine, par3ItemStack, field_77024_a, zLevel, (float)par4, (float)par5);
    }
}