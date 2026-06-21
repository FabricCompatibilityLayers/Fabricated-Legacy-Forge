/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.client.EffectRendererExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.client.RenderGlobalExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldProviderExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftforge.client.SkyProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin implements RenderGlobalExtension {

    @Shadow public Minecraft mc;
    @Shadow public WorldClient theWorld;
    @Shadow @Final public RenderEngine renderEngine;

    // Pattern P (@WrapMethod): wraps whole method to conditionally delegate sky rendering to a mod-
    // registered SkyProvider before running any vanilla sky logic. Preferred over @Inject(HEAD,
    // cancellable) because the conditional-suppress intent is explicit and composes with other injectors.
    @WrapMethod(method = "renderSky")
    private void forge$renderSkyWithProvider(float par1, Operation<Void> original) {
        SkyProvider skyProvider = ((WorldProviderExtension) this.mc.theWorld.provider).getSkyProvider();
        if (skyProvider != null) {
            skyProvider.render(par1, this.theWorld, this.mc);
            return;
        }
        original.call(par1);
    }

    // Pattern N (@Redirect ordinal 2): intercepts only the third addEffect call in func_72726_b —
    // the one inside "if (var21 != null)" at the bottom of the else-chain. Ordinals 0 and 1 (hugeexplosion
    // and largeexplode) are left alone. effectObject is re-derived from par1Str rather than tracked via
    // @Share, since par1Str is still in scope and the mapping is a pure function of the particle name.
    // Logic delta: original patch stored effectObject as a local across multiple branches; here the same
    // value is recomputed at the single call site, which is semantically identical.
    @Redirect(
        method = "func_72726_b(Ljava/lang/String;DDDDDD)Lnet/minecraft/src/EntityFX;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/EffectRenderer;addEffect(Lnet/minecraft/src/EntityFX;)V",
            ordinal = 2
        )
    )
    private void forge$addEffectWithObject(EffectRenderer instance, EntityFX fx,
            @Local(argsOnly = true) String par1Str) {
        Object effectObject = null;
        if (par1Str.equals("snowballpoof")) {
            effectObject = Item.snowball;
        } else if (par1Str.equals("slime")) {
            effectObject = Item.slimeBall;
        } else if (par1Str.startsWith("iconcrack_")) {
            int idx = Integer.parseInt(par1Str.substring(par1Str.indexOf("_") + 1));
            effectObject = Item.itemsList[idx];
        } else if (par1Str.startsWith("tilecrack_")) {
            int idx = Integer.parseInt(par1Str.substring(par1Str.indexOf("_") + 1));
            effectObject = Block.blocksList[idx];
        }
        ((EffectRendererExtension) instance).addEffect(fx, effectObject);
    }

}