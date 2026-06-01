/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import net.minecraft.src.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Tessellator.class, priority = 1001)
public interface TessellatorAccessor {
    @Accessor("textureID")
    void setTextureID(int id);
    @Accessor("renderingWorldRenderer")
    static void setRenderingWorldRenderer(boolean renderingWorldRenderer) { }
    @Accessor("renderingWorldRenderer")
    static boolean isRenderingWorldRenderer() { return false; }
    @Accessor("defaultTexture")
    boolean isDefaultTexture();
}
