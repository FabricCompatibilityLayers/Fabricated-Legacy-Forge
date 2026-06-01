/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedfml.extension.common.MapDataExtension;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapData.class)
public class MapDataMixin implements MapDataExtension {
    // c:I;
    public int dimensionId;

    @Inject(method = "func_76184_a", at = @At("HEAD"))
    private void fml$readDimensionId(NBTTagCompound p_76184_1_, CallbackInfo ci) {
        NBTBase dimension = p_76184_1_.func_74781_a("dimension");

        if (dimension instanceof NBTTagByte)
        {
            this.dimensionId = ((NBTTagByte)dimension).field_74756_a;
        }
        else
        {
            this.dimensionId = ((NBTTagInt)dimension).field_74748_a;
        }
    }

    @Redirect(method = "func_76187_b", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/NBTTagCompound;func_74774_a(Ljava/lang/String;B)V", ordinal = 0))
    private void fml$writeDimensionId(NBTTagCompound instance, String p_74774_2_, byte b) {
        instance.func_74768_a(p_74774_2_, this.dimensionId);
    }

    @Override
    public int getDimensionId() {
        return dimensionId;
    }

    @Override
    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }
}
