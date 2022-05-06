package cpw.mods.fml.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface ICraftingHandler {
    void onCrafting(PlayerEntity arg, ItemStack arg2, Inventory arg3);

    void onSmelting(PlayerEntity arg, ItemStack arg2);
}
