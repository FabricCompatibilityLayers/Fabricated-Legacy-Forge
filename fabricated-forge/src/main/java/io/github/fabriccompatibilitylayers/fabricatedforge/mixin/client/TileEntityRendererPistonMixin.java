package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntityPiston;
import net.minecraft.src.TileEntityRendererPiston;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererPiston.class)
public abstract class TileEntityRendererPistonMixin {

    @Shadow private RenderBlocks blockRenderer;

    // Pattern A + H (@Inject INVOKE + @Local): injects before startDrawingQuads() to fire the
    // Forge before-render hook; var9 is the first Block local in the method (ordinal 0).
    @Inject(
        method = "renderPiston",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;startDrawingQuads()V")
    )
    private void forge$beforeBlockRender(TileEntityPiston par1TileEntityPiston, double par2, double par4, double par6, float par8, CallbackInfo ci,
                                         @Local(ordinal = 0) Block var9) {
        ForgeHooksClient.beforeBlockRender(var9, blockRenderer);
    }

    // Pattern A + H (@Inject INVOKE AFTER + @Local): injects after draw() to fire the
    // Forge after-render hook; shift = AFTER so execution resumes after the call completes.
    // var9 reuses the same first Block local (ordinal 0); @Shadow blockRenderer already declared above.
    @Inject(
        method = "renderPiston",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderHelper;enableStandardItemLighting()V")
    )
    private void forge$afterBlockRender(TileEntityPiston par1TileEntityPiston, double par2, double par4, double par6, float par8, CallbackInfo ci,
                                        @Local(ordinal = 0) Block var9) {
        ForgeHooksClient.afterBlockRender(var9, blockRenderer);
    }
}