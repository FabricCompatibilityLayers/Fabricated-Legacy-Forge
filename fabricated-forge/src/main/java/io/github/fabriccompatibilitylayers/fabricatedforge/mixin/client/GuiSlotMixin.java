/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.client.GuiSlotExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiSlot.class)
public abstract class GuiSlotMixin implements GuiSlotExtension {

    @Shadow @Final private Minecraft mc;
    @Shadow private int left;
    @Shadow private int right;
    @Shadow protected int top;
    @Shadow protected int bottom;
    @Shadow private float amountScrolled;

    public String BACKGROUND_IMAGE = "/gui/background.png";

    // Pattern E+Q: @WrapWithCondition on glBindTexture(ordinal=0) activates a @Share flag and
    // suppresses the call; the remaining four setup calls (glColor4f, startDrawingQuads,
    // setColorOpaque_I, addVertexWithUV) are each suppressed by a @WrapWithCondition that checks
    // the flag. @WrapOperation on draw()(ordinal=0) then calls drawContainerBackground and clears
    // the flag, leaving all later draw() calls in the method unaffected.
    // Logic delta: patch replaces the block with a direct call; here the block is suppressed
    // instruction-by-instruction — identical net effect, method stays open to other injectors.

    @WrapWithCondition(method = "drawScreen",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 0))
    private boolean forge$startBgSuppression(int target, int texture,
        @Share(value = "bgSuppressed", namespace = "fabricated-forge") LocalIntRef bgRef) {
        bgRef.set(1);
        return false;
    }

    @WrapWithCondition(method = "drawScreen",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V"))
    private boolean forge$suppressBgColor(float r, float g, float b, float a,
        @Share(value = "bgSuppressed", namespace = "fabricated-forge") LocalIntRef bgRef) {
        return bgRef.get() == 0;
    }

    @WrapWithCondition(method = "drawScreen",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;startDrawingQuads()V"))
    private boolean forge$suppressBgStartDraw(Tessellator tessellator,
        @Share(value = "bgSuppressed", namespace = "fabricated-forge") LocalIntRef bgRef) {
        return bgRef.get() == 0;
    }

    @WrapWithCondition(method = "drawScreen",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;setColorOpaque_I(I)V"))
    private boolean forge$suppressBgSetColor(Tessellator tessellator, int color,
        @Share(value = "bgSuppressed", namespace = "fabricated-forge") LocalIntRef bgRef) {
        return bgRef.get() == 0;
    }

    @WrapWithCondition(method = "drawScreen",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;addVertexWithUV(DDDDD)V"))
    private boolean forge$suppressBgVertex(Tessellator tessellator,
        double x, double y, double z, double u, double v,
        @Share(value = "bgSuppressed", namespace = "fabricated-forge") LocalIntRef bgRef) {
        return bgRef.get() == 0;
    }

    @WrapOperation(method = "drawScreen",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;draw()I", ordinal = 0))
    private int forge$callDrawContainerBackground(Tessellator tessellator, Operation<Integer> original,
        @Share(value = "bgSuppressed", namespace = "fabricated-forge") LocalIntRef bgRef) {
        if (bgRef.get() == 1) {
            bgRef.set(0);
            drawContainerBackground(tessellator);
            return 0;
        }
        return original.call(tessellator);
    }

    // Pattern E (@WrapOperation): intercepts the single getTexture call inside overlayBackground
    // to substitute BACKGROUND_IMAGE for the hardcoded "/gui/background.png" string.
    // No ordinal needed — there is only one getTexture call in this method.
    @WrapOperation(method = "overlayBackground",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I"))
    private int forge$overlayBackgroundTexture(RenderEngine renderEngine, String path, Operation<Integer> original) {
        return original.call(renderEngine, BACKGROUND_IMAGE);
    }

    // Pattern J (extension interface): drawContainerBackground is added via GuiSlotExtension so
    // GuiSlot subclasses can override it. The forge$callDrawContainerBackground wrapper (hunk 2)
    // calls this method directly since GuiSlotMixin implements the interface.
    @Override // → Extension interface
    public void drawContainerBackground(Tessellator tess) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(BACKGROUND_IMAGE));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float height = 32.0F;
        tess.startDrawingQuads();
        tess.setColorOpaque_I(2105376);
        tess.addVertexWithUV((double) left,  (double) bottom, 0.0D, (double) (left  / height), (double) ((bottom + (int) amountScrolled) / height));
        tess.addVertexWithUV((double) right, (double) bottom, 0.0D, (double) (right / height), (double) ((bottom + (int) amountScrolled) / height));
        tess.addVertexWithUV((double) right, (double) top,    0.0D, (double) (right / height), (double) ((top    + (int) amountScrolled) / height));
        tess.addVertexWithUV((double) left,  (double) top,    0.0D, (double) (left  / height), (double) ((top    + (int) amountScrolled) / height));
        tess.draw();
    }

}