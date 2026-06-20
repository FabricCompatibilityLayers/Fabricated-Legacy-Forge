/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.client.EffectRendererExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.BlockExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common.BlockAccessor;
import net.minecraft.src.*;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeHooks;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(EffectRenderer.class)
public abstract class EffectRendererMixin implements EffectRendererExtension {

    @Shadow protected World worldObj;
    @Shadow private List[] fxLayers;

    @Shadow
    public abstract void addEffect(EntityFX par1);

    @Shadow
    public abstract void addBlockHitEffects(int par1, int par2, int par3, int par4);

    @Shadow
    private RenderEngine renderer;
    // New field merged into EffectRenderer by Mixin — groups custom-texture particles
    // by texture path for later update and render passes.
    private Multimap<String, EntityFX> effectList = ArrayListMultimap.create();

    @WrapWithCondition(method = "updateEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityFX;onUpdate()V"))
    private boolean forge$conditionalOnUpdate(EntityFX instance) {
        return instance != null;
    }

    @WrapOperation(method = "updateEffects", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityFX;isDead:Z", opcode = Opcodes.GETFIELD))
    private boolean forge$nullCheck(EntityFX instance, Operation<Boolean> original) {
        return instance != null && original.call(instance);
    }

    // Pattern A (@Inject RETURN): appends effectList update after the vanilla fxLayers loop —
    // simple end-of-method hook, no cancellation needed.
    @Inject(method = "updateEffects", at = @At("RETURN"))
    private void forge$updateEffectList(CallbackInfo ci) {
        Iterator<Map.Entry<String, EntityFX>> itr = effectList.entries().iterator();
        while (itr.hasNext()) {
            EntityFX fx = itr.next().getValue();
            fx.onUpdate();
            if (fx.isDead) {
                itr.remove();
            }
        }
    }

    /**
     * @author CatCore
     * @reason additional loop jumps
     */
    @Overwrite
    public void renderParticles(Entity par1Entity, float par2) {
        float var3 = ActiveRenderInfo.rotationX;
        float var4 = ActiveRenderInfo.rotationZ;
        float var5 = ActiveRenderInfo.rotationYZ;
        float var6 = ActiveRenderInfo.rotationXY;
        float var7 = ActiveRenderInfo.rotationXZ;
        EntityFX.interpPosX = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
        EntityFX.interpPosY = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
        EntityFX.interpPosZ = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;

        for(int var8 = 0; var8 < 3; ++var8) {
            if (!this.fxLayers[var8].isEmpty()) {
                int var9 = 0;
                if (var8 == 0) {
                    var9 = this.renderer.getTexture("/particles.png");
                }

                if (var8 == 1) {
                    var9 = this.renderer.getTexture("/terrain.png");
                }

                if (var8 == 2) {
                    var9 = this.renderer.getTexture("/gui/items.png");
                }

                GL11.glBindTexture(3553, var9);
                Tessellator var10 = Tessellator.instance;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                var10.startDrawingQuads();

                for(int var11 = 0; var11 < this.fxLayers[var8].size(); ++var11) {
                    EntityFX var12 = (EntityFX)this.fxLayers[var8].get(var11);
                    if (var12 == null) continue;
                    var10.setBrightness(var12.getBrightnessForRender(par2));
                    var12.renderParticle(var10, par2, var3, var7, var4, var5, var6);
                }

                var10.draw();
            }
        }

        for (String key : effectList.keySet()) {
            ForgeHooksClient.bindTexture(key, 0);

            for (EntityFX entry : effectList.get(key)) {
                if (entry == null) continue;
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();

                if (entry.getFXLayer() != 3) {
                    tessellator.setBrightness(entry.getBrightnessForRender(par2));
                    entry.renderParticle(tessellator, par2, var3, var7, var4, var5, var6);
                }

                tessellator.draw();
            }

            ForgeHooksClient.unbindTexture();
        }
    }

    /**
     * @author CatCore
     * @reason additional loop jumps
     */
    @Overwrite
    public void renderLitParticles(Entity par1Entity, float par2) {
        float var4 = MathHelper.cos(par1Entity.rotationYaw * ((float)Math.PI / 180F));
        float var5 = MathHelper.sin(par1Entity.rotationYaw * ((float)Math.PI / 180F));
        float var6 = -var5 * MathHelper.sin(par1Entity.rotationPitch * ((float)Math.PI / 180F));
        float var7 = var4 * MathHelper.sin(par1Entity.rotationPitch * ((float)Math.PI / 180F));
        float var8 = MathHelper.cos(par1Entity.rotationPitch * ((float)Math.PI / 180F));
        byte var9 = 3;
        if (!this.fxLayers[var9].isEmpty()) {
            Tessellator var10 = Tessellator.instance;

            for(int var11 = 0; var11 < this.fxLayers[var9].size(); ++var11) {
                EntityFX var12 = (EntityFX)this.fxLayers[var9].get(var11);
                if (var12 == null) continue;
                var10.setBrightness(var12.getBrightnessForRender(par2));
                var12.renderParticle(var10, par2, var4, var8, var5, var6, var7);
            }

        }
    }

    // Sub-hunk 5a — Pattern A (@Inject RETURN): appends effectList.clear() after fxLayers loop.
    @Inject(method = "clearEffects", at = @At("RETURN"))
    private void forge$clearEffectList(CallbackInfo ci) {
        effectList.clear();
    }

    // Sub-hunk 5b — Pattern G+E (@WrapOperation on MIXINEXTRAS:EXPRESSION): replaces the vanilla
    // "par4 != 0" guard with the Forge block hook so blocks can suppress default destroy particles.
    // Logic delta: Block lookup is duplicated here vs the body's own var6 assignment — the body's
    // var6 is still captured by the addEffect wrapper below; no semantic difference.
    @Definition(id = "par4", local = @Local(argsOnly = true, type = int.class, ordinal = 3))
    @Expression("par4 != 0")
    @WrapOperation(method = "addBlockDestroyEffects", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$checkBlockDestroyCondition(int par4, int zero, Operation<Boolean> original,
            @Local(argsOnly = true, ordinal = 0) int par1,
            @Local(argsOnly = true, ordinal = 1) int par2,
            @Local(argsOnly = true, ordinal = 2) int par3,
            @Local(argsOnly = true, ordinal = 4) int par5) {
        Block var6 = Block.blocksList[par4];
        return var6 != null && !((BlockExtension) var6).addBlockDestroyEffects(
                worldObj, par1, par2, par3, par5, (EffectRenderer)(Object)this);
    }

    // Hunk 6 — Pattern E (@WrapOperation INVOKE): intercepts the addEffect(EntityFX) call inside
    // addBlockDestroyEffects to route through the Forge-aware addEffect(EntityFX, Object) overload.
    @Redirect(
            method = "addBlockDestroyEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EffectRenderer;addEffect(Lnet/minecraft/src/EntityFX;)V")
    )
    private void forge$addDestroyEffectWithBlock(EffectRenderer instance, EntityFX fx, @Local(ordinal = 0) Block var6) {
        this.addEffect(fx, var6);
    }

    // Sub-hunk 7a — Pattern E (@WrapOperation INVOKE): same shape as hunk 6 but targeting
    // the addEffect call-site inside addBlockHitEffects(IIII).
    @Redirect(
            method = "addBlockHitEffects(IIII)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EffectRenderer;addEffect(Lnet/minecraft/src/EntityFX;)V")
    )
    private void forge$addHitEffectWithBlock(EffectRenderer instance, EntityFX fx, @Local(ordinal = 0) Block var6) {
        this.addEffect(fx, var6);
    }

    // Sub-hunk 7b — Pattern D (@Overwrite): rewrites getStatistics() to include all 4 fxLayers
    // and effectList.size(); vanilla only counted layers 0–2.
    /**
     * @author FabricCompatibilityLayers
     * @reason Counts all fxLayers and effectList entries for accurate particle statistics
     */
    @Overwrite
    public String getStatistics() {
        int size = 0;
        for (List x : fxLayers) {
            size += x.size();
        }
        size += effectList.size();
        return Integer.toString(size);
    }

    // Sub-hunk 7c — Pattern J (Extension interface): routes particles with custom block/item
    // textures into effectList; falls back to vanilla addEffect(EntityFX) for default textures.
    @Override  // → Extension interface
    public void addEffect(EntityFX effect, Object obj) {
        if (obj == null || !(obj instanceof Block || obj instanceof Item)) {
            addEffect(effect);
            return;
        }
        if (obj instanceof Item && ((ItemExtension) obj).isDefaultTexture()) {
            addEffect(effect);
            return;
        }
        if (obj instanceof Block && ((BlockAccessor) obj).isDefaultTexture()) {
            addEffect(effect);
            return;
        }
        String texture = "/terrain.png";
        if (effect.getFXLayer() == 0) {
            texture = "/particles.png";
        } else if (effect.getFXLayer() == 2) {
            texture = "/gui/items.png";
        }
        texture = ForgeHooks.getTexture(texture, obj);
        effectList.put(texture, effect);
    }

    // Sub-hunk 7d — Pattern J (Extension interface): Forge overload that lets blocks override
    // hit-particle behaviour via Block.addBlockHitEffects() before falling back to the IIII overload.
    @Override  // → Extension interface
    public void addBlockHitEffects(int x, int y, int z, MovingObjectPosition target) {
        Block block = Block.blocksList[worldObj.getBlockId(x, y, z)];
        if (block != null && !((BlockExtension) block).addBlockHitEffects(worldObj, target, (EffectRenderer)(Object)this)) {
            addBlockHitEffects(x, y, z, target.sideHit);
        }
    }

}