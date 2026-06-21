/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import io.github.fabriccompatibilitylayers.fabricatedfml.utils.MakeStatic;
import net.minecraft.src.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

@Mixin(value = Tessellator.class, priority = 9999)
public class TessellatorLateMixin {
    @Shadow
    @MakeStatic
    private ByteBuffer byteBuffer;
    @Shadow @MakeStatic private IntBuffer intBuffer;
    @Shadow @MakeStatic private FloatBuffer floatBuffer;
    @Shadow @MakeStatic private ShortBuffer shortBuffer;

    // Same two-step approach. vboCount's initializer (= 10) lives in the vanilla constructor;
    // postApply's PUTFIELD→PUTSTATIC rewrite picks it up there too.
    @Shadow @MakeStatic private boolean useVBO;
    @Shadow @MakeStatic private IntBuffer vertexBuffers;
    @Shadow @MakeStatic private int vboCount;
}
