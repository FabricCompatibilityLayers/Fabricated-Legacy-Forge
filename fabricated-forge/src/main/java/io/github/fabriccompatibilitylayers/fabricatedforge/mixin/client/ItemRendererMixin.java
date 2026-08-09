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
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class, priority = 1010)
public abstract class ItemRendererMixin {

    @Shadow private Minecraft mc;
    @Shadow private RenderBlocks renderBlocksInstance;

    @Expression("? != null")
    @WrapOperation(method = "renderItem", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    private boolean forge$customRenderAndIsBlockItem(Object left, Object right, Operation<Boolean> original,
                                                     @Local(argsOnly = true) EntityLiving par1EntityLiving,
                                                     @Local(argsOnly = true) ItemStack par2ItemStack,
                                                     @Cancellable CallbackInfo ci) {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(par2ItemStack, IItemRenderer.ItemRenderType.EQUIPPED);

        if (customRenderer != null) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(((ItemExtension) par2ItemStack.getItem()).getTextureFile()));
            ForgeHooksClient.renderEquippedItem(customRenderer, renderBlocksInstance, par1EntityLiving, par2ItemStack);
            GL11.glPopMatrix();
            ci.cancel();
            return false;
        }

        return par2ItemStack.getItem() instanceof ItemBlock;
    }

    @WrapOperation(method = "renderItem", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 0),
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 1),
            @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 2)
    })
    private int forge$getTextureFile(RenderEngine instance, String s, Operation<Integer> original,
                                     @Local(argsOnly = true) ItemStack par2ItemStack) {
        return original.call(instance, ((ItemExtension) par2ItemStack.getItem()).getTextureFile());
    }

    @Definition(id = "itemID", field = "Lnet/minecraft/src/ItemStack;itemID:I")
    @Definition(id = "map", field = "Lnet/minecraft/src/Item;map:Lnet/minecraft/src/ItemMap;")
    @Definition(id = "shiftedIndex", field = "Lnet/minecraft/src/ItemMap;shiftedIndex:I")
    @Expression("?.itemID == map.shiftedIndex")
    @WrapOperation(method = "renderItemInFirstPerson", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isMap(int left, int right, Operation<Boolean> original,
                                @Local ItemStack var17,
                                @Share(namespace = "forge", value = "custom") LocalRef<IItemRenderer> ref) {
        boolean result = var17.getItem() instanceof ItemMap;

        if (result) {
            ref.set(MinecraftForgeClient.getItemRenderer(var17, IItemRenderer.ItemRenderType.FIRST_PERSON_MAP));
        }

        return result;
    }

    @WrapOperation(method = "renderItemInFirstPerson", at = @At(value = "FIELD", target = "Lnet/minecraft/src/Item;map:Lnet/minecraft/src/ItemMap;", ordinal = 1, opcode = Opcodes.GETSTATIC))
    private ItemMap forge$allowCustomMap(Operation<ItemMap> original,
                                         @Local ItemStack var17) {
        return (ItemMap) var17.getItem();
    }

    @WrapOperation(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemMap;getMapData(Lnet/minecraft/src/ItemStack;Lnet/minecraft/src/World;)Lnet/minecraft/src/MapData;"))
    private MapData forge$renderCustomMap(ItemMap instance, ItemStack stack, World world, Operation<MapData> original,
                                          @Share(namespace = "forge", value = "custom") LocalRef<IItemRenderer> ref) {
        IItemRenderer custom = ref.get();
        MapData mapData = original.call(instance, stack, world);

        if (custom != null) {
            custom.renderItem(IItemRenderer.ItemRenderType.FIRST_PERSON_MAP, stack, this.mc.thePlayer, this.mc.renderEngine, mapData);
            return null;
        }

        return mapData;
    }

    @WrapOperation(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemRenderer;renderItem(Lnet/minecraft/src/EntityLiving;Lnet/minecraft/src/ItemStack;I)V", ordinal = 1))
    private void forge$renderAllPasses(ItemRenderer instance, EntityLiving par2ItemStack, ItemStack var17, int i, Operation<Void> original,
                                       @Local(ordinal = 5) float var18) {
        original.call(instance, par2ItemStack, var17, i);

        for (int x = i + 1; x < ((ItemExtension) var17.getItem()).getRenderPasses(var17.getItemDamage()); x++) {
            int var59 = Item.itemsList[var17.itemID].func_82790_a(var17, x);
            float var63 = (float)(var59 >> 16 & 255) / 255.0F;
            float var68 = (float)(var59 >> 8 & 255) / 255.0F;
            float var70 = (float)(var59 & 255) / 255.0F;
            GL11.glColor4f(var18 * var63, var18 * var68, var18 * var70, 1.0F);
            original.call(instance, par2ItemStack, var17, x);
        }
    }
}