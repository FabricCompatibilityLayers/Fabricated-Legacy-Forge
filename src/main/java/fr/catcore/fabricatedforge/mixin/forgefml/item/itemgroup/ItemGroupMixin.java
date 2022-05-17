package fr.catcore.fabricatedforge.mixin.forgefml.item.itemgroup;

import fr.catcore.fabricatedforge.mixininterface.IItemGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemGroup.class)
public abstract class ItemGroupMixin implements IItemGroup {

    @Mutable
    @Shadow @Final public static ItemGroup[] itemGroups;

    @Shadow @Final private int index;

    @Shadow public abstract Item method_3320();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fmlCtr(int par1, String par2, CallbackInfo ci) {
        if (par1 >= itemGroups.length) {
            ItemGroup[] tmp = new ItemGroup[par1 + 1];

            for(int x = 0; x < itemGroups.length; ++x) {
                tmp[x] = itemGroups[x];
            }

            itemGroups = tmp;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public int getColumn() {
        return this.index > 11 ? (this.index - 12) % 10 % 5 : this.index % 6;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public boolean isTopRow() {
        if (this.index > 11) {
            return (this.index - 12) % 10 < 5;
        } else {
            return this.index < 6;
        }
    }

    @Override
    public int getTabPage() {
        return this.index > 11 ? (this.index - 12) / 10 + 1 : 0;
    }

//    // Public
//    private static int getNextID() {
//        return itemGroups.length;
//    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(this.method_3320());
    }
}
