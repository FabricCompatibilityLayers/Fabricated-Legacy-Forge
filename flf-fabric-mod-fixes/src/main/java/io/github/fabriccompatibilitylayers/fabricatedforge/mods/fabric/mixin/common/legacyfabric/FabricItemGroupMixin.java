/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.fabric.mixin.common.legacyfabric;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.CreativeTabsExtension;
import net.legacyfabric.fabric.impl.item.group.FabricItemGroup;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@IfModLoaded("legacy-fabric-item-groups-v1")
@Mixin(FabricItemGroup.class)
public abstract class FabricItemGroupMixin implements CreativeTabsExtension {
    @Shadow
    @Final
    private Supplier<ItemStack> itemSupplier;

    @Override
    public ItemStack getIconItemStack() {
        return this.itemSupplier.get();
    }
}
