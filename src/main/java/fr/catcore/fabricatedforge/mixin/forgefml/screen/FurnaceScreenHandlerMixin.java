package fr.catcore.fabricatedforge.mixin.forgefml.screen;

import fr.catcore.fabricatedforge.mixininterface.ISmeltingRecipeRegistry;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
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
    public ItemStack transferSlot(PlayerEntity par1EntityPlayer, int par2) {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.slots.get(par2);
        if (var4 != null && var4.hasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (par2 == 2) {
                if (!this.insertItem(var5, 3, 39, true)) {
                    return null;
                }

                var4.onStackChanged(var5, var3);
            } else if (par2 != 1 && par2 != 0) {
                if (((ISmeltingRecipeRegistry)SmeltingRecipeRegistry.getInstance()).getSmeltingResult(var5) != null) {
                    if (!this.insertItem(var5, 0, 1, false)) {
                        return null;
                    }
                } else if (FurnaceBlockEntity.isFuel(var5)) {
                    if (!this.insertItem(var5, 1, 2, false)) {
                        return null;
                    }
                } else if (par2 >= 3 && par2 < 30) {
                    if (!this.insertItem(var5, 30, 39, false)) {
                        return null;
                    }
                } else if (par2 >= 30 && par2 < 39 && !this.insertItem(var5, 3, 30, false)) {
                    return null;
                }
            } else if (!this.insertItem(var5, 3, 39, false)) {
                return null;
            }

            if (var5.count == 0) {
                var4.setStack((ItemStack)null);
            } else {
                var4.markDirty();
            }

            if (var5.count == var3.count) {
                return null;
            }

            var4.onTakeItem(par1EntityPlayer, var5);
        }

        return var3;
    }
}
