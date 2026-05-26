package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
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

    // New field merged into EffectRenderer by Mixin — groups custom-texture particles
    // by texture path for later update and render passes.
    private Multimap<String, EntityFX> effectList = ArrayListMultimap.create();

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

    // Pattern A (@Inject RETURN): appends effectList render pass after the vanilla 3-layer loop.
    // Captures var3–var7 (ActiveRenderInfo rotation values assigned at method top) via @Local.
    // par2 float ordinal 0 is the method parameter itself; var3–var7 follow as ordinals 1–5.
    @Inject(method = "renderParticles", at = @At("RETURN"))
    private void forge$renderEffectList(Entity par1Entity, float par2, CallbackInfo ci,
                                        @Local(ordinal = 1) float var3,
                                        @Local(ordinal = 2) float var4,
                                        @Local(ordinal = 3) float var5,
                                        @Local(ordinal = 4) float var6,
                                        @Local(ordinal = 5) float var7) {
        for (String key : effectList.keySet()) {
            ForgeHooksClient.bindTexture(key, 0);

            for (EntityFX entry : effectList.get(key)) {
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