/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import fr.catcore.cursedmixinextensions.annotations.NewConstructor;
import fr.catcore.cursedmixinextensions.annotations.Public;
import fr.catcore.cursedmixinextensions.annotations.ShadowConstructor;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.CreativeTabsExtension;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeTabs.class)
public abstract class CreativeTabsMixin implements CreativeTabsExtension {
    @Mutable
    @Shadow @Final public static CreativeTabs[] creativeTabArray;

    @Shadow @Final private int tabIndex;

    @Shadow public abstract Item getTabIconItem();

    @ShadowConstructor
    abstract void constructor(int par1, String par2Str);

    @NewConstructor
    public void constructor(String label)
    {
        constructor(getNextID(), label);
    }

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/src/CreativeTabs;tabIndex:I"))
    private void forge$autoIncrementArraySize(int par1, String par2Str, CallbackInfo ci) {
        if (par1 >= creativeTabArray.length)
        {
            CreativeTabs[] tmp = new CreativeTabs[par1 + 1];
            System.arraycopy(creativeTabArray, 0, tmp, 0, creativeTabArray.length);
            creativeTabArray = tmp;
        }
    }

    @Inject(method = "getTabColumn", at = @At(value = "HEAD"), cancellable = true)
    private void forge$getTabColumn(CallbackInfoReturnable<Integer> cir) {
        if (tabIndex > 11)
        {
            cir.setReturnValue(((tabIndex - 12) % 10) % 5);
        }
    }

    @Inject(method = "isTabInFirstRow", at = @At(value = "HEAD"), cancellable = true)
    private void forge$isTabInFirstRow(CallbackInfoReturnable<Boolean> cir) {
        if (tabIndex > 11)
        {
            cir.setReturnValue(((tabIndex - 12) % 10) < 5);
        }
    }

    @Override
    public int getTabPage()
    {
        if (tabIndex > 11)
        {
            return ((tabIndex - 12) / 10) + 1;
        }
        return 0;
    }

    @Public
    private static int getNextID()
    {
        return creativeTabArray.length;
    }

    /**
     * Get the ItemStack that will be rendered to the tab.
     */
    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(getTabIconItem());
    }
}
