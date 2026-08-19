/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.registry;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.ItemRegistrationHelper;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    @Shadow
    @Final
    public int shiftedIndex;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void osl$registryFMLItems(int par1, CallbackInfo ci) {
        if (this.shiftedIndex == 0) return;

        if (ItemRegistrationHelper.ready && !((Object)this instanceof ItemBlock)) {
            ModContainer container = Loader.instance().activeModContainer();

            if (container != null) {
                ItemRegistrationHelper.registerItem((Item) (Object) this, container);
            }
        }
    }
}
