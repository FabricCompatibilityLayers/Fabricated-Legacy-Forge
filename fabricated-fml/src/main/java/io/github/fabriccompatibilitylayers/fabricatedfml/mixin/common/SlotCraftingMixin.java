/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlotCrafting.class)
public class SlotCraftingMixin extends Slot {
    @Shadow private EntityPlayer field_75238_b;

    @Shadow @Final private IInventory field_75239_a;

    public SlotCraftingMixin(IInventory p_i3616_1_, int p_i3616_2_, int p_i3616_3_, int p_i3616_4_) {
        super(p_i3616_1_, p_i3616_2_, p_i3616_3_, p_i3616_4_);
    }

    @Inject(method = "func_75213_b", at = @At("HEAD"))
    private void fml$onItemCrafted(ItemStack p_75213_1_, CallbackInfo ci) {
        GameRegistry.onItemCrafted(field_75238_b, p_75213_1_, field_75239_a);
    }
}
