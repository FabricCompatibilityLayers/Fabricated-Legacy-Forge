/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import net.minecraft.src.ItemStack;
import net.minecraft.src.WeightedRandomChestContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = WeightedRandomChestContent.class, priority = 1001)
public interface WeightedRandomChestContentAccessor {
    @Accessor("itemStack")
    ItemStack getItemStack();
    @Accessor("itemStack")
    void setItemStack(ItemStack itemStack);
}
