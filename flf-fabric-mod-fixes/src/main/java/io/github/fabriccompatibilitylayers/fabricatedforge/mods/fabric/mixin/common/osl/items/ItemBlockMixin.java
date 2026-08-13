/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.items;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.items.ItemRegistrationHelper;
import net.minecraft.src.ItemBlock;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Conditional(modLoaded = @Mod("osl-items"))
@Mixin(ItemBlock.class)
public class ItemBlockMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void osl$registryFMLItems(int par1, CallbackInfo ci) {
        if (ItemRegistrationHelper.ready) {
            ModContainer container = Loader.instance().activeModContainer();

            if (container != null) {
                ItemRegistrationHelper.registerItemBlock((ItemBlock) (Object) this, container);
            }
        }
    }
}
