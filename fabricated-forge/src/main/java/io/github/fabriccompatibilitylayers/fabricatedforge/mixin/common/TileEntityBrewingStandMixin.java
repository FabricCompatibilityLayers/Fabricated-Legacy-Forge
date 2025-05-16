package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.TileEntityExtension;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityBrewingStand;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntityBrewingStand.class)
public abstract class TileEntityBrewingStandMixin implements TileEntityExtension, ISidedInventory {
    @Shadow private ItemStack[] brewingItemStacks;

    @Redirect(method = "brewPotions", at = @At(value = "NEW", target = "(Lnet/minecraft/src/Item;)Lnet/minecraft/src/ItemStack;"))
    private ItemStack forge$getContainerItemStack(Item item,
                                                  @Local ItemStack var1) {
        return ((ItemExtension) Item.itemsList[var1.itemID]).getContainerItemStack(brewingItemStacks[3]);
    }

    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        return (side == ForgeDirection.UP ? 3 : 0);
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        return (side == ForgeDirection.UP ? 1 : 3);
    }
}
