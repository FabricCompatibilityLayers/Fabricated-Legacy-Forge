package fr.catcore.fabricatedforge.mixin.forgefml.item.itemgroup;

import fr.catcore.fabricatedforge.mixininterface.IItem;
import fr.catcore.fabricatedforge.mixininterface.IItemGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.itemgroup.ItemGroup;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemGroup.class)
public abstract class ItemGroupMixin implements IItemGroup {

    @Mutable
    @Shadow @Final public static ItemGroup[] itemGroups;

    @Shadow @Final private int index;

    @Shadow public abstract Item method_3320();

    @Inject(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/item/itemgroup/ItemGroup;index:I"))
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

    /**
     * @author fml
     * @reason yes
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public void showItems(List par1List) {
        for(Item var5 : Item.ITEMS) {
            if (var5 != null) {
                for(ItemGroup tab : ((IItem)var5).getCreativeTabs()) {
                    if (tab == ((Object) this)) {
                        var5.method_3345(var5.id, (ItemGroup) (Object) this, par1List);
                    }
                }
            }
        }
    }

    @Override
    public int getTabPage() {
        return this.index > 11 ? (this.index - 12) / 10 + 1 : 0;
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(this.method_3320());
    }
}
