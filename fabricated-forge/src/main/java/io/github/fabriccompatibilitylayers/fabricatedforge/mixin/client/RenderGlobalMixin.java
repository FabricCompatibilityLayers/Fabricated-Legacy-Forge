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
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.Map;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin implements RenderGlobalExtension {

    @Shadow public Minecraft mc;
    @Shadow public WorldClient theWorld;
    @Shadow @Final public RenderEngine renderEngine;
    @Shadow public RenderBlocks globalRenderBlocks;
    @Shadow public Map field_72738_E;

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

    // Pattern D (@Overwrite): replaces the EntityPlayer overload body entirely with a cast-and-delegate —
    // @Overwrite is appropriate here because the whole body is being replaced (not suppressed conditionally).
    /**
     * @author FabricCompatibilityLayers
     * @reason Delegates to the EntityLiving overload added by Forge for broader API compatibility
     */
    @Overwrite
    public void drawBlockDamageTexture(Tessellator par1Tessellator, EntityPlayer par2EntityPlayer, float par3) {
        this.drawBlockDamageTexture(par1Tessellator, (EntityLiving)par2EntityPlayer, par3);
    }

    // Pattern J (Extension interface): adds the EntityLiving overload to RenderGlobal so mods can
    // pass any EntityLiving (not just EntityPlayer) as the damage-texture viewer.
    // Logic delta: parameter widened from EntityPlayer → EntityLiving; body is otherwise identical.
    @Override  // → Extension interface
    public void drawBlockDamageTexture(Tessellator par1Tessellator, EntityLiving par2EntityLiving, float par3) {
        double var4 = par2EntityLiving.lastTickPosX + (par2EntityLiving.posX - par2EntityLiving.lastTickPosX) * (double)par3;
        double var6 = par2EntityLiving.lastTickPosY + (par2EntityLiving.posY - par2EntityLiving.lastTickPosY) * (double)par3;
        double var8 = par2EntityLiving.lastTickPosZ + (par2EntityLiving.posZ - par2EntityLiving.lastTickPosZ) * (double)par3;
        if (!this.field_72738_E.isEmpty()) {
            GL11.glBlendFunc(774, 768);
            int var10 = this.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(3553, var10);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glPushMatrix();
            GL11.glDisable(3008);
            GL11.glPolygonOffset(-3.0F, -3.0F);
            GL11.glEnable(32823);
            GL11.glEnable(3008);
            par1Tessellator.startDrawingQuads();
            par1Tessellator.setTranslation(-var4, -var6, -var8);
            par1Tessellator.disableColor();
            Iterator var11 = this.field_72738_E.values().iterator();

            while(var11.hasNext()) {
                DestroyBlockProgress var12 = (DestroyBlockProgress)var11.next();
                double var13 = (double)var12.getPartialBlockX() - var4;
                double var15 = (double)var12.getPartialBlockY() - var6;
                double var17 = (double)var12.getPartialBlockZ() - var8;
                if (var13 * var13 + var15 * var15 + var17 * var17 > (double)1024.0F) {
                    var11.remove();
                } else {
                    int var19 = this.theWorld.getBlockId(var12.getPartialBlockX(), var12.getPartialBlockY(), var12.getPartialBlockZ());
                    Block var20 = var19 > 0 ? Block.blocksList[var19] : null;
                    if (var20 == null) {
                        var20 = Block.stone;
                    }

                    this.globalRenderBlocks.renderBlockUsingTexture(var20, var12.getPartialBlockX(), var12.getPartialBlockY(), var12.getPartialBlockZ(), 240 + var12.getPartialBlockDamage());
                }
            }

            par1Tessellator.draw();
            par1Tessellator.setTranslation((double)0.0F, (double)0.0F, (double)0.0F);
            GL11.glDisable(3008);
            GL11.glPolygonOffset(0.0F, 0.0F);
            GL11.glDisable(32823);
            GL11.glEnable(3008);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }
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