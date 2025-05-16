package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.FurnaceRecipesExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.ItemExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.TileEntityExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntityFurnace.class)
public abstract class TileEntityFurnaceMixin extends TileEntity implements TileEntityExtension, ISidedInventory, IInventory {
    @Shadow private ItemStack[] furnaceItemStacks;

    @Definition(id = "furnaceItemStacks", field = "Lnet/minecraft/src/TileEntityFurnace;furnaceItemStacks:[Lnet/minecraft/src/ItemStack;")
    @Expression("this.furnaceItemStacks[1] = ?")
    @WrapOperation(method = "updateEntity", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void forge$getContainerItemStack(ItemStack[] array, int index, ItemStack value, Operation<Void> original) {
        original.call(array, index, ((ItemExtension) array[1].getItem()).getContainerItemStack(array[1]));
    }

    @Redirect(method = {"canSmelt", "smeltItem"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/src/FurnaceRecipes;getSmeltingResult(I)Lnet/minecraft/src/ItemStack;"))
    private ItemStack forge$getSmeltingResult(FurnaceRecipes instance, int i) {
        return ((FurnaceRecipesExtension) instance).getSmeltingResult(this.furnaceItemStacks[0]);
    }

    @Definition(id = "furnaceItemStacks", field = "Lnet/minecraft/src/TileEntityFurnace;furnaceItemStacks:[Lnet/minecraft/src/ItemStack;")
    @Definition(id = "stackSize", field = "Lnet/minecraft/src/ItemStack;stackSize:I")
    @Expression("this.furnaceItemStacks[2].stackSize < ?")
    @WrapOperation(method = "canSmelt", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$fixResult(int left, int right, Operation<Boolean> original,
                                    @Local ItemStack var1) {
        return original.call(left + var1.stackSize, right);
    }

    @ModifyReceiver(method = "canSmelt", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemStack;getMaxStackSize()I", ordinal = 0))
    private ItemStack forge$swapStack(ItemStack instance,
                                      @Local ItemStack var1) {
        return var1;
    }

    @Definition(id = "furnaceItemStacks", field = "Lnet/minecraft/src/TileEntityFurnace;furnaceItemStacks:[Lnet/minecraft/src/ItemStack;")
    @Definition(id = "stackSize", field = "Lnet/minecraft/src/ItemStack;stackSize:I")
    @Expression("this.furnaceItemStacks[2].stackSize < ?")
    @WrapOperation(method = "canSmelt", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 2))
    private boolean forge$hackLastCheck(int left, int right, Operation<Boolean> original) {
        return false;
    }

    @Definition(id = "furnaceItemStacks", field = "Lnet/minecraft/src/TileEntityFurnace;furnaceItemStacks:[Lnet/minecraft/src/ItemStack;")
    @Definition(id = "itemID", field = "Lnet/minecraft/src/ItemStack;itemID:I")
    @Expression("this.furnaceItemStacks[?].itemID == ?")
    @WrapOperation(method = "smeltItem", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean forge$isItemEqual(int left, int right, Operation<Boolean> original,
                                      @Local ItemStack var1) {
        return this.furnaceItemStacks[2].isItemEqual(var1);
    }

    @Definition(id = "furnaceItemStacks", field = "Lnet/minecraft/src/TileEntityFurnace;furnaceItemStacks:[Lnet/minecraft/src/ItemStack;")
    @Definition(id = "stackSize", field = "Lnet/minecraft/src/ItemStack;stackSize:I")
    @Expression("?.stackSize = ?.stackSize + 1")
    @WrapOperation(method = "smeltItem", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void forge$fixMerge(ItemStack instance, int value, Operation<Void> original,
                                @Local ItemStack var1) {
        original.call(instance, value - 1 + var1.stackSize);
    }

    @Expression("? < 256")
    @WrapOperation(method = "getItemBurnTime", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean forge$ItemBlockCheck(int left, int right, Operation<Boolean> original,
                                                @Local ItemStack var1) {
        return var1.getItem() instanceof ItemBlock;
    }

    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        if (side == ForgeDirection.DOWN) return 1;
        if (side == ForgeDirection.UP) return 0;
        return 2;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        return 1;
    }
}
