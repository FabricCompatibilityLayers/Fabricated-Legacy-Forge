package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    @Shadow
    public List slots;

    /**
     * @reason ModLoader patch
     * @author Risugami
     */
    @Overwrite
    public boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        boolean var5 = false;
        int var6 = startIndex;
        if (fromLast) {
            var6 = endIndex - 1;
        }

        Slot var7;
        ItemStack var8;
        int maxStack;
        if (stack.isDamaged()) {
            while (stack.count > 0 && (!fromLast && var6 < endIndex || fromLast && var6 >= startIndex)) {
                var7 = (Slot) this.slots.get(var6);
                var8 = var7.getStack();
                maxStack = Math.min(var7.getMaxStackAmount(), stack.getMaxCount());
                if (var8 != null && var7.canInsert(stack) && var8.id == stack.id && (!stack.isUnbreakable() || stack.getMeta() == var8.getMeta()) && ItemStack.equalsIgnoreDamage(stack, var8)) {
                    int var9 = var8.count + stack.count;
                    if (var9 <= maxStack) {
                        stack.count = 0;
                        var8.count = var9;
                        var7.markDirty();
                        var5 = true;
                    } else if (var8.count < maxStack) {
                        stack.count -= maxStack - var8.count;
                        var8.count = maxStack;
                        var7.markDirty();
                        var5 = true;
                    }
                }

                if (fromLast) {
                    --var6;
                } else {
                    ++var6;
                }
            }
        }

        if (stack.count > 0) {
            if (fromLast) {
                var6 = endIndex - 1;
            } else {
                var6 = startIndex;
            }

            while (!fromLast && var6 < endIndex || fromLast && var6 >= startIndex) {
                var7 = (Slot) this.slots.get(var6);
                var8 = var7.getStack();
                maxStack = Math.min(var7.getMaxStackAmount(), stack.getMaxCount());
                if (var8 == null && var7.canInsert(stack)) {
                    var8 = stack.copy();
                    var7.setStack(var8);
                    if (stack.count > maxStack) {
                        stack.count -= maxStack;
                        var8.count = maxStack;
                    } else {
                        stack.count = 0;
                    }

                    var7.markDirty();
                    var5 = true;
                    break;
                }

                if (fromLast) {
                    --var6;
                } else {
                    ++var6;
                }
            }
        }

        return var5;
    }
}
