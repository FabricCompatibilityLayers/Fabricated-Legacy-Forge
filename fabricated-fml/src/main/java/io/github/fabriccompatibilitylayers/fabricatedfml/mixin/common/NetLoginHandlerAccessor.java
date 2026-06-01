/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import net.minecraft.src.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetLoginHandler.class)
public interface NetLoginHandlerAccessor {
    @Accessor("field_72544_i")
    void setFlag(boolean flag);
}
