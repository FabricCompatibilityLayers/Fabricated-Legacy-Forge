/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.osl.registries;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.osl.OSLRegisterHelper;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.compat.registry.FLFCompatRegistries;
import net.minecraft.src.SaveHandler;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;

import java.io.File;

@Conditional(modLoaded = @Mod("osl-registries"))
@Mixin(value = SaveHandler.class, priority = 1500)
public abstract class SaveHandlerMixin {
    @Shadow
    protected abstract File getSaveDirectory();

    @TargetHandler(
            mixin = "net.ornithemc.osl.registries.impl.mixin.common.AlphaWorldStorageMixin",
            name = "osl$registries$loadRegistryMappings"
    )
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", desc = @Desc(
            owner = SaveHandler.class,
            value = "readRegistryMappings",
            args = {
                    File.class,
                    boolean.class
            },
            ret = boolean.class), ordinal = 1))
    private boolean lf$readLegacyFabricRegistryMappings(SaveHandler instance, File file, boolean throwOnException, Operation<Boolean> original) {
        if (!original.call(instance, file, throwOnException)) {
            File dir = this.getSaveDirectory();
            File file2 = new File(dir, FLFCompatRegistries.FILE_NAME);

            return OSLRegisterHelper.loadFLFFile(file2);
        }

        return true;
    }
}
