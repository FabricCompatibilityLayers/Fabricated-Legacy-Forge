package fr.catcore.fabricatedforge.mixin.forgefml.screen;

import fr.catcore.fabricatedforge.mixininterface.ISmeltingRecipeRegistry;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FurnaceScreenHandler.class)
public abstract class FurnaceScreenHandlerMixin extends ScreenHandler {

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ItemStack method_3265(int par1) {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.slots.get(par1);
        if (var3 != null && var3.hasStack()) {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();
            if (par1 == 2) {
                if (!this.insertItem(var4, 3, 39, true)) {
                    return null;
                }

                var3.onStackChanged(var4, var2);
            } else if (par1 != 1 && par1 != 0) {
                if (((ISmeltingRecipeRegistry)SmeltingRecipeRegistry.getInstance()).getSmeltingResult(var4) != null) {
                    if (!this.insertItem(var4, 0, 1, false)) {
                        return null;
                    }
                } else if (FurnaceBlockEntity.isFuel(var4)) {
                    if (!this.insertItem(var4, 1, 2, false)) {
                        return null;
                    }
                } else if (par1 >= 3 && par1 < 30) {
                    if (!this.insertItem(var4, 30, 39, false)) {
                        return null;
                    }
                } else if (par1 >= 30 && par1 < 39 && !this.insertItem(var4, 3, 30, false)) {
                    return null;
                }
            } else if (!this.insertItem(var4, 3, 39, false)) {
                return null;
            }

            if (var4.count == 0) {
                var3.setStack((ItemStack)null);
            } else {
                var3.markDirty();
            }

            if (var4.count == var2.count) {
                return null;
            }

            var3.method_3298(var4);
        }

        return var2;
    }
}
