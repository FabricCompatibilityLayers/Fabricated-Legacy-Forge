/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.Block;
import net.minecraft.src.ItemMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemMap.class)
public class ItemMapMixin {
    @ModifyConstant(method = "updateMapData", constant = @Constant(intValue = 256))
    private int forge$deharcodeBlockIdLimit(int constant) {
        return Block.blocksList.length;
    }
}
