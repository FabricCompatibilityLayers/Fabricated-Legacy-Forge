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
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.client.RenderGlobalExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Shadow private Minecraft mc;
    @Shadow private float fovMultiplierTemp;
    @Shadow private float fovModifierHandPrev;
    @Shadow private float fovModifierHand;

    /**
     * @author FabricCompatibilityLayers
     * @reason Guards the EntityPlayerSP cast with instanceof; falls back to mc.thePlayer when
     *         renderViewEntity is not an EntityPlayerSP, allowing non-player view entities.
     */
    @Overwrite
    private void updateFovModifierHand() {
        if (this.mc.renderViewEntity instanceof EntityPlayerSP) {
            EntityPlayerSP var1 = (EntityPlayerSP) this.mc.renderViewEntity;
            this.fovMultiplierTemp = var1.getFOVMultiplier();
        } else {
            this.fovMultiplierTemp = this.mc.thePlayer.getFOVMultiplier();
        }
        this.fovModifierHandPrev = this.fovModifierHand;
        this.fovModifierHand += (this.fovMultiplierTemp - this.fovModifierHand) * 0.5F;
    }

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldClient;getBlockId(III)I"))
    private int forge$orientBedCamera(WorldClient instance, int i, int j, int k,
                                      @Local EntityLiving var2) {
        ForgeHooksClient.orientBedCamera(mc, var2);
        return 0;
    }

    @WrapWithCondition(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderGlobal;drawBlockBreaking(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/MovingObjectPosition;ILnet/minecraft/src/ItemStack;F)V"))
    private boolean forge$onDrawBlockHighlight$check1(RenderGlobal instance, EntityPlayer par2MovingObjectPosition, MovingObjectPosition par3, int par4ItemStack, ItemStack par5, float v,
                                                      @Share(namespace = "forge", value = "onDrawBlockHighlightCheck")LocalBooleanRef localB) {
        boolean result = !ForgeHooksClient.onDrawBlockHighlight(instance, par2MovingObjectPosition, par3, par4ItemStack, par5, v);
        localB.set(result);
        return result;
    }

    @WrapWithCondition(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderGlobal;drawSelectionBox(Lnet/minecraft/src/EntityPlayer;Lnet/minecraft/src/MovingObjectPosition;ILnet/minecraft/src/ItemStack;F)V"))
    private boolean forge$onDrawBlockHighlight$check2(RenderGlobal instance, EntityPlayer par2MovingObjectPosition, MovingObjectPosition par3, int par4ItemStack, ItemStack par5, float v,
                                                      @Share(namespace = "forge", value = "onDrawBlockHighlightCheck")LocalBooleanRef localB) {
        return localB.get();
    }

    @Definition(id = "EntityPlayer", type = EntityPlayer.class)
    @Expression("(EntityPlayer) ?")
    @WrapOperation(method = "renderWorld", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 2))
    private EntityPlayer forge$cancelCasting(Object object, Operation<EntityPlayer> original) {
        return null;
    }

    @Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderGlobal;drawBlockDamageTexture(Lnet/minecraft/src/Tessellator;Lnet/minecraft/src/EntityPlayer;F)V"))
    private void forge$drawBlockDamageTexture(RenderGlobal instance, Tessellator par2EntityPlayer, EntityPlayer par3, float v,
                                              @Local EntityLiving var4) {
        ((RenderGlobalExtension) instance).drawBlockDamageTexture(par2EntityPlayer, var4, v);
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 17))
    private void forge$dispatchRenderLast(float par1, long par2, CallbackInfo ci,
                                          @Local RenderGlobal var5) {
        this.mc.mcProfiler.endStartSection("FRenderLast");
        ForgeHooksClient.dispatchRenderLast(var5, par1);
    }
}