package fr.catcore.fabricatedforge.mixin.forgefml.screen;

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
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.slots.get(invSlot);
        if (var4 != null && var4.hasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (invSlot == 2) {
                if (!this.insertItem(var5, 3, 39, true)) {
                    return null;
                }

                var4.onStackChanged(var5, var3);
            } else if (invSlot != 1 && invSlot != 0) {
                if (SmeltingRecipeRegistry.getInstance().method_3490(var5.getItem().id) != null) {
                    if (!this.insertItem(var5, 0, 1, false)) {
                        return null;
                    }
                } else if (FurnaceBlockEntity.isFuel(var5)) {
                    if (!this.insertItem(var5, 1, 2, false)) {
                        return null;
                    }
                } else if (invSlot >= 3 && invSlot < 30) {
                    if (!this.insertItem(var5, 30, 39, false)) {
                        return null;
                    }
                } else if (invSlot >= 30 && invSlot < 39 && !this.insertItem(var5, 3, 30, false)) {
                    return null;
                }
            } else if (!this.insertItem(var5, 3, 39, false)) {
                return null;
            }

            if (var5.count == 0) {
                var4.setStack(null);
            } else {
                var4.markDirty();
            }

            if (var5.count == var3.count) {
                return null;
            }

            var4.onTakeItem(player, var5);
        }

        return var3;
    }
}
