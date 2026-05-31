package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.compat.legacyfabric;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.CreativeTabsExtension;
import net.legacyfabric.fabric.impl.item.group.FabricItemGroup;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

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
