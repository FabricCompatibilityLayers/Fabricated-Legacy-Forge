/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.TileEntityExtension;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TileEntityRenderer.class)
public class TileEntityRendererMixin {

    // Pattern E (@WrapOperation at CONSTANT): intercepts the 4096.0 double constant directly —
    // more surgical than wrapping getDistanceFrom() and doesn't encode magic values in the handler.
    // @Local(argsOnly = true) captures par1TileEntity from the method args so getRenderDistance()
    // can be called without any @Shadow.
    // Logic delta: vanilla always tests against 4096.0 (= 64²); after injection each TileEntity
    // can advertise its own render distance via TileEntityExtension#getRenderDistance(), and the
    // threshold becomes renderDist² to match the squared-distance comparison already in place.
    @ModifyConstant(
        method = "renderTileEntity",
        constant = @Constant(doubleValue = 4096.0)
    )
    private double forge$modifyRenderDistance(double constant,
                                              @Local(argsOnly = true) TileEntity tileEntity) {
        double renderDist = ((TileEntityExtension) tileEntity).getRenderDistance();
        return renderDist * renderDist;
    }
}